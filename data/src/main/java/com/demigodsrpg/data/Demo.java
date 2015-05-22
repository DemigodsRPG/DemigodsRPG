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

import com.demigodsrpg.aspect.Groups;
import com.demigodsrpg.data.deity.Deity;
import com.demigodsrpg.data.deity.DeityType;
import com.demigodsrpg.data.deity.Gender;
import com.demigodsrpg.families.data.Family;
import org.bukkit.ChatColor;

public class Demo {
    public static class D {
        public static final Deity LOREM = new Deity(DeityType.GOD, "Lorem", Gender.EITHER, Family.NEUTRAL, Groups.WATER_ASPECT, Groups.FIRE_ASPECT);
        public static final Deity IPSUM = new Deity(DeityType.HERO, "Ipsum", Gender.FEMALE, F.SENPAI, Groups.BLOODLUST_ASPECT);
        public static final Deity DOLOR = new Deity(DeityType.HERO, "Dolor", Gender.MALE, F.KŌHAI, Groups.MAGNETISM_ASPECT);
        public static final Deity SIT = new Deity(DeityType.GOD, "Sit", Gender.FEMALE, F.SENSEI, Groups.BLOODLUST_ASPECT, Groups.LIGHTNING_ASPECT);
        public static final Deity AMET = new Deity(DeityType.HERO, "Amet", Gender.MALE, F.SENSEI, Groups.WATER_ASPECT);
    }

    public static class F {
        public static final Family KŌHAI = new Family("Kōhai", ChatColor.GREEN, "KOHAI", "Kohai need senpai.");
        public static final Family SENPAI = new Family("Senpai", ChatColor.YELLOW, "SENPAI", "Senpai need kohai.");
        public static final Family SENSEI = new Family("Sensei", ChatColor.DARK_AQUA, "SENSEI", "Sensei do their thing.");
    }
}
