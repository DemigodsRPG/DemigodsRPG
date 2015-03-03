package com.demigodsrpg.game;

import com.demigodsrpg.game.command.*;
import com.demigodsrpg.game.command.admin.*;
import com.demigodsrpg.game.deity.Deity;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.listener.*;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.model.TributeModel;
import com.demigodsrpg.game.registry.*;
import com.demigodsrpg.game.util.ZoneUtil;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.state.ServerStartedEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.service.config.ConfigRoot;
import org.spongepowered.api.service.event.EventManager;
import org.spongepowered.api.service.scheduler.AsynchronousScheduler;
import org.spongepowered.api.service.scheduler.SynchronousScheduler;
import org.spongepowered.api.util.event.Subscribe;
import org.spongepowered.api.world.World;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Plugin(id = "demigods",
        name = "Demigods RPG",
        version = "4.0.0")
public class DGGame {
    // -- GAME RELATED CONSTANTS -- //
    public static Game GAME;
    public static Server SERVER;

    // -- PLUGIN RELATED CONSTANTS -- //

    private static DGGame INST;
    public static PluginContainer PLUGIN;
    public static Logger CONSOLE;
    public static String SAVE_PATH;

    // -- REGISTRIES -- //

    public static final PlayerRegistry PLAYER_R = new PlayerRegistry();
    public static final ShrineRegistry SHRINE_R = new ShrineRegistry();
    public static final TributeRegistry TRIBUTE_R = new TributeRegistry();
    public static final SpawnRegistry SPAWN_R = new SpawnRegistry();
    public static final FactionRegistry FACTION_R = new FactionRegistry();
    public static final BattleRegistry BATTLE_R = new BattleRegistry();
    public static final AbilityRegistry ABILITY_R = new AbilityRegistry();
    public static final DeityRegistry DEITY_R = new DeityRegistry();
    public static final ServerDataRegistry MISC_R = new ServerDataRegistry();
    public static final ConcurrentMap<String, AreaRegistry> AREA_R = new ConcurrentHashMap<>();

