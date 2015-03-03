package com.demigodsrpg.game;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.service.config.ConfigRoot;

@SuppressWarnings("unchecked")
public enum Setting {
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

    private final Class<?> clazz;
    private final Object setting;

    private <T> Setting(Class<T> clazz, T setting, T def) {
        this.clazz = clazz;
        if (setting != null) {
            this.setting = setting;
        } else {
            this.setting = def;
        }
    }

    public Class<?> getType() {
        return clazz;
    }

    public <T> T get() {
        return ((Class<T>) clazz).cast(setting);
    }

    private static CommentedConfigurationNode getNode(Object... args) {
        try {
            return DGGame.GAME.getServiceManager().provide(ConfigRoot.class).get().getConfig().load().getNode(args);
        } catch (Exception oops) {
            oops.printStackTrace();
        }
        return null;
    }
}
