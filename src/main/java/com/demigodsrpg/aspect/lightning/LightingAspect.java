package com.demigodsrpg.aspect.lightning;

import com.demigodsrpg.aspect.Aspect;
import org.bukkit.*;
import org.bukkit.material.MaterialData;

public class LightingAspect implements Aspect.Group {
    @Override
    public String getName() {
        return "Lightning Aspect";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.YELLOW;
    }

    @Override
    public Sound getSound() {
        return Sound.ENTITY_LIGHTNING_THUNDER;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.FEATHER);
    }
}
