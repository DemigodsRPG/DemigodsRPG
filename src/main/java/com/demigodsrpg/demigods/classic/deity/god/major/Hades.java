package com.demigodsrpg.demigods.classic.deity.god.major;

import com.demigodsrpg.demigods.classic.deity.IDeity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public class Hades implements IDeity{
    @Override
    public String getDeityName() {
        return "Hades";
    }

    @Override
    public String getNomen() {
        return "Child of " + getDeityName();
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.DARK_PURPLE;
    }

    @Override
    public Sound getSound() {
        return Sound.GHAST_DEATH;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.BONE);
    }

    @Override
    public IDeity.Importance getImportance() {
        return Importance.MAJOR;
    }

    @Override
    public IDeity.Alliance getDefaultAlliance() {
        return Alliance.OLYMPIAN;
    }

    @Override
    public IDeity.Pantheon getPantheon() {
        return Pantheon.OLYMPIAN;
    }
}