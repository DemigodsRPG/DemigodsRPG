package com.demigodsrpg.game.aspect.titan;

import com.demigodsrpg.game.aspect.Aspect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public class Hyperion implements Aspect {
    @Override
    public String getDeityName() {
        return "Hyperion";
    }

    @Override
    public String getNomen() {
        return "agent of " + getDeityName();
    }

    @Override
    public String getInfo() {
        return "Titan of the sun.";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GREEN;
    }

    @Override
    public Sound getSound() {
        return Sound.WITHER_IDLE;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.GOLD_BOOTS);
    }

    @Override
    public Tier getImportance() {
        return Tier.MINOR;
    }

    @Override
    public Aspect.Alliance getDefaultAlliance() {
        return Alliance.TITAN;
    }

    @Override
    public Aspect.Pantheon getPantheon() {
        return Pantheon.TITAN;
    }
}