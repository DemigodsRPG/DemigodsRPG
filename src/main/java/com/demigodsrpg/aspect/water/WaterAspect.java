package com.demigodsrpg.aspect.water;

import com.demigodsrpg.aspect.Aspect;
import org.bukkit.*;
import org.bukkit.material.MaterialData;

public class WaterAspect implements Aspect.Group {
    @Override
    public String getName() {
        return "Water Aspect";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.AQUA;
    }

    @Override
    public Sound getSound() {
        return Sound.BLOCK_WATER_AMBIENT;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.INK_SAC);
    }
}
