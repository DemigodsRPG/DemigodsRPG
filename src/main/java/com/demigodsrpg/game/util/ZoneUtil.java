package com.demigodsrpg.game.util;

import com.demigodsrpg.game.DGGame;
import com.google.common.collect.Sets;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Set;

public class ZoneUtil {
    private static final Set<String> ENABLED_WORLDS = Sets.newHashSet();
    // private static final Configuration PLUGIN_CONFIG = DGGame.getInst().getConfig();

    public static int init() {
        // Load disabled worlds
        Set<String> enabledWorlds = Sets.newHashSet();
        int erroredWorlds = 0;
        for (World world : DGGame.SERVER.getWorlds() /* PLUGIN_CONFIG.getStringList("active_worlds") */) {
            enabledWorlds.add(world.getName());
            // erroredWorlds += Bukkit.getServer().getWorld(world) == null ? 1 : 0;
        }
        ENABLED_WORLDS.addAll(enabledWorlds);

        return erroredWorlds;
    }

    /**
     * Returns true if <code>location</code> is within a no-PVP zone.
     *
     * @param location the location to check.
     * @return true/false depending on if it's a no-PVP zone or not.
     */
    public static boolean inNoPvpZone(Location location) {
        return false /* !PLUGIN_CONFIG.getBoolean("zones.allow_skills_anywhere") && WorldGuardUtil.worldGuardEnabled() && !WorldGuardUtil.canPVP(location) */;
    }

    public static boolean inNoDGZone(Location location) {
        return false /* isNoDGWorld(location.getWorld()) */;
    }

    public static boolean isNoDGWorld(World world) {
        return !ENABLED_WORLDS.contains(world.getName());
    }

    public static void enableWorld(String worldName) {
        ENABLED_WORLDS.add(worldName);
    }

    public static void disableWorld(String worldName) {
        ENABLED_WORLDS.remove(worldName);
    }
}
