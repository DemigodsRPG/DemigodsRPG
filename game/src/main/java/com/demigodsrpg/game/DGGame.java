/*
 * Copyright 2015 Demigods RPG
 * Copyright 2015 Alexander Chauncey
 * Copyright 2015 Alex Bennett
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demigodsrpg.game;

import com.demigodsrpg.chitchat.Chitchat;
import com.demigodsrpg.data.DGData;
import com.demigodsrpg.data.Demo;
import com.demigodsrpg.data.Setting;
import com.demigodsrpg.data.deity.Faction;
import com.demigodsrpg.data.model.PlayerModel;
import com.demigodsrpg.data.model.ShrineModel;
import com.demigodsrpg.data.model.TributeModel;
import com.demigodsrpg.data.registry.config.AreaRegistry;
import com.demigodsrpg.game.command.*;
import com.demigodsrpg.game.command.admin.*;
import com.demigodsrpg.game.integration.chitchat.FactionChatTag;
import com.demigodsrpg.game.integration.chitchat.FactionIdTag;
import com.demigodsrpg.game.listener.*;
import com.demigodsrpg.util.ZoneUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;
import java.util.stream.Collectors;

public class DGGame {
    // -- PLUGIN RELATED CONSTANTS -- //

    private static DGGame INST;
    private static DGBukkitPlugin PLUGIN;

    // -- ENABLE/DISABLE -- //

    public DGGame(DGBukkitPlugin plugin) {
        // Define the instances
        INST = this;
        PLUGIN = plugin;
        DGData.PLUGIN = plugin;

        // Define the console
        DGData.CONSOLE = plugin.getLogger();

        // Define the save path
        DGData.SAVE_PATH = plugin.getDataFolder().getPath() + "/data/";

        // Get custom factions and deities
        DGData.FACTION_R.registerFromDatabase();
        DGData.DEITY_R.registerFromDatabase();

        // Register default factions
        DGData.FACTION_R.register(Faction.NEUTRAL);
        DGData.FACTION_R.register(Faction.EXCOMMUNICATED);

        // Debug data
        if (Setting.DEBUG_DATA) {
            DGData.CONSOLE.info("Enabling demo mode.");

            // Debug deities
            DGData.CONSOLE.info("Enabling demo deities.");
            DGData.DEITY_R.register(Demo.D.LOREM);
            DGData.DEITY_R.register(Demo.D.IPSUM);
            DGData.DEITY_R.register(Demo.D.DOLOR);
            DGData.DEITY_R.register(Demo.D.SIT);
            DGData.DEITY_R.register(Demo.D.AMET);

            // Debug factions
            DGData.CONSOLE.info("Enabling demo factions.");
            DGData.FACTION_R.register(Demo.F.KŌHAI);
            DGData.FACTION_R.register(Demo.F.SENPAI);
            DGData.FACTION_R.register(Demo.F.SENSEI);

            // Rebuild shrine warps
            DGData.CONSOLE.info("Rebuilding shrine warps.");
            DGData.PLAYER_R.getRegistered().forEach(model -> {
                List<ShrineModel> ownedShrines = DGData.SHRINE_R.getRegistered().stream().
                        filter(shrine -> model.getMojangId().equals(shrine.getOwnerMojangId())).collect(Collectors.toList());
                ownedShrines.forEach(shrine -> {
                    if (!model.getShrineWarps().contains(shrine.getPersistentId())) {
                        model.addShrineWarp(shrine);
                    }
                });
            });
        }

        // Determine territory registries
        for (World world : Bukkit.getWorlds()) {
            AreaRegistry area_r = new AreaRegistry(world);
            area_r.registerFromDatabase();
            DGData.AREA_R.put(world.getName(), new AreaRegistry(world));
        }

        // Register the abilities
        DGData.ABILITY_R.registerAbilities();

        // Regen shrines
        DGData.SHRINE_R.generate();

        // Fill up tribute data
        if (DGData.TRIBUTE_R.getRegistered().isEmpty()) {
            DGData.TRIBUTE_R.initializeTributeTracking();
        }

        // Start the threads
        startThreads(plugin);

        // Register the listeners
        PluginManager manager = plugin.getServer().getPluginManager();
        manager.registerEvents(new InventoryListener(), plugin);
        manager.registerEvents(new PlayerListener(), plugin);
        manager.registerEvents(new BattleListener(), plugin);
        manager.registerEvents(new ShrineListener(), plugin);
        manager.registerEvents(new TributeListener(), plugin);
        manager.registerEvents(new AreaListener(), plugin);
        manager.registerEvents(new AbilityListener(), plugin);
        manager.registerEvents(DGData.ABILITY_R, plugin);


        // Register commands
        plugin.getCommand("faction").setExecutor(new FactionCommand());
        plugin.getCommand("binds").setExecutor(new BindsCommand());
        plugin.getCommand("check").setExecutor(new CheckCommand());
        plugin.getCommand("aspect").setExecutor(new AspectCommand());
        plugin.getCommand("cleanse").setExecutor(new CleanseCommand());
        plugin.getCommand("shrine").setExecutor(new ShrineCommand());
        plugin.getCommand("values").setExecutor(new ValuesCommand());

        // Admin commands
        plugin.getCommand("adminmode").setExecutor(new AdminModeComand());
        plugin.getCommand("selectarea").setExecutor(new SelectAreaCommand());
        plugin.getCommand("createfaction").setExecutor(new CreateFactionCommand());
        plugin.getCommand("createfactionarea").setExecutor(new CreateFactionAreaCommand());
        plugin.getCommand("checkplayer").setExecutor(new CheckPlayerCommand());
        plugin.getCommand("adddevotion").setExecutor(new AddDevotionCommand());
        plugin.getCommand("removedevotion").setExecutor(new RemoveDevotionCommand());
        plugin.getCommand("giveaspect").setExecutor(new GiveAspectCommand());
        plugin.getCommand("removeaspect").setExecutor(new RemoveAspectCommand());
        plugin.getCommand("setfaction").setExecutor(new SetFactionCommand());

        // Enable ZoneUtil
        ZoneUtil.init(plugin);

        // Handle Chitchat integration
        if (manager.isPluginEnabled("Chitchat")) {
            Chitchat.getChatFormat().add(new FactionChatTag());
            Chitchat.getChatFormat().add(new FactionIdTag());
        }

        // Let the console know
        DGData.CONSOLE.info("Enabled and ready for battle.");
    }

    public void onDisable(DGBukkitPlugin plugin) {
        // Ensure that we unregister our commands and tasks
        HandlerList.unregisterAll(plugin);
        Bukkit.getScheduler().cancelTasks(plugin);

        // Clear the cache.
        DGData.clearCache();

        // Let the console know
        DGData.CONSOLE.info("Disabled successfully.");
    }

    // -- TASK RELATED -- //

    private Runnable SYNC, ASYNC, FIRE_SPREAD, BATTLE, VALUE;

    @SuppressWarnings("deprecation")
    void startThreads(DGBukkitPlugin plugin) {
        BukkitScheduler scheduler = Bukkit.getScheduler();

        // Define the runnables
        SYNC = () -> {
            // Update online players
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (ZoneUtil.inNoDGZone(player.getLocation())) continue;
                PlayerModel model = DGData.PLAYER_R.fromPlayer(player);
                if (model != null) {
                    model.updateCanPvp();
                }
            }
        };
        ASYNC = DGData.SERVER_R::clearExpired;
        FIRE_SPREAD = () -> {
            for (World world : Bukkit.getWorlds()) {
                world.getLivingEntities().stream().filter(entity -> entity.getFireTicks() > 0).forEach(entity ->
                                entity.getNearbyEntities(0.5, 0.5, 0.5).stream().filter(nearby -> nearby instanceof LivingEntity && !nearby.equals(entity)).forEach(nearby -> nearby.setFireTicks(100))
                );
            }
        };
        BATTLE = DGData.BATTLE_R::endExpired;
        VALUE = new TributeModel.ValueTask();

        // Start sync demigods runnable
        scheduler.scheduleSyncRepeatingTask(plugin, SYNC, 20, 20);
        DGData.CONSOLE.info("Main Demigods SYNC thread enabled...");

        // Start async demigods runnable
        scheduler.scheduleAsyncRepeatingTask(plugin, ASYNC, 20, 20);
        DGData.CONSOLE.info("Main Demigods ASYNC thread enabled...");

        // Start sync fire runnable
        scheduler.scheduleSyncRepeatingTask(plugin, FIRE_SPREAD, 3, 20);
        DGData.CONSOLE.info("Demigods FIRE SPREAD task enabled...");

        // Start sync fire runnable
        scheduler.scheduleSyncRepeatingTask(plugin, BATTLE, 3, 20);
        DGData.CONSOLE.info("Demigods BATTLE task enabled...");

        // Start async value runnable
        scheduler.scheduleAsyncRepeatingTask(plugin, VALUE, 60, 400);
        DGData.CONSOLE.info("Demigods VALUE task enabled...");
    }

    public static DGGame getInst() {
        return INST;
    }

    public static DGBukkitPlugin getPlugin() {
        return PLUGIN;
    }
}
