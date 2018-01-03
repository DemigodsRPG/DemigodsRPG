package com.demigodsrpg.aspect.magnetism;

import com.demigodsrpg.aspect.Aspect;
import org.bukkit.*;
import org.bukkit.material.MaterialData;

public class MagnetismAspect implements Aspect.Group {
    @Override
    public String getName() {
        return "Magnetism Aspect";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.BLUE;
    }

    @Override
    public Sound getSound() {
        return Sound.UI_BUTTON_CLICK;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.IRON_INGOT);
    }
}
