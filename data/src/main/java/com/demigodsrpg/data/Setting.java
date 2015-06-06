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

package com.demigodsrpg.data;

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
    public static boolean PSQL_PERSISTENCE = getConfig().getBoolean("psql.use", false);
    public static final String PSQL_CONNECTION = "jdbc:" + getConfig().getString("psql.connection", "postgresql://localhost:5432/demigods?user=demigods&password=demigods");

    private static ConfigurationSection getConfig() {
        return JavaPlugin.getProvidingPlugin(Setting.class).getConfig();
    }
}
