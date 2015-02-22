package com.demigodsrpg.game.aspect;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public interface Aspect {
    Group getGroup();

    int getId();

    String getInfo();

    Tier getTier();

    public enum Tier {
        NONE, I, II, III
    }

    public interface Group {
        public String getName();

        public ChatColor getColor();

        public Sound getSound();

        public MaterialData getClaimMaterial();
    }
}
