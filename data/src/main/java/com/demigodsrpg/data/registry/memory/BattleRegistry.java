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

package com.demigodsrpg.data.registry.memory;

import com.demigodsrpg.data.DGData;
import com.demigodsrpg.data.Setting;
import com.demigodsrpg.data.battle.Battle;
import com.demigodsrpg.data.battle.Report;
import com.demigodsrpg.data.model.Participant;
import com.demigodsrpg.util.ZoneUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BattleRegistry {
    private static ConcurrentMap<String, Battle> BATTLE_MAP = new ConcurrentHashMap<>();

    public boolean isInBattle(Participant participant) {
        return getBattle(participant) != null;
    }

    public boolean canParticipate(Entity entity) {
        return (entity instanceof Player) && defineParticipant(entity).getCanPvp();
    }

    public boolean battleReady(Entity... participants) {
        for (Entity participant : participants) {
            if (!canParticipate(participant)) {
                return false;
            }
        }
        return true;
    }

    public Battle getBattle(Participant participant) {
        for (Battle battle : BATTLE_MAP.values()) {
            if (battle.isInvolved(participant)) {
                return battle;
            }
        }
        return null;
    }

    public Battle getBattle(Participant... participants) {
        Battle battle = null;

        for (Participant participant : participants) {
            if (isInBattle(participant)) {
                Battle found = getBattle(participant);
                if (battle == null) {
                    battle = found;
                } else if (!battle.getId().equals(found.getId())) {
                    battle = mergeBattles(battle, found);
                }
            }
        }

        return battle;
    }

    public Battle mergeBattles(Battle... battles) {
        Battle merged = new Battle();

        Location startLocation = null;
        long startTime = System.currentTimeMillis();

        for (Battle battle : battles) {
            if (battle.getStartTimeMillis() < startTime) {
                startTime = battle.getStartTimeMillis();
                startLocation = battle.getStartLocation();
            }
            merged.getInvolved().putAll(battle.getInvolved());
            unregister(battle);
        }

        merged.setStartTimeMillis(startTime);
        merged.setStartLocation(startLocation);

        register(merged);

        return merged;
    }

    public Participant defineParticipant(Entity entity) {
        if (entity instanceof Player) {
            return DGData.PLAYER_R.fromPlayer((Player) entity);
        }
        return null;
    }

    public boolean canTarget(Entity entity) {
        if (!ZoneUtil.inNoDGZone(entity.getLocation()) && !ZoneUtil.inNoPvpZone(entity.getLocation())) {
            return false;
        }
        Participant participant = defineParticipant(entity);
        return participant == null || participant.getCanPvp();
    }

    public void register(Battle battle) {
        BATTLE_MAP.put(battle.getId(), battle);
    }

    public void unregister(Battle battle) {
        BATTLE_MAP.remove(battle.getId());
    }

    public void endExpired() {
        BATTLE_MAP.values().stream().filter(battle -> System.currentTimeMillis() - battle.getLastInteract() > Setting.BATTLE_INTERVAL_SECONDS * 1000).forEach(battle -> {
            Report report = battle.end();
            report.sendToServer();
            report.sendToFactions();
            report.sendToInvolved();
        });
    }
}
