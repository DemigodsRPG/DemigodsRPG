package com.demigodsrpg.demigods.classic.deity.titan;

import com.demigodsrpg.demigods.classic.deity.IDeity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public class Atlas implements IDeity {
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