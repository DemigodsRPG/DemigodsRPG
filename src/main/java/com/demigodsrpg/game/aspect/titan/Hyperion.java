package com.demigodsrpg.game.aspect.titan;

import com.demigodsrpg.game.aspect.IAspect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public class Hyperion implements IAspect {
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
    public Strength getImportance() {
        return Strength.MINOR;
    }

    @Override
    public IAspect.Alliance getDefaultAlliance() {
        return Alliance.TITAN;
    }

    @Override
    public IAspect.Pantheon getPantheon() {
        return Pantheon.TITAN;
    }
}