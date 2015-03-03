package com.demigodsrpg.game.battle;

import com.demigodsrpg.game.deity.Faction;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.world.Location;

public interface Participant {
    boolean getCanPvp();

    EntityType getEntityType();

    Location getLocation();

    Faction getFaction();

    boolean reward(BattleMetaData data);
}
