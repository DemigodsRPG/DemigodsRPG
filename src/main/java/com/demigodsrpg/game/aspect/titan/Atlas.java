package com.demigodsrpg.game.aspect.titan;

import com.demigodsrpg.game.aspect.IAspect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public class Atlas implements IAspect {
    @Override
    public String getDeityName() {
        return "Altas";
    }

    @Override
    public String getNomen() {
        return "agent of " + getDeityName();
    }

    @Override
    public String getInfo() {
        return "Titan of strength.";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.BLUE;
    }

    @Override
    public Sound getSound() {
        return Sound.FIREWORK_LAUNCH;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.FIREWORK);
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