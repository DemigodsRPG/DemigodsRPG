package com.demigodsrpg.demigods.classic.deity.god;

import com.demigodsrpg.demigods.classic.deity.IDeity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public class Ares implements IDeity {
    @Override
    public String getDeityName() {
        return "Ares";
    }

    @Override
    public String getNomen() {
        return "Child of " + getDeityName();
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.RED;
    }

    @Override
    public Sound getSound() {
        return Sound.VILLAGER_HIT;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.GOLD_SWORD);
    }

    @Override
    public IDeity.Importance getImportance() {
        return Importance.MINOR;
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