package com.demigodsrpg.game.battle;

import com.demigodsrpg.game.deity.Faction;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public interface Participant {
    boolean getCanPvp();

    EntityType getEntityType();

    Location getLocation();

    Faction getFaction();

    boolean reward(BattleMetaData data);
}
