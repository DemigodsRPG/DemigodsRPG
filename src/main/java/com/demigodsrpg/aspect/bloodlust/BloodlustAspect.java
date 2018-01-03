package com.demigodsrpg.aspect.bloodlust;

import com.demigodsrpg.aspect.Aspect;
import org.bukkit.*;
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
        return Sound.ENTITY_VILLAGER_HURT;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.GOLD_SWORD);
    }
}
