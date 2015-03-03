package com.demigodsrpg.game.util;

import com.demigodsrpg.game.DGGame;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class LocationUtil {
    public static String stringFromLocation(Location location) {
        return ((World) location.getExtent()).getName() + ";" + location.getPosition().getX() + ";" + location.getPosition().getY() + ";" + location.getPosition().getZ() /* + ";" + location.getYaw() + ";" + location.getPitch() */;
    }

    public static Location locationFromString(String location) {
        String[] part = location.split(";");
        if (DGGame.SERVER.getWorld(part[0]).isPresent()) {
            return new Location(DGGame.SERVER.getWorld(part[0]).get(), new Vector3d(Double.parseDouble(part[1]), Double.parseDouble(part[2]), Double.parseDouble(part[3]) /*, Float.parseFloat(part[4]), Float.parseFloat(part[4])) */));
        }
        return null;
    }
}
