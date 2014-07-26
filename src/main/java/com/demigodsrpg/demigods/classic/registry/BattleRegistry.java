package com.demigodsrpg.demigods.classic.registry;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.battle.Participant;
import com.demigodsrpg.demigods.classic.util.ZoneUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class BattleRegistry {
    public boolean isInBattle(Participant participant) {
        return true;
    } // TODO

    public boolean canParticipate(Entity entity) {
        return !(entity instanceof Player) || defineParticipant(entity).getCanPvp();
    }

    public Participant defineParticipant(Entity entity) {
        if(entity instanceof Player) {
            return DGClassic.PLAYER_R.fromPlayer((Player) entity);
        }
        return null;
    }

    public boolean canTarget(Entity entity) {
        if(ZoneUtil.isNoDGCWorld(entity.getWorld())) return false;
        Participant participant = defineParticipant(entity);
        return participant == null || participant.getCanPvp();
    }
}
