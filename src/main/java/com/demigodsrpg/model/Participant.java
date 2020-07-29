package com.demigodsrpg.model;

import com.demigodsrpg.battle.BattleMetaData;
import com.demigodsrpg.family.Family;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.Optional;

public interface Participant {
    String getLastKnownName();

    String getKey();

    boolean getCanPvp();

    EntityType getEntityType();

    Optional<Location> getLocation();

    Family getFamily();

    void addTeamKill();

    boolean reward(BattleMetaData data);
}
