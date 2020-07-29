package com.demigodsrpg.aspect.lightning;

import com.demigodsrpg.aspect.Aspect;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
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
        return Sound.ENTITY_LIGHTNING_BOLT_THUNDER;
    }

    @Override
    public BlockData getClaimMaterial() {
        return Bukkit.getServer().createBlockData(Material.FEATHER);
    }
}
