package com.demigodsrpg.game.aspect;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public interface IAspect {
    String getName();

    String getInfo();

    ChatColor getColor();

    Sound getSound();

    MaterialData getClaimMaterial();

    Strength getStrength();

    public enum Strength {
        NONE, SMALL, MEDIUM, LARGE
    }
}
