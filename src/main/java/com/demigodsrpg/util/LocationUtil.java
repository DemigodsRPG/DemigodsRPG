package com.demigodsrpg.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtil {
    public static String stringFromLocation(Location location) {
        return location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ() +
                ";" + location.getYaw() + ";" + location.getPitch();
    }

    public static Location locationFromString(String location) {
        String[] part = location.split(";");
        if (Bukkit.getWorld(part[0]) != null) {
            return new Location(Bukkit.getWorld(part[0]), Double.parseDouble(part[1]), Double.parseDouble(part[2]),
                    Double.parseDouble(part[3]), Float.parseFloat(part[4]), Float.parseFloat(part[4]));
        }
        return null;
    }
}
