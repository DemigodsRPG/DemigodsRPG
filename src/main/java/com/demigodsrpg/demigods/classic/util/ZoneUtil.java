package com.demigodsrpg.demigods.classic.util;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Set;

public class ZoneUtil {
    private static final Set<String> ENABLED_WORLDS = Sets.newHashSet();
    private static final Configuration PLUGIN_CONFIG = DGClassic.getInst().getConfig();

    public static int init() {
        // Load disabled worlds
        Set<String> enabledWorlds = Sets.newHashSet();
        int erroredWorlds = 0;
        for (String world : PLUGIN_CONFIG.getStringList("active_worlds")) {
            enabledWorlds.add(world);
            erroredWorlds += Bukkit.getServer().getWorld(world) == null ? 1 : 0;
        }
        ENABLED_WORLDS.addAll(enabledWorlds);

        // Init WorldGuard stuff
        WorldGuardUtil.setWhenToOverridePVP(DGClassic.getInst(), new Predicate<Event>() {
            @Override
            public boolean apply(Event event) {
                return event instanceof EntityDamageByEntityEvent && !inNoDGCZone(((EntityDamageByEntityEvent) event).getEntity().getLocation());
            }
        });

        return erroredWorlds;
    }

    /**
     * Returns true if <code>location</code> is within a no-PVP zone.
     *
     * @param location the location to check.
     * @return true/false depending on if it's a no-PVP zone or not.
     */
    public static boolean inNoPvpZone(Location location) {
        return !PLUGIN_CONFIG.getBoolean("zones.allow_skills_anywhere") && WorldGuardUtil.worldGuardEnabled() && !WorldGuardUtil.canPVP(location);
    }

    public static boolean inNoDGCZone(Location location) {
        return isNoDGCWorld(location.getWorld());
    }

    public static boolean isNoDGCWorld(World world) {
        return !ENABLED_WORLDS.contains(world.getName());
    }

    public static void enableWorld(String worldName) {
        ENABLED_WORLDS.add(worldName);
    }

    public static void disableWorld(String worldName) {
        ENABLED_WORLDS.remove(worldName);
    }
}
