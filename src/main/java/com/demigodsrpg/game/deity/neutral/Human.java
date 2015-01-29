package com.demigodsrpg.game.deity.neutral;

import com.demigodsrpg.game.deity.IDeity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public class Human implements IDeity {
    @Override
    public String getDeityName() {
        return "Man";
    }

    @Override
    public String getNomen() {
        return "human";
    }

    @Override
    public String getInfo() {
        return "Mortals who preside over the world.";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GRAY;
    }

    @Override
    public Sound getSound() {
        return Sound.BURP;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.POTATO);
    }

    @Override
    public Importance getImportance() {
        return Importance.NONE;
    }

    @Override
    public Alliance getDefaultAlliance() {
        return Alliance.NEUTRAL;
    }

    @Override
    public Pantheon getPantheon() {
        return Pantheon.MORTAL;
    }
}
