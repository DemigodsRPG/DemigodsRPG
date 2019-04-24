package com.demigodsrpg.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.bukkit.protection.events.DisallowedPVPEvent;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.*;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import java.util.Iterator;
import java.util.concurrent.ConcurrentMap;

/**
 * Custom flags will not require reflection in WorldGuard 6+, until then we'll use it.
 */
public class WorldGuardUtil implements Listener {
    private static boolean ENABLED;
    private static ConcurrentMap<String, ProtoPVPListener> protoPVPListeners = Maps.newConcurrentMap();

    public WorldGuardUtil(final Plugin plugin) {
        final WorldGuardUtil th = this;
        try {
            ENABLED = Bukkit.getPluginManager().getPlugin("WorldGuard") instanceof WorldGuardPlugin;
        } catch (Exception error) {
            ENABLED = false;
        }

        if (plugin.isEnabled()) {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    Bukkit.getPluginManager().registerEvents(th, plugin);
                }
            }, 40);
        }
        if (plugin.isEnabled()) {
            Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    // process proto-listeners
                    Iterator<ProtoPVPListener> protoPVPListenerIterator = protoPVPListeners.values().iterator();
                    while (worldGuardEnabled() && protoPVPListenerIterator.hasNext()) {
                        ProtoPVPListener queued = protoPVPListenerIterator.next();
                        queued.register();
                        protoPVPListeners.remove(queued.plugin.getName());
                    }
                }
            }, 0, 5);
        }
    }

    /**
     * @return WorldGuard is enabled.
     */
    public static boolean worldGuardEnabled() {
        return ENABLED;
    }

    /**
     * @param id The name of a WorldGuard flag.
     * @deprecated If you don't have WorldGuard installed this will error.
     */
    @Deprecated
    public static Flag<?> getFlag(String id) {
        return Flags.get(id);
    }

    /**
     * Check that a ProtectedRegion exists at a Location.
     *
     * @param name     The name of the region.
     * @param location The location being checked.
     * @return The region does exist at the provided location.
     */
    public static boolean checkForRegion(final String name, Location location) {
        return Iterators
                .any(WorldGuard.getInstance().getPlatform().getRegionContainer()
                        .get(BukkitAdapter.adapt(location.getWorld()))
                        .getApplicableRegions(BukkitAdapter.asBlockVector(location))
                        .iterator(), new Predicate<ProtectedRegion>() {
                    @Override
                    public boolean apply(ProtectedRegion region) {
                        return region.getId().toLowerCase().contains(name);
                    }
                });
    }

    /**
     * Check for a flag at a given location.
     *
     * @param flag     The flag being checked.
     * @param location The location being checked.
     * @return The flag does exist at the provided location.
     */
    public static boolean checkForFlag(final Flag flag, Location location) {
        return Iterators
                .any(WorldGuard.getInstance().getPlatform().getRegionContainer()
                        .get(BukkitAdapter.adapt(location.getWorld()))
                        .getApplicableRegions(BukkitAdapter.asBlockVector(location))
                        .iterator(), new Predicate<ProtectedRegion>() {
                    @Override
                    public boolean apply(ProtectedRegion region) {
                        try {
                            return region.getFlags().containsKey(flag);
                        } catch (Exception ignored) {
                        }
                        return false;
                    }
                });
    }

    /**
     * Check if a StateFlag is enabled at a given location.
     *
     * @param flag     The flag being checked.
     * @param location The location being checked.
     * @return The flag is enabled.
     */
    public static boolean checkStateFlagAllows(final StateFlag flag, Location location, RegionAssociable associable) {
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        return query.testState(BukkitAdapter.adapt(location), associable, flag);
    }

    /**
     * Check if a StateFlag is enabled at a given location.
     *
     * @param flag     The flag being checked.
     * @param location The location being checked.
     * @return The flag is enabled.
     */
    public static boolean checkStateFlagAllows(final StateFlag flag, Location location, Player player) {
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        return query.testState(BukkitAdapter.adapt(location), WorldGuardPlugin.inst().wrapPlayer(player), flag);
    }

    /**
     * Check for a flag-value at a given location.
     *
     * @param flag     The flag being checked.
     * @param value    The value (marshalled) as a String.
     * @param location The location being checked.
     * @return The flag-value does exist at the provided location.
     */
    public static boolean checkForFlagValue(final Flag flag, final String value, Location location) {
        return Iterators
                .any(WorldGuard.getInstance().getPlatform().getRegionContainer()
                        .get(BukkitAdapter.adapt(location.getWorld()))
                        .getApplicableRegions(BukkitAdapter.asBlockVector(location))
                        .iterator(), new Predicate<ProtectedRegion>() {
                    @Override
                    public boolean apply(ProtectedRegion region) {
                        try {
                            return flag.marshal(region.getFlag(flag)).equals(value);
                        } catch (Exception ignored) {
                        }
                        return false;
                    }
                });
    }

    /**
     * @param player   Given player.
     * @param location Given location.
     * @return The player can build here.
     */
    public static boolean canBuild(Player player, Location location) {
        return checkStateFlagAllows(Flags.BUILD, location, player);
    }

    /**
     * @param location Given location.
     * @return PVP is allowed here.
     */
    public static boolean canPVP(Player player, Location location) {
        return checkStateFlagAllows(Flags.PVP, location, player);
    }

    public static void setWhenToOverridePVP(Plugin plugin, Predicate<Event> checkPVP) {
        if (!worldGuardEnabled()) {
            protoPVPListeners.put(plugin.getName(), new ProtoPVPListener(plugin, checkPVP));
        } else { new WorldGuardPVPListener(plugin, checkPVP); }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void onPluginEnable(PluginEnableEvent event) {
        if (ENABLED || !event.getPlugin().getName().equals("WorldGuard")) return;
        try {
            ENABLED = event.getPlugin() instanceof WorldGuardPlugin;
        } catch (Exception ignored) {
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void onPluginDisable(PluginDisableEvent event) {
        if (!ENABLED || event.getPlugin().getName().equals("WorldGuard")) return;
        try {
            ENABLED = false;
        } catch (Exception ignored) {
        }
    }

    static class ProtoPVPListener {
        private Plugin plugin;
        private Predicate<Event> checkPVP;

        ProtoPVPListener(Plugin plugin, Predicate<Event> checkPVP) {
            this.plugin = plugin;
            this.checkPVP = checkPVP;
        }

        void register() {
            new WorldGuardPVPListener(plugin, checkPVP);
        }
    }

    public static class WorldGuardPVPListener implements Listener {
        private Predicate<Event> checkPVP;

        WorldGuardPVPListener(Plugin plugin, Predicate<Event> checkPVP) {
            this.checkPVP = checkPVP;
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }

        @EventHandler(priority = EventPriority.LOWEST)
        void onDisallowedPVP(DisallowedPVPEvent event) {
            if (checkPVP.apply(event.getCause())) event.setCancelled(true);
        }
    }
}
