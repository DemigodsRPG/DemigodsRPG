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
import com.demigodsrpg.data.deity.Faction;
import com.demigodsrpg.data.model.PlayerModel;
import com.demigodsrpg.data.model.ShrineModel;
import com.demigodsrpg.data.model.TributeModel;
import com.demigodsrpg.data.registry.AreaRegistry;
import com.demigodsrpg.game.command.*;
import com.demigodsrpg.game.command.admin.*;
import com.demigodsrpg.game.integration.chitchat.FactionChatTag;
import com.demigodsrpg.game.integration.chitchat.FactionIdTag;
import com.demigodsrpg.game.listener.*;
import com.demigodsrpg.util.LibraryHandler;
import com.demigodsrpg.util.Setting;
import com.demigodsrpg.util.ZoneUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;
import java.util.stream.Collectors;

public class DGPlugin extends JavaPlugin {
    // -- PLUGIN RELATED CONSTANTS -- //

    private static DGPlugin INST;
    private static LibraryHandler LIBRARIES;

    // -- PLUGIN RELATED INSTANCE METHODS -- //

    @Override
    public void onEnable() {
        // Get and load the libraries
        LIBRARIES = new LibraryHandler(this);

        // Censored Libs
        LIBRARIES.addMavenLibrary(LibraryHandler.DG_MG, Depends.COM_CS, Depends.CS_SCHEMATIC, Depends.CS_VER);
        LIBRARIES.addMavenLibrary(LibraryHandler.DG_MG, Depends.COM_CS, Depends.CS_UTIL, Depends.CS_VER);
        LIBRARIES.addMavenLibrary(LibraryHandler.DG_MG, Depends.COM_CS, Depends.CS_BUKKIT_UTIL, Depends.CS_VER);

        // Demigods RPG Libs
        LIBRARIES.addMavenLibrary(LibraryHandler.DG_MG, Depends.COM_DG, Depends.DG_UTIL, Depends.DG_UTIL_VER);
        LIBRARIES.addMavenLibrary(LibraryHandler.DG_MG, Depends.COM_DG, Depends.DG_DATA, Depends.DG_DATA_VER);

        // Define the instance
        INST = this;
        DGData.PLUGIN = this;

        // Define the console
        DGData.CONSOLE = getLogger();

        // Define the save path
        DGData.SAVE_PATH = getDataFolder().getPath() + "/data/";

        // Config
        getConfig().options().copyDefaults(true);
        saveConfig();

        // Get custom factions and deities
        DGData.FACTION_R.registerFromFile();
        DGData.DEITY_R.registerFromFile();

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
            area_r.registerFromFile();
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
        startThreads();

        // Register the listeners
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new InventoryListener(), this);
        manager.registerEvents(new PlayerListener(), this);
        manager.registerEvents(new BattleListener(), this);
        manager.registerEvents(new ShrineListener(), this);
        manager.registerEvents(new TributeListener(), this);
        manager.registerEvents(new AreaListener(), this);
        manager.registerEvents(new AbilityListener(), this);
        manager.registerEvents(DGData.ABILITY_R, this);


        // Register commands
        getCommand("faction").setExecutor(new FactionCommand());
        getCommand("binds").setExecutor(new BindsCommand());
        getCommand("check").setExecutor(new CheckCommand());
        getCommand("aspect").setExecutor(new AspectCommand());
        getCommand("cleanse").setExecutor(new CleanseCommand());
        getCommand("shrine").setExecutor(new ShrineCommand());
        getCommand("values").setExecutor(new ValuesCommand());

        // Admin commands
        getCommand("adminmode").setExecutor(new AdminModeComand());
        getCommand("selectarea").setExecutor(new SelectAreaCommand());
        getCommand("createfaction").setExecutor(new CreateFactionCommand());
        getCommand("createfactionarea").setExecutor(new CreateFactionAreaCommand());
        getCommand("checkplayer").setExecutor(new CheckPlayerCommand());
        getCommand("adddevotion").setExecutor(new AddDevotionCommand());
        getCommand("removedevotion").setExecutor(new RemoveDevotionCommand());
        getCommand("giveaspect").setExecutor(new GiveAspectCommand());
        getCommand("removeaspect").setExecutor(new RemoveAspectCommand());
        getCommand("setfaction").setExecutor(new SetFactionCommand());

        // Enable ZoneUtil
        ZoneUtil.init();

        // Handle Chitchat integration
        if (manager.isPluginEnabled("Chitchat")) {
            Chitchat.getChatFormat().add(new FactionChatTag());
            Chitchat.getChatFormat().add(new FactionIdTag());
        }

        // Let the console know
        DGData.CONSOLE.info("Enabled and ready for battle.");
    }

    @Override
    public void onDisable() {
        // Ensure that we unregister our commands and tasks
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);

        // Clear the cache.
        clearCache();

        // Let the console know
        DGData.CONSOLE.info("Disabled successfully.");
    }

    // -- PLUGIN RELATED UTILITY METHODS -- //

    private static void clearCache() {
        DGData.PLAYER_R.clearCache();
        DGData.SHRINE_R.clearCache();
        DGData.FACTION_R.clearCache();
        DGData.TRIBUTE_R.clearCache();
        DGData.SPAWN_R.clearCache();
        DGData.DEITY_R.clearCache();
        DGData.SERVER_R.clearCache();

        DGData.AREA_R.values().forEach(AreaRegistry::clearCache);
    }

    // -- TASK RELATED -- //

    private static final Runnable SYNC, ASYNC, FIRE_SPREAD, BATTLE, VALUE;

    static {
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
    }

    @SuppressWarnings("deprecation")
    void startThreads() {
        BukkitScheduler scheduler = Bukkit.getScheduler();

        // Start sync demigods runnable
        scheduler.scheduleSyncRepeatingTask(this, SYNC, 20, 20);
        DGData.CONSOLE.info("Main Demigods SYNC thread enabled...");

        // Start async demigods runnable
        scheduler.scheduleAsyncRepeatingTask(this, ASYNC, 20, 20);
        DGData.CONSOLE.info("Main Demigods ASYNC thread enabled...");

        // Start sync fire runnable
        scheduler.scheduleSyncRepeatingTask(this, FIRE_SPREAD, 3, 20);
        DGData.CONSOLE.info("Demigods FIRE SPREAD task enabled...");

        // Start sync fire runnable
        scheduler.scheduleSyncRepeatingTask(this, BATTLE, 3, 20);
        DGData.CONSOLE.info("Demigods BATTLE task enabled...");

        // Start async value runnable
        scheduler.scheduleAsyncRepeatingTask(this, VALUE, 60, 400);
        DGData.CONSOLE.info("Demigods VALUE task enabled...");
    }

    public static DGPlugin getInst() {
        return INST;
    }
}
