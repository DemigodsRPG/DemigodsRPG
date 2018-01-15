package com.demigodsrpg.util;

import com.google.common.collect.Sets;
import org.bukkit.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class ZoneUtil {
    private static final Set<String> ENABLED_WORLDS = Sets.newHashSet();
    private static Configuration PLUGIN_CONFIG;

    public static int init(Plugin plugin) {
        // Get the plugin config
        PLUGIN_CONFIG = plugin.getConfig();

        // Load disabled worlds
        Set<String> enabledWorlds = Sets.newHashSet();
        int erroredWorlds = 0;
        for (String world : PLUGIN_CONFIG.getStringList("active_worlds")) {
            enabledWorlds.add(world);
            erroredWorlds += Bukkit.getServer().getWorld(world) == null ? 1 : 0;
        }
        ENABLED_WORLDS.addAll(enabledWorlds);

        // Init WorldGuard stuff
        WorldGuardUtil.setWhenToOverridePVP(plugin, event -> event instanceof EntityDamageByEntityEvent &&
                !inNoDGZone(((EntityDamageByEntityEvent) event).getEntity().getLocation()));

        return erroredWorlds;
    }

    /**
     * Returns true if <code>location</code> is within a no-PVP zone.
     *
     * @param location the location to check.
     * @return true/false depending on if it's a no-PVP zone or not.
     */
    public static boolean inNoPvpZone(Location location) {
        return !PLUGIN_CONFIG.getBoolean("zones.allow_skills_anywhere") && WorldGuardUtil.worldGuardEnabled() &&
                !WorldGuardUtil.canPVP(location);
    }

    public static boolean inNoDGZone(Location location) {
        return isNoDGWorld(location.getWorld());
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
