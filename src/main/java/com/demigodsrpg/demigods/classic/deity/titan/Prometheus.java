package com.demigodsrpg.demigods.classic.deity.titan;

import com.demigodsrpg.demigods.classic.deity.IDeity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public class Prometheus implements IDeity {
    @Override
    public String getDeityName() {
        return "Prometheus";
    }

    @Override
    public String getNomen() {
        return "Spawn of " + getDeityName();
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GOLD;
    }

    @Override
    public Sound getSound() {
        return Sound.FIRE;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.FIREBALL);
    }

    @Override
    public Importance getImportance() {
        return Importance.MINOR;
    }

    @Override
    public Alliance getDefaultAlliance() {
        return Alliance.OLYMPIAN;
    }

    @Override
    public Pantheon getPantheon() {
        return Pantheon.TITAN;
    }
}
