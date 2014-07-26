package com.demigodsrpg.demigods.classic.battle;

import com.demigodsrpg.demigods.classic.deity.IDeity;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public interface Participant {
    boolean getCanPvp();

    EntityType getEntityType();

    Location getLocation();

    IDeity.Alliance getAlliance();

    boolean reward(Battle.Data data);
}
