package com.demigodsrpg.game.battle;

import com.demigodsrpg.game.deity.IDeity;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public interface Participant {
    boolean getCanPvp();

    EntityType getEntityType();

    Location getLocation();

    IDeity.Alliance getAlliance();

    boolean reward(BattleMetaData data);
}
