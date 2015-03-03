package com.demigodsrpg.game.shrine;

import org.spongepowered.api.world.Location;

import java.util.List;

interface IShrine {
    void generate(Location reference);

    Location getClickable(Location reference);

    Location getSafeTeleport(Location reference);

    int getGroundRadius();

    List<Location> getLocations(Location reference);
}
