package com.demigodsrpg.demigods.classic.shrine;

import org.bukkit.Location;

import java.util.Collection;

public interface IShrine {
    void generate(Location reference);

    Location getCenter(Location reference);

    int getGroundRadius();

    Collection<Location> getLocations(Location reference);
}