    // -- PLUGIN RELATED INSTANCE METHODS -- //
    @Subscribe
    public void onEnable(ServerStartedEvent event) {
        // Define the game
        GAME = event.getGame();
        SERVER = event.getGame().getServer().get();

        // Define the instance
        INST = this;
        PLUGIN = GAME.getPluginManager().fromInstance(this).get();

        // Define the logger
        CONSOLE = GAME.getPluginManager().getLogger(PLUGIN);

        // FIXME Config
        // getConfig().copyDefaults(true);
        // saveConfig();

        // Define the save path
        SAVE_PATH = GAME.getServiceManager().provide(ConfigRoot.class).get().getDirectory().getPath() + "/data/";

        // Register default factions
        FACTION_R.register(Faction.NEUTRAL);
        FACTION_R.register(Faction.EXCOMMUNICATED);

        // Debug data
        if (Setting.DEBUG_DATA.get()) {
            // Debug deities
            DEITY_R.register(Deity.LOREM);
            DEITY_R.register(Deity.IPSUM);
        }

        // Determine territory registries
        for (World world : SERVER.getWorlds()) {
            AreaRegistry area_r = new AreaRegistry(world);
            area_r.registerFromFile();
            AREA_R.put(world.getName(), new AreaRegistry(world));
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
        EventManager manager = GAME.getEventManager();
        manager.register(new InventoryListener(), this);
        manager.register(new PlayerListener(), this);
        manager.register(new ShrineListener(), this);
        manager.register(new TributeListener(), this);
        manager.register(new AreaListener(), this);
        manager.register(ABILITY_R, this);

        // Register commands
        CommandService commands = GAME.getCommandDispatcher();
        commands.register(PLUGIN, new FactionCommand(), "faction", "f");
        commands.register(PLUGIN, new BindsCommand(), "binds", "b");
        commands.register(PLUGIN, new CheckCommand(), "check", "c");
        commands.register(PLUGIN, new AspectCommand(), "aspect", "a");
        commands.register(PLUGIN, new CleanseCommand(), "cleanse");
        commands.register(PLUGIN, new ShrineCommand(), "shrine", "sh", "s");
        commands.register(PLUGIN, new ValuesCommand(), "values", "value", "v");

        // Admin commands
        commands.register(PLUGIN, new AdminModeComand(), "adminmode", "admin");
        commands.register(PLUGIN, new SelectAreaCommand(), "selectarea", "sa");
        commands.register(PLUGIN, new CreateFactionAreaCommand(), "createfactionarea");
        commands.register(PLUGIN, new CheckPlayerCommand(), "checkplayer");
        commands.register(PLUGIN, new AddDevotionCommand(), "adddevotion");
        commands.register(PLUGIN, new RemoveDevotionCommand(), "removedevotion");
        commands.register(PLUGIN, new GiveAspectCommand(), "giveaspect");
        commands.register(PLUGIN, new RemoveAspectCommand(), "removeaspect");
        commands.register(PLUGIN, new SetFactionCommand(), "setfaction");

        // Enable ZoneUtil
        ZoneUtil.init();

        // Handle Chitchat integration FIXME
        if (GAME.getPluginManager().isLoaded("Chitchat")) {
            // Chitchat.getChatFormat().add(new FactionChatTag());
            // Chitchat.getChatFormat().add(new FactionIdTag());
        }

        // Let the console know
        CONSOLE.info("Enabled and ready for battle.");
    }

    @Subscribe
    public void onDisable(ServerStoppingEvent event) {
        // Ensure that we unregister our commands and tasks
        GAME.getEventManager().unregister(PLUGIN); // TODO Test this
        // Bukkit.getScheduler().cancelTasks(this); FIXME

        // Clear the cache.
        clearCache();

        // Let the console know
        CONSOLE.info("Disabled successfully.");
    }

    // -- PLUGIN RELATED UTILITY METHODS -- //

    private static void clearCache() {
        PLAYER_R.clearCache();
        SHRINE_R.clearCache();
        FACTION_R.clearCache();
        TRIBUTE_R.clearCache();
        SPAWN_R.clearCache();
        DEITY_R.clearCache();
        MISC_R.clearCache();

        AREA_R.values().forEach(AreaRegistry::clearCache);
    }

    // -- TASK RELATED -- //

    private static final Runnable SYNC, ASYNC, /*FIRE_SPREAD,*/
            VALUE;

    static {
        SYNC = new Runnable() {
            @Override
            public void run() {
                // Update online players
                for (Player player : SERVER.getOnlinePlayers()) {
                    if (ZoneUtil.inNoDGZone(player.getLocation())) continue;
                    PlayerModel model = PLAYER_R.fromPlayer(player);
                    if (model != null) {
                        model.updateCanPvp();
                    }
                }
            }
        };
        ASYNC = new Runnable() {
            @Override
            public void run() {
                // Update Timed Data
                MISC_R.clearExpired();
            }
        };
        /*FIRE_SPREAD = new Runnable() { TODO Replace with EntityCollisionEvent
            @Override
            public void run() {
                for (World world : SERVER.getWorlds()) {
                    world.getEntities().stream().filter(entity -> entity.getFireTicks() > 0).forEach(entity -> {
                        entity.getNearbyEntities(0.5, 0.5, 0.5).stream().filter(nearby -> nearby instanceof LivingEntity && !nearby.equals(entity)).forEach(nearby -> {
                            nearby.setFireTicks(100);
                        });
                    });
                }
            }
        }; */
        VALUE = new TributeModel.ValueTask();
    }

    @SuppressWarnings("deprecation")
    void startThreads() {
        SynchronousScheduler sync = GAME.getSyncScheduler();
        AsynchronousScheduler async = GAME.getAsyncScheduler();

        // Start sync demigods runnable
        sync.runRepeatingTaskAfter(this, SYNC, 20, 20); // TODO Test if these are ticks or seconds/milliseconds
        CONSOLE.info("Main Demigods SYNC runnable enabled...");

        // Start async demigods runnable
        async.runRepeatingTaskAfter(this, ASYNC, TimeUnit.SECONDS, 1, 1);
        CONSOLE.info("Main Demigods ASYNC runnable enabled...");

        // Start async value runnable
        async.runRepeatingTaskAfter(this, VALUE, TimeUnit.SECONDS, 3, 20);
        CONSOLE.info("Main Demigods VALUE runnable enabled...");
    }

    public static DGGame getInst() {
        return INST;
    }
}
