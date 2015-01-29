package com.demigodsrpg.game.registry;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.battle.Participant;
import com.demigodsrpg.game.util.ZoneUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class BattleRegistry {
    public boolean isInBattle(Participant participant) {
        return true;
    } // TODO

    public boolean canParticipate(Entity entity) {
        return !(entity instanceof Player) || defineParticipant(entity).getCanPvp();
    }

    Participant defineParticipant(Entity entity) {
        if (entity instanceof Player) {
            return DGGame.PLAYER_R.fromPlayer((Player) entity);
        }
        return null;
    }

    public boolean canTarget(Entity entity) {
        if (ZoneUtil.isNoDGCWorld(entity.getWorld())) return false;
        Participant participant = defineParticipant(entity);
        return participant == null || participant.getCanPvp();
    }
}
