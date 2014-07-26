package com.demigodsrpg.demigods.classic;

import org.bukkit.configuration.ConfigurationSection;

@SuppressWarnings("unchecked")
public enum Setting {
    MAX_TARGET_RANGE(Integer.class, getConfig().getInt("max_target_range", 100)),
    MAX_HP(Integer.class, getConfig().getInt("max_hp", 3000)),
    ASCENSION_CAP(Integer.class, getConfig().getInt("ascension_cap", 120)),
    FAVOR_CAP(Integer.class, getConfig().getInt("globalfavorcap", 20000)),
    FAVOR_REGEN_SECONDS(Double.class, getConfig().getDouble("favor_regen_seconds", 0.5)),
    BROADCAST_NEW_DEITY(Boolean.class, getConfig().getBoolean("broadcast_new_deities", true)),
    ALLOW_PVP_EVERYWHERE(Boolean.class, getConfig().getBoolean("allow_skills_everywhere", false)),
    MAX_TEAM_KILLS(Integer.class, getConfig().getInt("max_team_kills", 10)),
    FRIENDLY_FIRE(Boolean.class, getConfig().getBoolean("friendly_fire", false)),
    EXP_MULTIPLIER(Double.class, getConfig().getDouble("globalexpmultiplier", 4.0));

    private Class<?> clazz;
    private Object setting;

    private <T> Setting(Class<T> clazz, T setting) {
        this.clazz = clazz;
        this.setting = setting;
    }

    public Class<?> getType() {
        return clazz;
    }

    public <T> T get() {
        return ((Class<T>) clazz).cast(setting);
    }

    public static ConfigurationSection getConfig() {
        return DGClassic.getInst().getConfig();
    }
}
