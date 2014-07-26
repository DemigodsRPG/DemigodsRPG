package com.demigodsrpg.demigods.classic.deity.titan;

import com.demigodsrpg.demigods.classic.deity.IDeity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public class Oceanus implements IDeity {
    @Override
    public String getDeityName() {
        return "Oceanus";
    }

    @Override
    public String getNomen() {
        return "Spawn of " + getDeityName();
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.DARK_AQUA;
    }

    @Override
    public Sound getSound() {
        return Sound.SPLASH;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.WATER_BUCKET);
    }

    @Override
    public IDeity.Importance getImportance() {
        return Importance.MINOR;
    }

    @Override
    public IDeity.Alliance getDefaultAlliance() {
        return Alliance.TITAN;
    }

    @Override
    public IDeity.Pantheon getPantheon() {
        return Pantheon.TITAN;
    }
}