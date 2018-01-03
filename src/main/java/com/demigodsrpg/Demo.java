package com.demigodsrpg;

import com.demigodsrpg.aspect.Groups;
import com.demigodsrpg.deity.*;
import com.demigodsrpg.family.Family;
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
