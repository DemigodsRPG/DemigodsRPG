package com.demigodsrpg.demigods.classic;

import com.demigodsrpg.demigods.classic.command.*;
import com.demigodsrpg.demigods.classic.command.admin.*;
import com.demigodsrpg.demigods.classic.listener.InventoryListener;
import com.demigodsrpg.demigods.classic.listener.PlayerListener;
import com.demigodsrpg.demigods.classic.listener.ShrineListener;
import com.demigodsrpg.demigods.classic.listener.TributeListener;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import com.demigodsrpg.demigods.classic.model.TributeModel;
import com.demigodsrpg.demigods.classic.registry.*;
import com.demigodsrpg.demigods.classic.util.ZoneUtil;
import com.google.common.base.Supplier;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DGClassic extends JavaPlugin {
    // -- PLUGIN RELATED CONSTANTS -- //

    public static DGClassic INST;
    public static Logger CONSOLE;
    public static String SAVE_PATH;

    // -- REGISTRIES -- //

    public static final PlayerRegistry PLAYER_R = new PlayerRegistry();
    public static final ShrineRegistry SHRINE_R = new ShrineRegistry();
    public static final TributeRegistry TRIBUTE_R = new TributeRegistry();
    public static final SpawnRegistry SPAWN_R = new SpawnRegistry();
    public static final BattleRegistry BATTLE_R = new BattleRegistry();
    public static final AbilityRegistry ABILITY_R = new AbilityRegistry();
    public static final ServerDataRegistry SERV_R = new ServerDataRegistry();
    public static final ConcurrentMap<String, TerritoryRegistry> TERR_R = new ConcurrentHashMap<>();

    // -- TEMP DATA -- //

    public static final Table<String, String, Object> TEMP_DATA = Tables.newCustomTable(new ConcurrentHashMap<String, Map<String, Object>>(), new Supplier<Map<String, Object>>() {
        @Override
        public Map<String, Object> get() {
            return new ConcurrentHashMap<>();
        }
    });

    // -- PLUGIN RELATED INSTANCE METHODS -- //

    @Override
    public void onEnable() {
        // Define the instance
        INST = this;

        // Define the logger
        CONSOLE = getLogger();

        // Config
        getConfig().options().copyDefaults(true);
        saveConfig();

        // Define the save path
        SAVE_PATH = getDataFolder().getPath() + "/data/";

        // Load persistent data
        PLAYER_R.registerFromFile();
        SHRINE_R.registerFromFile();
        TRIBUTE_R.registerFromFile();
        SPAWN_R.registerFromFile();
        SERV_R.registerFromFile();

        // Determine territory registries
        for (World world : Bukkit.getWorlds()) {
            TerritoryRegistry terr_r = new TerritoryRegistry(world);
            terr_r.registerFromFile();
            TERR_R.put(world.getName(), new TerritoryRegistry(world));
        }

        // Register the abilities
        ABILITY_R.registerAbilities();

        // Regen shrines
        SHRINE_R.generate();

        // Fill up tribute data
        if (TRIBUTE_R.getRegistered().isEmpty()) {
            TRIBUTE_R.initializeTributeTracking();
        }

        // Start the threads
        startThreads();

        // Register the listeners
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new InventoryListener(), this);
        manager.registerEvents(new PlayerListener(), this);
        manager.registerEvents(new ShrineListener(), this);
        manager.registerEvents(new TributeListener(), this);
        manager.registerEvents(ABILITY_R, this);

        // Register commands
        getCommand("alliance").setExecutor(new AllianceCommand());
        getCommand("binds").setExecutor(new BindsCommand());
        getCommand("check").setExecutor(new CheckCommand());
        getCommand("shrine").setExecutor(new ShrineCommand());
        getCommand("values").setExecutor(new ValuesCommand());

        // Admin commands
        getCommand("checkplayer").setExecutor(new CheckPlayerCommand());
        getCommand("adddevotion").setExecutor(new AddDevotionCommand());
        getCommand("removedevotion").setExecutor(new RemoveDevotionCommand());
        getCommand("givedeity").setExecutor(new GiveDeityCommand());
        getCommand("removedeity").setExecutor(new RemoveDeityCommand());
        getCommand("setalliance").setExecutor(new SetAllianceCommand());

        // Enable ZoneUtil
        ZoneUtil.init();

        // Let the console know
        CONSOLE.info("Enabled and ready for battle.");
    }

    @Override
    public void onDisable() {
        // Ensure that we unregister our commands and tasks
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);

        // Save the data
        if (!save()) {
            CONSOLE.severe("The vital save data was unable to save correctly!");
            CONSOLE.warning("Disabled with a corrupt save, please use a backup.");
        } else {
            // Let the console know
            CONSOLE.info("Disabled successfully.");
        }
    }

    // -- PLUGIN RELATED UTILITY METHODS -- //

    public static boolean save() {
        boolean noErrors;
        noErrors = PLAYER_R.saveToFile();
        noErrors = SHRINE_R.saveToFile() && noErrors;
        noErrors = TRIBUTE_R.saveToFile() && noErrors;
        noErrors = SPAWN_R.saveToFile() && noErrors;
        noErrors = SERV_R.saveToFile() && noErrors;

        for (TerritoryRegistry terr_r : TERR_R.values()) {
            noErrors = terr_r.saveToFile() && noErrors;
        }

        return noErrors;
    }

    // -- TASK RELATED -- //

    private static final BukkitRunnable SYNC, ASYNC, SAVE, FAVOR, VALUE;


    static {
        SYNC = new BukkitRunnable() {
            @Override
            public void run() {
                // Update online players
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (ZoneUtil.inNoDGCZone(player.getLocation())) continue;
                    PlayerModel model = PLAYER_R.fromPlayer(player);
                    if (model != null) {
                        model.updateCanPvp();
                    }
                }
            }
        };
        ASYNC = new BukkitRunnable() {
            @Override
            public void run() {
                // Update Timed Data
                SERV_R.clearExpired();
            }
        };
        SAVE = new BukkitRunnable() {
            @Override
            public void run() {
                // Save the data
                save();
            }
        };
        FAVOR = new BukkitRunnable() {
            @Override
            public void run() {
                // Update Favor
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (ZoneUtil.inNoDGCZone(player.getLocation())) continue;
                    PlayerModel model = PLAYER_R.fromPlayer(player);
                    if (model != null) {
                        model.updateFavor();
                    }
                }
            }
        };
        VALUE = new TributeModel.ValueTask();
    }

    @SuppressWarnings("deprecation")
    public void startThreads() {
        BukkitScheduler scheduler = Bukkit.getScheduler();

        // Start sync demigods runnable
        scheduler.scheduleSyncRepeatingTask(this, SYNC, 20, 20);
        CONSOLE.info("Main Demigods SYNC runnable enabled...");

        // Start async demigods runnable
        scheduler.scheduleAsyncRepeatingTask(this, ASYNC, 20, 20);
        CONSOLE.info("Main Demigods ASYNC runnable enabled...");

        // Start async demigods runnable
        scheduler.scheduleAsyncRepeatingTask(this, SAVE, 20, 1200);
        CONSOLE.info("Main Demigods SAVE runnable enabled...");

        // Start async favor runnable
        scheduler.scheduleAsyncRepeatingTask(this, FAVOR, 20, (long) ((double) Setting.FAVOR_REGEN_SECONDS.get() * 20));
        CONSOLE.info("Favor regeneration (" + (TimeUnit.SECONDS.toMillis((long) (double) Setting.FAVOR_REGEN_SECONDS.get())) + ") runnable enabled...");

        // Start async value runnable
        scheduler.scheduleAsyncRepeatingTask(this, VALUE, 60, 200);
        CONSOLE.info("Main Demigods VALUE runnable enabled...");
    }

    public static DGClassic getInst() {
        return INST;
    }
}
