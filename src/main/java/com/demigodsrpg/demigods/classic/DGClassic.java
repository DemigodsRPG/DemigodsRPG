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
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

public class DGClassic extends JavaPlugin {
    // -- PLUGIN RELATED CONSTANTS -- //

    private static DGClassic INST;
    public static Logger CONSOLE;
    public static String SAVE_PATH;

    // -- REGISTRIES -- //

    public static final PlayerRegistry PLAYER_R = new PlayerRegistry();
    public static final ShrineRegistry SHRINE_R = new ShrineRegistry();
    public static final TributeRegistry TRIBUTE_R = new TributeRegistry();
    private static final SpawnRegistry SPAWN_R = new SpawnRegistry();
    public static final BattleRegistry BATTLE_R = new BattleRegistry();
    public static final AbilityRegistry ABILITY_R = new AbilityRegistry();
    public static final ServerDataRegistry SERV_R = new ServerDataRegistry();
    private static final ConcurrentMap<String, TerritoryRegistry> TERR_R = new ConcurrentHashMap<>();

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
        getCommand("deity").setExecutor(new DeityCommand());
        getCommand("forsake").setExecutor(new ForsakeCommand());
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

        // Clear the cache.
        clearCache();

        // Let the console know
        CONSOLE.info("Disabled successfully.");
    }

    // -- PLUGIN RELATED UTILITY METHODS -- //

    private static void clearCache() {
        PLAYER_R.clearCache();
        SHRINE_R.clearCache();
        TRIBUTE_R.clearCache();
        SPAWN_R.clearCache();
        SERV_R.clearCache();

        for (TerritoryRegistry terr_r : TERR_R.values()) {
            terr_r.clearCache();
        }
    }

    // -- TASK RELATED -- //

    private static final BukkitRunnable SYNC, ASYNC, VALUE;

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
        VALUE = new TributeModel.ValueTask();
    }

    @SuppressWarnings("deprecation")
    void startThreads() {
        BukkitScheduler scheduler = Bukkit.getScheduler();

        // Start sync demigods runnable
        scheduler.scheduleSyncRepeatingTask(this, SYNC, 20, 20);
        CONSOLE.info("Main Demigods SYNC runnable enabled...");

        // Start async demigods runnable
        scheduler.scheduleAsyncRepeatingTask(this, ASYNC, 20, 20);
        CONSOLE.info("Main Demigods ASYNC runnable enabled...");

        // Start async value runnable
        scheduler.scheduleAsyncRepeatingTask(this, VALUE, 60, 400);
        CONSOLE.info("Main Demigods VALUE runnable enabled...");
    }

    public static DGClassic getInst() {
        return INST;
    }
}
