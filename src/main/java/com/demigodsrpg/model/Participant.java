package com.demigodsrpg.model;

import com.demigodsrpg.battle.BattleMetaData;
import com.demigodsrpg.family.Family;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public interface Participant {
    String getLastKnownName();

    String getKey();

    boolean getCanPvp();

    EntityType getEntityType();

    Location getLocation();

    Family getFamily();

    void addTeamKill();

    boolean reward(BattleMetaData data);
}
