package com.demigodsrpg.demigods.classic.deity.titan.major;

import com.demigodsrpg.demigods.classic.deity.IDeity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public class Rhea implements IDeity {
    @Override
    public String getDeityName() {
        return "Rhea";
    }

    @Override
    public String getNomen() {
        return "spawn of " + getDeityName();
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.DARK_GREEN;
    }

    @Override
    public Sound getSound() {
        return Sound.STEP_WOOD;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.LOG);
    }

    @Override
    public IDeity.Importance getImportance() {
        return Importance.MAJOR;
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