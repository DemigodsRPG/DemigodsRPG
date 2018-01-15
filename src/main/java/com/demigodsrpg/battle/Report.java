package com.demigodsrpg.battle;

import com.demigodsrpg.DGData;
import com.demigodsrpg.family.Family;
import com.demigodsrpg.model.Participant;
import com.demigodsrpg.model.PlayerModel;
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
    private Multimap<Family, String> familyScores;
    private Map<Participant, Double> weightedKillDeathRatios;
    private Map<Family, Double> weightedFamilyRatios;

    public Report(Battle battle) {
        battleLength = battle.getEndTimeMillis() - battle.getStartTimeMillis();
        this.battle = battle;

        // Get the stats together
        participantScores = getParticipantScores();
        familyScores = getFamilyScores();
        weightedKillDeathRatios = getWeightedKillDeathRatios();
        weightedFamilyRatios = getWeightedFamilyRatios();
    }

    public void sendToServer() {
        Multimap<Participant, String> scores = getParticipantScores();
        List<Participant> participants = new ArrayList<>(scores.keySet());
        if (participants.size() == 2) {
            double delta =
                    getWeightedKillDeathRatio(participants.get(0)) - getWeightedKillDeathRatio(participants.get(1));
            delta = delta < 0 ? delta * -1 : delta;
            if (delta > 0.8) {
                Participant one = participants.get(0);
                Participant two = participants.get(1);
                Bukkit.broadcastMessage(one.getFamily().getColor() + one.getLastKnownName() + ChatColor.GRAY + " and " +
                        two.getFamily().getColor() + two.getLastKnownName() + ChatColor.GRAY + " just tied in a duel.");
            } else {
                int winnerIndex = getWeightedKillDeathRatio(participants.get(0)) >
                        getWeightedKillDeathRatio(participants.get(1)) ? 0 : 1;
                Participant winner = participants.get(winnerIndex);
                Participant loser = participants.get(winnerIndex == 0 ? 1 : 0);
                Bukkit.broadcastMessage(winner.getFamily().getColor() + winner.getLastKnownName() + ChatColor.GRAY +
                        " just won in a duel against " + loser.getFamily().getColor() + loser.getLastKnownName() +
                        ChatColor.GRAY + ".");
            }
        } else if (participants.size() > 2) {
            double winningScore = 0;
            Family winningfamily = null;
            List<Participant> MVPs = getMVPs();
            boolean oneMVP = MVPs.size() == 1;
            for (Map.Entry<Family, Double> entry : weightedFamilyRatios.entrySet()) {
                double score = entry.getValue();
                if (score > winningScore) {
                    winningfamily = entry.getKey();
                    winningScore = score;
                }
            }
            if (winningfamily != null) {
                Bukkit.broadcastMessage(
                        ChatColor.GRAY + "The " + ChatColor.YELLOW + winningfamily.getName() + " family " +
                                ChatColor.GRAY + "just won a battle involving " + participants.size() +
                                " participants.");
                Bukkit.broadcastMessage(
                        ChatColor.GRAY + "The " + ChatColor.YELLOW + "MVP" + (oneMVP ? "" : "s") + ChatColor.GRAY +
                                " from this battle " + (oneMVP ? "is" : "are") + ":");
                for (Participant mvp : MVPs) {
                    BattleMetaData data = battle.getInvolved().get(mvp.getKey());
                    Bukkit.broadcastMessage(
                            ChatColor.DARK_GRAY + " ➥ " + mvp.getFamily().getColor() + mvp.getLastKnownName() +
                                    ChatColor.GRAY + " / " + ChatColor.YELLOW + "Kills" + ChatColor.GRAY + ": " +
                                    data.getKills() + " / " + ChatColor.YELLOW + "Deaths" + ChatColor.GRAY + ": " +
                                    data.getDeaths());
                }
            }
        }
    }

    public void sendToFamilies() {
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
        BattleMetaData data = battle.getInvolved().get(participant.getKey());
        if (data.deaths == 0) {
            return data.getKills() * 1.2;
        }
        return (data.getKills() + data.getDeaths()) / data.deaths;
    }

    public Map<Participant, Double> getWeightedKillDeathRatios() {
        Map<Participant, Double> weightedRatios = new HashMap<>();
        for (Map.Entry<String, BattleMetaData> entry : battle.getInvolved().entrySet()) {
            Participant participant = DGData.PLAYER_R.fromId(entry.getKey());
            weightedRatios.put(participant, getWeightedKillDeathRatio(participant));
        }
        return weightedRatios;
    }

    public Map<Family, Double> getWeightedFamilyRatios() {
        Map<Family, Double> weightedRatios = new HashMap<>();
        for (Map.Entry<Participant, Double> entry : weightedKillDeathRatios.entrySet()) {
            Family participantFamily = entry.getKey().getFamily();
            double score = weightedRatios.getOrDefault(participantFamily, 0.0);
            score += entry.getValue();
            weightedRatios.put(participantFamily, score);
        }
        return weightedRatios;
    }

    public Multimap<Participant, String> getParticipantScores() {
        Multimap<Participant, String> scores = Multimaps.newListMultimap(new ConcurrentHashMap<>(), ArrayList::new);
        for (Map.Entry<String, BattleMetaData> entry : battle.getInvolved().entrySet()) {
            Participant participant = DGData.PLAYER_R.fromId(entry.getKey());
            if (entry.getValue().hits > 0) {
                scores.put(participant, ChatColor.DARK_GRAY + " ➥ " + ChatColor.YELLOW + "Hits: " + ChatColor.WHITE +
                        entry.getValue().hits);
            }
            if (entry.getValue().kills > 0) {
                scores.put(participant, ChatColor.DARK_GRAY + " ➥ " + ChatColor.YELLOW + "Kills: " + ChatColor.WHITE +
                        entry.getValue().kills);
            }
            if (entry.getValue().deaths > 0) {
                scores.put(participant, ChatColor.DARK_GRAY + " ➥ " + ChatColor.YELLOW + "Deaths: " + ChatColor.WHITE +
                        entry.getValue().deaths);
            }
            if (entry.getValue().denies > 0) {
                scores.put(participant, ChatColor.DARK_GRAY + " ➥ " + ChatColor.YELLOW + "Denies: " + ChatColor.WHITE +
                        entry.getValue().denies);
            }
            if (entry.getValue().assists > 0) {
                scores.put(participant, ChatColor.DARK_GRAY + " ➥ " + ChatColor.YELLOW + "Assists: " + ChatColor.WHITE +
                        entry.getValue().assists);
            }
            if (entry.getValue().teamKills > 0) {
                scores.put(participant, ChatColor.DARK_GRAY + " ➥ " + ChatColor.RED + "Team kills: " + ChatColor.WHITE +
                        entry.getValue().teamKills);
            }
        }
        return scores;
    }

    public Multimap<Family, String> getFamilyScores() {
        Multimap<Family, String> scores = Multimaps.newListMultimap(new ConcurrentHashMap<>(), ArrayList::new);
        Map<Family, BattleMetaData> familyScores = new HashMap<>();
        for (Map.Entry<String, BattleMetaData> entry : battle.getInvolved().entrySet()) {
            Participant participant = DGData.PLAYER_R.fromId(entry.getKey());
            Family participantFamily = participant.getFamily();
            BattleMetaData participantData = entry.getValue();
            BattleMetaData familyData = familyScores.getOrDefault(participantFamily, new BattleMetaData());
            familyData.hits += participantData.hits;
            familyData.kills += participantData.kills;
            familyData.deaths += participantData.deaths;
            familyData.denies += participantData.denies;
            familyData.assists += participantData.assists;
            familyData.teamKills += participantData.teamKills;
            familyScores.put(participantFamily, familyData);
        }
        for (Map.Entry<Family, BattleMetaData> entry : familyScores.entrySet()) {
            scores.put(entry.getKey(), ChatColor.DARK_GRAY + " ➥ " + ChatColor.YELLOW + "Hits: " + ChatColor.WHITE +
                    entry.getValue().hits);
            scores.put(entry.getKey(), ChatColor.DARK_GRAY + " ➥ " + ChatColor.YELLOW + "Kills: " + ChatColor.WHITE +
                    entry.getValue().kills);
            scores.put(entry.getKey(), ChatColor.DARK_GRAY + " ➥ " + ChatColor.YELLOW + "Deaths: " + ChatColor.WHITE +
                    entry.getValue().deaths);
            scores.put(entry.getKey(), ChatColor.DARK_GRAY + " ➥ " + ChatColor.YELLOW + "Denies: " + ChatColor.WHITE +
                    entry.getValue().denies);
            scores.put(entry.getKey(), ChatColor.DARK_GRAY + " ➥ " + ChatColor.YELLOW + "Assists: " + ChatColor.WHITE +
                    entry.getValue().assists);
        }
        return scores;
    }

    public List<Participant> getMVPs() {
        final double max = Collections.max(getWeightedKillDeathRatios().values());
        return getWeightedKillDeathRatios().entrySet().stream().filter(entry -> entry.getValue() == max)
                .map(Map.Entry::getKey).collect(Collectors.toList());
    }
}
