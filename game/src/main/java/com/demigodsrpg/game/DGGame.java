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

import com.demigodsrpg.aspect.Aspects;
import com.demigodsrpg.chitchat.Chitchat;
import com.demigodsrpg.data.DGData;
import com.demigodsrpg.data.Demo;
import com.demigodsrpg.data.Setting;
import com.demigodsrpg.data.model.PlayerModel;
import com.demigodsrpg.data.model.TributeModel;
import com.demigodsrpg.data.registry.config.AreaRegistry;
import com.demigodsrpg.enchantment.CustomEnchantments;
import com.demigodsrpg.families.data.Family;
import com.demigodsrpg.game.command.*;
import com.demigodsrpg.game.command.admin.*;
import com.demigodsrpg.game.integration.chitchat.FactionChatTag;
import com.demigodsrpg.game.integration.chitchat.FactionIdTag;
import com.demigodsrpg.game.listener.*;
import com.demigodsrpg.util.ZoneUtil;
import com.iciql.Db;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

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

        // Test for SQL connection if it is enabled
        if (Setting.PSQL_PERSISTENCE) {
            try {
                Class.forName("org.postgresql.Driver");
                Db.open(Setting.PSQL_CONNECTION).close();
                DGData.CONSOLE.info("PSQL database saving enabled.");
            } catch (Exception oops) {
                oops.printStackTrace();
                DGData.CONSOLE.warning("Could not load the PSQL driver.");
                DGData.CONSOLE.warning("Defaulting to json file save.");
                Setting.PSQL_PERSISTENCE = false;
            }
        } else {
            DGData.CONSOLE.info("Json file database saving enabled.");
        }

        // Define the save path
        DGData.SAVE_PATH = plugin.getDataFolder().getPath() + "/data/";

        // Get custom factions and deities
        DGData.FAMILY_R.registerFromDatabase();
        DGData.DEITY_R.registerFromDatabase();

        // Register default factions
        DGData.FAMILY_R.register(Family.NEUTRAL);
        DGData.FAMILY_R.register(Family.EXCOMMUNICATED);

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
            DGData.FAMILY_R.register(Demo.F.KŌHAI);
            DGData.FAMILY_R.register(Demo.F.SENPAI);
            DGData.FAMILY_R.register(Demo.F.SENSEI);
        }

        // Determine territory registries
        for (World world : Bukkit.getWorlds()) {
            AreaRegistry area_r = new AreaRegistry(world);
            area_r.registerFromDatabase();
            DGData.AREA_R.put(world.getName(), new AreaRegistry(world));
        }

        // Register the abilities
        DGData.ABILITY_R.registerAbilities(Aspects.values());

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
        plugin.getCommand("family").setExecutor(new FamilyCommand());
        plugin.getCommand("binds").setExecutor(new BindsCommand());
        plugin.getCommand("check").setExecutor(new CheckCommand());
        plugin.getCommand("aspect").setExecutor(new AspectCommand());
        plugin.getCommand("cleanse").setExecutor(new CleanseCommand());
        plugin.getCommand("shrine").setExecutor(new ShrineCommand());
        plugin.getCommand("values").setExecutor(new ValuesCommand());

        // Admin commands
        plugin.getCommand("adminmode").setExecutor(new AdminModeComand());
        plugin.getCommand("selectarea").setExecutor(new SelectAreaCommand());
        plugin.getCommand("createfamily").setExecutor(new CreateFamilyCommand());
        plugin.getCommand("createfamilyarea").setExecutor(new CreateFamilyAreaCommand());
        plugin.getCommand("checkplayer").setExecutor(new CheckPlayerCommand());
        plugin.getCommand("adddevotion").setExecutor(new AddDevotionCommand());
        plugin.getCommand("removedevotion").setExecutor(new RemoveDevotionCommand());
        plugin.getCommand("giveaspect").setExecutor(new GiveAspectCommand());
        plugin.getCommand("removeaspect").setExecutor(new RemoveAspectCommand());
        plugin.getCommand("setfamilyn").setExecutor(new SetFamilyCommand());

        // Enable ZoneUtil
        ZoneUtil.init(plugin);

        // Handle Chitchat integration
        if (manager.isPluginEnabled("Chitchat")) {
            Chitchat.getChatFormat().add(new FactionChatTag());
            Chitchat.getChatFormat().add(new FactionIdTag());
        }

        // Register custom enchantments
        handleCustomEnchatments();

        // Let the console know
        DGData.CONSOLE.info("     ____            _           _");
        DGData.CONSOLE.info("    |    \\ ___ _____|_|___ ___ _| |___");
        DGData.CONSOLE.info("    |  |  | -_|     | | . | . | . |_ -|");
        DGData.CONSOLE.info("    |____/|___|_|_|_|_|_  |___|___|___|");
        DGData.CONSOLE.info("        Battle of the |___| Chosen");
        DGData.CONSOLE.info("  ");
        DGData.CONSOLE.info(" v. " + DGData.PLUGIN.getDescription().getVersion() + " enabled.");
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

    @SuppressWarnings("unchecked")
    void handleCustomEnchatments() {
        try {
            // Set the server to accept new enchantments
            Field acceptNew = Enchantment.class.getDeclaredField("acceptingNew");
            acceptNew.setAccessible(true);
            acceptNew.set(null, true);

            // Use reflection to get the maps from these fields
            Field byIdField = Enchantment.class.getDeclaredField("byId");
            Field byNameField = Enchantment.class.getDeclaredField("byName");
            byIdField.setAccessible(true);
            byNameField.setAccessible(true);
            Map<Integer, Enchantment> byId = (Map<Integer, Enchantment>) byIdField.get(null);
            Map<String, Enchantment> byName = (Map<String, Enchantment>) byNameField.get(null);

            // Iterate over the custom enchantments
            for (Enchantment enchant : CustomEnchantments.values()) {
                // Remove the enchantments if they are already there
                if (byId.containsKey(enchant.getId())) {
                    byId.remove(enchant.getId());
                }
                if (byName.containsKey(enchant.getName())) {
                    byName.remove(enchant.getName());
                }

                // Add the enchantments
                try {
                    Enchantment.registerEnchantment(enchant);
                } catch (Exception oops) {
                    DGData.CONSOLE.warning("Couldn't register a custom enchantment.");
                    oops.printStackTrace();
                }
            }

            // Set the server to stop accepting new enchantments, just to be safe
            acceptNew.set(null, false);
        } catch (Exception ignored) {
        }
        DGData.CONSOLE.info("Custom enchantments injected.");
    }

    public static DGGame getInst() {
        return INST;
    }

    public static DGBukkitPlugin getPlugin() {
        return PLUGIN;
    }

    // -- DIRTY PRIVATE HELPER METHOD -- //

    private void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
    }
}
