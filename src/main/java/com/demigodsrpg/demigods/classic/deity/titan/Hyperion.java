package com.demigodsrpg.demigods.classic.deity.titan;

import com.demigodsrpg.demigods.classic.deity.IDeity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public class Hyperion implements IDeity {
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