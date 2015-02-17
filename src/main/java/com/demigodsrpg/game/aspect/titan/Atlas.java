package com.demigodsrpg.game.aspect.titan;

import com.demigodsrpg.game.aspect.Aspect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public class Atlas implements Aspect {
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