/*
 * Copyright 2015 Demigods RPG
 * Copyright 2015 Alexander Chauncey
 * Copyright 2015 Alex Bennett
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demigodsrpg.game.battle;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.model.PlayerModel;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Report {
    private long battleLength;
    private Battle battle;

    private Multimap<Participant, String> participantScores;
    private Multimap<Faction, String> factionScores;
    private Map<Participant, Double> weightedKillDeathRatios;
    private Map<Faction, Double> weightedFactionRatios;

    public Report(Battle battle) {
        battleLength = battle.getEndTimeMillis() - battle.getStartTimeMillis();
        this.battle = battle;

        // Get the stats together
        participantScores = getParticipantScores();
        factionScores = getFactionScores();
        weightedKillDeathRatios = getWeightedKillDeathRatios();
        weightedFactionRatios = getWeightedFactionRatios();
    }

    public void sendToServer() {
        Multimap<Participant, String> scores = getParticipantScores();
        List<Participant> participants = new ArrayList<>(scores.keySet());
        if (participants.size() == 2) {
            double delta = getWeightedKillDeathRatio(participants.get(0)) - getWeightedKillDeathRatio(participants.get(1));
            delta = delta < 0 ? delta * -1 : delta;
            if (delta > 0.8) {
                Participant one = participants.get(0);
                Participant two = participants.get(1);
                Bukkit.broadcastMessage(one.getFaction().getColor() + one.getLastKnownName() + ChatColor.GRAY + " and " + two.getFaction().getColor() + two.getLastKnownName() + ChatColor.GRAY + " just tied in a duel.");
            } else {
                int winnerIndex = getWeightedKillDeathRatio(participants.get(0)) > getWeightedKillDeathRatio(participants.get(1)) ? 0 : 1;
                Participant winner = participants.get(winnerIndex);
                Participant loser = participants.get(winnerIndex == 0 ? 1 : 0);
                Bukkit.broadcastMessage(winner.getFaction().getColor() + winner.getLastKnownName() + ChatColor.GRAY + " just won in a duel against " + loser.getFaction().getColor() + loser.getLastKnownName() + ChatColor.GRAY + ".");
            }
        } else if (participants.size() > 2) {
            double winningScore = 0;
            Faction winningfaction = null;
            List<Participant> MVPs = getMVPs();
            boolean oneMVP = MVPs.size() == 1;
            for (Map.Entry<Faction, Double> entry : weightedFactionRatios.entrySet()) {
                double score = entry.getValue();
                if (score > winningScore) {
                    winningfaction = entry.getKey();
                    winningScore = score;
                }
            }
            if (winningfaction != null) {
                Bukkit.broadcastMessage(ChatColor.GRAY + "The " + ChatColor.YELLOW + winningfaction.getName() + " faction " + ChatColor.GRAY + "just won a battle involving " + participants.size() + " participants.");
                Bukkit.broadcastMessage(ChatColor.GRAY + "The " + ChatColor.YELLOW + "MVP" + (oneMVP ? "" : "s") + ChatColor.GRAY + " from this battle " + (oneMVP ? "is" : "are") + ":");
                for (Participant mvp : MVPs) {
                    BattleMetaData data = battle.getInvolved().get(mvp.getPersistentId());
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + " ➥ " + mvp.getFaction().getColor() + mvp.getLastKnownName() + ChatColor.GRAY + " / " + ChatColor.YELLOW + "Kills" + ChatColor.GRAY + ": " + data.getKills() + " / " + ChatColor.YELLOW + "Deaths" + ChatColor.GRAY + ": " + data.getDeaths());
                }
            }
        }
    }

    public void sendToFactions() {
        // TODO
    }

    public void sendToInvolved() {
        for (Participant participant : participantScores.keySet()) {
            OfflinePlayer offline = ((PlayerModel) participant).getOfflinePlayer();
            if (offline.isOnline()) {
                Player player = offline.getPlayer();
                player.sendMessage("  ");
                player.sendMessage(ChatColor.YELLOW + "Your stats:");
                participantScores.get(participant).forEach(player::sendMessage);
                player.sendMessage("  ");
            }
        }
    }

    public double getWeightedKillDeathRatio(Participant participant) {
        BattleMetaData data = battle.getInvolved().get(participant.getPersistentId());
        if (data.deaths == 0) {
            return data.getKills() * 1.2;
        }
        return (data.getKills() + data.getDeaths()) / data.deaths;
    }

    public Map<Participant, Double> getWeightedKillDeathRatios() {
        Map<Participant, Double> weightedRatios = new HashMap<>();
        for (Map.Entry<String, BattleMetaData> entry : battle.getInvolved().entrySet()) {
            Participant participant = DGGame.PLAYER_R.fromId(entry.getKey());
            weightedRatios.put(participant, getWeightedKillDeathRatio(participant));
        }
        return weightedRatios;
    }

    public Map<Faction, Double> getWeightedFactionRatios() {
        Map<Faction, Double> weightedRatios = new HashMap<>();
        for (Map.Entry<Participant, Double> entry : weightedKillDeathRatios.entrySet()) {
            Faction participantFaction = entry.getKey().getFaction();
            double score = weightedRatios.getOrDefault(participantFaction, 0.0);
            score += entry.getValue();
            weightedRatios.put(participantFaction, score);
        }
        return weightedRatios;
    }

    public Multimap<Participant, String> getParticipantScores() {
        Multimap<Participant, String> scores = Multimaps.newListMultimap(new ConcurrentHashMap<>(), ArrayList::new);
        for (Map.Entry<String, BattleMetaData> entry : battle.getInvolved().entrySet()) {
            Participant participant = DGGame.PLAYER_R.fromId(entry.getKey());
            if (entry.getValue().hits > 0) {
                scores.put(participant, ChatColor.DARK_GRAY + " ➥ " + ChatColor.YELLOW + "Hits: " + ChatColor.WHITE + entry.getValue().hits);
            }
            if (entry.getValue().kills > 0) {
                scores.put(participant, ChatColor.DARK_GRAY + " ➥ " + ChatColor.YELLOW + "Kills: " + ChatColor.WHITE + entry.getValue().kills);
            }
            if (entry.getValue().deaths > 0) {
                scores.put(participant, ChatColor.DARK_GRAY + " ➥ " + ChatColor.YELLOW + "Deaths: " + ChatColor.WHITE + entry.getValue().deaths);
            }
            if (entry.getValue().denies > 0) {
                scores.put(participant, ChatColor.DARK_GRAY + " ➥ " + ChatColor.YELLOW + "Denies: " + ChatColor.WHITE + entry.getValue().denies);
            }
            if (entry.getValue().assists > 0) {
                scores.put(participant, ChatColor.DARK_GRAY + " ➥ " + ChatColor.YELLOW + "Assists: " + ChatColor.WHITE + entry.getValue().assists);
            }
            if (entry.getValue().teamKills > 0) {
                scores.put(participant, ChatColor.DARK_GRAY + " ➥ " + ChatColor.RED + "Team kills: " + ChatColor.WHITE + entry.getValue().teamKills);
            }
        }
        return scores;
    }

    public Multimap<Faction, String> getFactionScores() {
        Multimap<Faction, String> scores = Multimaps.newListMultimap(new ConcurrentHashMap<>(), ArrayList::new);
        Map<Faction, BattleMetaData> factionScores = new HashMap<>();
        for (Map.Entry<String, BattleMetaData> entry : battle.getInvolved().entrySet()) {
            Participant participant = DGGame.PLAYER_R.fromId(entry.getKey());
            Faction participantFaction = participant.getFaction();
            BattleMetaData participantData = entry.getValue();
            BattleMetaData factionData = factionScores.getOrDefault(participantFaction, new BattleMetaData());
            factionData.hits += participantData.hits;
            factionData.kills += participantData.kills;
            factionData.deaths += participantData.deaths;
            factionData.denies += participantData.denies;
            factionData.assists += participantData.assists;
            factionData.teamKills += participantData.teamKills;
            factionScores.put(participantFaction, factionData);
        }
        for (Map.Entry<Faction, BattleMetaData> entry : factionScores.entrySet()) {
            scores.put(entry.getKey(), ChatColor.DARK_GRAY + " ➥ " + ChatColor.YELLOW + "Hits: " + ChatColor.WHITE + entry.getValue().hits);
            scores.put(entry.getKey(), ChatColor.DARK_GRAY + " ➥ " + ChatColor.YELLOW + "Kills: " + ChatColor.WHITE + entry.getValue().kills);
            scores.put(entry.getKey(), ChatColor.DARK_GRAY + " ➥ " + ChatColor.YELLOW + "Deaths: " + ChatColor.WHITE + entry.getValue().deaths);
            scores.put(entry.getKey(), ChatColor.DARK_GRAY + " ➥ " + ChatColor.YELLOW + "Denies: " + ChatColor.WHITE + entry.getValue().denies);
            scores.put(entry.getKey(), ChatColor.DARK_GRAY + " ➥ " + ChatColor.YELLOW + "Assists: " + ChatColor.WHITE + entry.getValue().assists);
        }
        return scores;
    }

    public List<Participant> getMVPs() {
        final double max = Collections.max(getWeightedKillDeathRatios().values());
        return getWeightedKillDeathRatios().entrySet().stream().filter(entry -> entry.getValue() == max).map(Map.Entry::getKey).collect(Collectors.toList());
    }
}
