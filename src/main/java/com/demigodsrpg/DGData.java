package com.demigodsrpg;

import com.demigodsrpg.ability.AbilityRegistry;
import com.demigodsrpg.registry.*;
import com.demigodsrpg.registry.config.*;
import com.demigodsrpg.registry.memory.BattleRegistry;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

public class DGData {

    public static Plugin PLUGIN;
    public static Logger CONSOLE;
    public static String SAVE_PATH;

    public static final PlayerRegistry PLAYER_R = new PlayerRegistry();
    public static final ShrineRegistry SHRINE_R = new ShrineRegistry();
    public static final TributeRegistry TRIBUTE_R = new TributeRegistry();
    public static final SpawnRegistry SPAWN_R = new SpawnRegistry();
    public static final FamilyRegistry FAMILY_R = new FamilyRegistry();
    public static final DeityRegistry DEITY_R = new DeityRegistry();
    public static final ServerDataRegistry SERVER_R = new ServerDataRegistry();
    public static final ConcurrentMap<String, AreaRegistry> AREA_R = new ConcurrentHashMap<>();

    public static final AbilityRegistry ABILITY_R = new AbilityRegistry(PLAYER_R, SERVER_R, Setting.NO_COST_ASPECT_MODE);
    public static final BattleRegistry BATTLE_R = new BattleRegistry();

    // -- DATA RELATED UTILITY METHODS -- //

    public static void clearCache() {
        PLAYER_R.clearCache();
        SHRINE_R.clearCache();
        FAMILY_R.clearCache();
        TRIBUTE_R.clearCache();
        SPAWN_R.clearCache();
        DEITY_R.clearCache();
        SERVER_R.clearCache();

        AREA_R.values().forEach(AreaRegistry::clearCache);
    }
}
