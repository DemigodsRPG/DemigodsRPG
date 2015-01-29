package com.demigodsrpg.game.deity.titan.major;

import com.demigodsrpg.game.deity.IDeity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public class Coeus implements IDeity {
    @Override
    public String getDeityName() {
        return "Coeus";
    }

    @Override
    public String getNomen() {
        return "spawn of " + getDeityName();
    }

    @Override
    public String getInfo() {
        return "Titan of rational knowledge, driven mad.";
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
        return new MaterialData(Material.ENCHANTED_BOOK);
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