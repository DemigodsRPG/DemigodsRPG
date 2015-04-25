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

import com.demigodsrpg.ability.AbilityRegistry;
import com.demigodsrpg.data.registry.*;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

public class DGData {
    public static final PlayerRegistry PLAYER_R = new PlayerRegistry();
    public static final ShrineRegistry SHRINE_R = new ShrineRegistry();
    public static final TributeRegistry TRIBUTE_R = new TributeRegistry();
    public static final SpawnRegistry SPAWN_R = new SpawnRegistry();
    public static final FactionRegistry FACTION_R = new FactionRegistry();
    public static final DeityRegistry DEITY_R = new DeityRegistry();
    public static final ServerDataRegistry SERVER_R = new ServerDataRegistry();
    public static final ConcurrentMap<String, AreaRegistry> AREA_R = new ConcurrentHashMap<>();

    public static final AbilityRegistry ABILITY_R = new AbilityRegistry();
    public static final BattleRegistry BATTLE_R = new BattleRegistry();

    public static Plugin PLUGIN;
    public static Logger CONSOLE;
    public static String SAVE_PATH;
}
