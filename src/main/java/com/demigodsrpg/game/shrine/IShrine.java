package com.demigodsrpg.game.shrine;

import com.censoredsoftware.library.schematic.Point;
import org.bukkit.Location;

import java.util.Collection;

interface IShrine {
    void generate(Point reference);

    Location getClickable(Location reference);

    Location getSafeTeleport(Location reference);

    int getGroundRadius();

    Collection<Point> getLocations(Point reference);
}
