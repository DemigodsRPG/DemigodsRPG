package com.demigodsrpg.game.aspect.titan.major;

import com.demigodsrpg.game.aspect.Aspect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public class Coeus implements Aspect {
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
    public Tier getImportance() {
        return Tier.MAJOR;
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