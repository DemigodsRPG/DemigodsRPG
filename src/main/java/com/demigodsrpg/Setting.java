package com.demigodsrpg;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unchecked")
public class Setting {
    public static final int MAX_TARGET_RANGE = getConfig().getInt("max_target_range", 100);
    public static final int MAX_HP = getConfig().getInt("max_hp", 3000);
    public static final int ASCENSION_CAP = getConfig().getInt("ascension_cap", 120);
    public static final int FAVOR_CAP = getConfig().getInt("globalfavorcap", 20000);
    public static final boolean BROADCAST_NEW_ASPECT = getConfig().getBoolean("broadcast_new_aspects", true);
    public static final boolean ALLOW_PVP_EVERYWHERE = getConfig().getBoolean("allow_skills_everywhere", false);
    public static final int MAX_TEAM_KILLS = getConfig().getInt("max_team_kills", 10);
    public static final int BATTLE_INTERVAL_SECONDS = getConfig().getInt("battle_interval_seconds", 10);
    public static final boolean FRIENDLY_FIRE = getConfig().getBoolean("friendly_fire", false);
    public static final double EXP_MULTIPLIER = getConfig().getDouble("globalexpmultiplier", 4.0);
    public static final boolean NO_COST_ASPECT_MODE = getConfig().getBoolean("no_cost_aspect_mode", false);
    public static final boolean NO_FACTION_CONTRACT_MODE = getConfig().getBoolean("no_faction_contract_mode", false);
    public static final boolean SAVE_PRETTY = getConfig().getBoolean("save_pretty", false);
    public static final boolean DEBUG_DATA = getConfig().getBoolean("debug_data", false);
    public static final boolean DEBUG_INVISIBLE_WALLS = getConfig().getBoolean("debug_invisible_walls", false);
    public static boolean MONGODB_PERSISTENCE = getConfig().getBoolean("mongo.use", false);
    public static final String MONGODB_HOSTNAME = getConfig().getString("mongo.hostname", "127.0.0.1");
    public static final int MONGODB_PORT = getConfig().getInt("mongo.port", 27017);
    public static final String MONGODB_DATABASE = getConfig().getString("mongo.database", "demigods");
    public static final String MONGODB_USERNAME = getConfig().getString("mongo.username", "demigods");
    public static final String MONGODB_PASSWORD = getConfig().getString("mongo.password", "demigods");
    public static final boolean NORSE_ENABLED = getConfig().getBoolean("pantheon.norse", true);
    public static final boolean GREEK_ENABLED = getConfig().getBoolean("pantheon.greek", false);

    private static ConfigurationSection getConfig() {
        return JavaPlugin.getProvidingPlugin(Setting.class).getConfig();
    }
}
