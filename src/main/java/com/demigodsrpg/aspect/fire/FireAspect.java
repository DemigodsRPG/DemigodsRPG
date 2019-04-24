package com.demigodsrpg.aspect.fire;

import com.demigodsrpg.aspect.Aspect;
import org.bukkit.*;
import org.bukkit.material.MaterialData;

public class FireAspect implements Aspect.Group {
    @Override
    public String getName() {
        return "Fire Aspect";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GOLD;
    }

    @Override
    public Sound getSound() {
        return Sound.BLOCK_FIRE_AMBIENT;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.FIRE_CHARGE);
    }
}
