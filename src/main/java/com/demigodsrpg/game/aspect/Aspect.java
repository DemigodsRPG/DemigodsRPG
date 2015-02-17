package com.demigodsrpg.game.aspect;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public interface Aspect {
    String getName();

    String name();

    int getId();

    String getInfo();

    ChatColor getColor();

    Sound getSound();

    MaterialData getClaimMaterial();

    Tier getTier();

    public enum Tier {
        NONE, I, II, III
    }
}
