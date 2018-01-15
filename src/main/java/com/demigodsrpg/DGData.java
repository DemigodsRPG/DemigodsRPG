package com.demigodsrpg;

import com.demigodsrpg.ability.AbilityRegistry;
import com.demigodsrpg.deity.Deity;
import com.demigodsrpg.family.Family;
import com.demigodsrpg.registry.*;
import com.demigodsrpg.registry.file.*;
import com.demigodsrpg.registry.memory.BattleRegistry;
import com.demigodsrpg.registry.mongo.*;
import com.mongodb.client.MongoDatabase;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

public class DGData {

    public static Plugin PLUGIN;
    public static Logger CONSOLE;
    public static String SAVE_PATH;

    public static PlayerRegistry PLAYER_R;
    public static ShrineRegistry SHRINE_R;
    public static TributeRegistry TRIBUTE_R;
    public static SpawnRegistry SPAWN_R;
    public static ServerDataRegistry SERVER_R;

    public static ConcurrentMap<String, AreaRegistry> AREA_R = new ConcurrentHashMap<>();

    public static AbilityRegistry ABILITY_R = new AbilityRegistry(PLAYER_R, SERVER_R, Setting.NO_COST_ASPECT_MODE);
    public static BattleRegistry BATTLE_R = new BattleRegistry();

    public static final List<Pantheon> PANTHEONS = new ArrayList<>();
    public static final List<Family> FAMILIES = new ArrayList<>();
    public static final List<Deity> DEITIES = new ArrayList<>();

    static {
        FAMILIES.add(Family.EXCOMMUNICATED);
        FAMILIES.add(Family.NEUTRAL);
    }

    static void enableMongo(MongoDatabase database) {
        PLAYER_R = new MPlayerRegistry(database);
        SHRINE_R = new MShrineRegistry(database);
        TRIBUTE_R = new MTributeRegistry(database);
        SPAWN_R = new MSpawnRegistry(database);
        SERVER_R = new MServerDataRegistry(database);
    }

    static void enableFile() {
        PLAYER_R = new FPlayerRegistry();
        SHRINE_R = new FShrineRegistry();
        TRIBUTE_R = new FTributeRegistry();
        SPAWN_R = new FSpawnRegistry();
        SERVER_R = new FServerDataRegistry();
    }

    static void addPantheon(Pantheon pantheon) {
        PANTHEONS.add(pantheon);
        FAMILIES.addAll(Arrays.asList(pantheon.getFamilies()));
        DEITIES.addAll(Arrays.asList(pantheon.getDeities()));
    }

    public static Family getFamily(String name) {
        return FAMILIES.stream().filter(f -> f.getName().equals(name)).findAny().orElse(null);
    }

    public static Deity getDeity(String name) {
        return DEITIES.stream().filter(d -> d.getName().equals(name)).findAny().orElse(null);
    }
}
