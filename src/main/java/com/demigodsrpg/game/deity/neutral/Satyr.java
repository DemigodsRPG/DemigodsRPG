package com.demigodsrpg.game.deity.neutral;

import com.demigodsrpg.game.deity.IDeity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public class Satyr implements IDeity {
    @Override
    public String getDeityName() {
        return "Satyr";
    }

    @Override
    public String getNomen() {
        return "satyr";
    }

    @Override
    public String getInfo() {
        return "Mortal followers of Pan.";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GRAY;
    }

    @Override
    public Sound getSound() {
        return Sound.STEP_GRASS;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.CARROT);
    }

    @Override
    public Importance getImportance() {
        return Importance.NONE;
    }

    @Override
    public Alliance getDefaultAlliance() {
        return Alliance.NEUTRAL;
    }

    @Override
    public Pantheon getPantheon() {
        return Pantheon.OLYMPIAN;
    }
}
