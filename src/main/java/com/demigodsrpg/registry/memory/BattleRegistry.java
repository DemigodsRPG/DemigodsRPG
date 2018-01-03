package com.demigodsrpg.registry.memory;

import com.demigodsrpg.DGData;
import com.demigodsrpg.Setting;
import com.demigodsrpg.battle.Battle;
import com.demigodsrpg.battle.Report;
import com.demigodsrpg.model.Participant;
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
            report.sendToFamilies();
            report.sendToInvolved();
        });
    }
}
