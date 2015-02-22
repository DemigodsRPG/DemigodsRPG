package com.demigodsrpg.game.aspect.bloodlust;

import com.demigodsrpg.game.aspect.Aspect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public class BloodlustAspect implements Aspect.Group {
    @Override
    public String getName() {
        return "Bloodlust Aspect";
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
}
