package com.demigodsrpg.game.aspect.water;

import com.demigodsrpg.game.aspect.Aspect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
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
        return Sound.WATER;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.INK_SACK);
    }
}
