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

package com.demigodsrpg.game;

import com.demigodsrpg.game.aspect.Groups;
import com.demigodsrpg.game.deity.Deity;
import com.demigodsrpg.game.deity.DeityType;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.deity.Gender;
import org.bukkit.ChatColor;

import java.util.Arrays;

public class Demo {
    public static class D {
        public static final Deity LOREM = new Deity(DeityType.GOD, "Lorem", Gender.EITHER, Faction.NEUTRAL, Arrays.asList(Groups.WATER_ASPECT));
        public static final Deity IPSUM = new Deity(DeityType.HERO, "Ipsum", Gender.FEMALE, F.SENPAI, Arrays.asList(Groups.BLOODLUST_ASPECT));
        public static final Deity DOLOR = new Deity(DeityType.HERO, "Dolor", Gender.MALE, F.KŌHAI, Arrays.asList(Groups.LIGHTNING_ASPECT));
    }

    public static class F {
        public static final Faction KŌHAI = new Faction("Kōhai", ChatColor.GREEN, "KŌHAI", "Kōhai need senpai.");
        public static final Faction SENPAI = new Faction("Senpai", ChatColor.YELLOW, "SENPAI", "Senpai need kōhai.");
    }
}
