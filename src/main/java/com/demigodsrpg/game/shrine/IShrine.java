package com.demigodsrpg.game.shrine;

import org.bukkit.Location;

import java.util.Collection;

interface IShrine {
    void generate(Location reference);

    Location getClickable(Location reference);

    Location getSafeTeleport(Location reference);

    int getGroundRadius();

    Collection<Location> getLocations(Location reference);
}
