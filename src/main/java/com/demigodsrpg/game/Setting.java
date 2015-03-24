package com.demigodsrpg.game;

@SuppressWarnings("unchecked")
public class Setting {
    /*
    MAX_TARGET_RANGE(Integer.class, getNode("max_target_range").getInt(), 100),
    MAX_HP(Integer.class, getNode("max_hp").getInt(), 3000),
    ASCENSION_CAP(Integer.class, getNode("ascension_cap").getInt(), 120),
    FAVOR_CAP(Integer.class, getNode("globalfavorcap").getInt(), 20000),
    FAVOR_REGEN_SECONDS(Double.class, getNode("favor_regen_seconds").getDouble(), 0.5),
    BROADCAST_NEW_ASPECT(Boolean.class, getNode("broadcast_new_aspects").getBoolean(), true),
    ALLOW_PVP_EVERYWHERE(Boolean.class, getNode("allow_skills_everywhere").getBoolean(), false),
    MAX_TEAM_KILLS(Integer.class, getNode("max_team_kills").getInt(), 10),
    FRIENDLY_FIRE(Boolean.class, getNode("friendly_fire").getBoolean(), false),
    EXP_MULTIPLIER(Double.class, getNode("globalexpmultiplier").getDouble(), 4.0),
    NO_COST_ASPECT_MODE(Boolean.class, getNode("no_cost_aspect_mode").getBoolean(), false),
    NO_FACTION_ASPECT_MODE(Boolean.class, getNode("no_faction_aspect_mode").getBoolean(), false),
    SAVE_PRETTY(Boolean.class, getNode("save_pretty").getBoolean(), false),
    DEBUG_DATA(Boolean.class, getNode("debug_data").getBoolean(), false),
    DEBUG_INVISIBLE_WALLS(Boolean.class, getNode("debug_invisible_walls").getBoolean(), false);
    */

    public static final int MAX_TARGET_RANGE = 100;
    public static final int MAX_HP = 3000;
    public static final int ASCENSION_CAP = 120;
    public static final int FAVOR_CAP = 20000;
    public static final int MAX_TEAM_KILLS = 10;
    public static final double FAVOR_REGEN_SECONDS = 0.5;
    public static final double EXP_MULTIPLIER = 4.0;
    public static final boolean BROADCAST_NEW_ASPECT = true;
    public static final boolean ALLOW_PVP_EVERYWHERE = false;
    public static final boolean FRIENDLY_FIRE = false;
    public static final boolean NO_COST_ASPECT_MODE = true; // DEBUG
    public static final boolean NO_FACTION_ASPECT_MODE = true; // DEBUG
    public static final boolean SAVE_PRETTY = true; // DEBUG
    public static final boolean DEBUG_DATA = true; // DEBUG
    public static final boolean DEBUG_INVISIBLE_WALLS = true; // DEBUG

    /*private static CommentedConfigurationNode getNode(Object... args) {
        try {
            return DGGame.GAME.getServiceManager().provide(ConfigRoot.class).get().getConfig().load().getNode(args);
        } catch (Exception oops) {
            oops.printStackTrace();
        }
        return null;
    }*/
}
