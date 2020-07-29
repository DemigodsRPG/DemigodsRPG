package com.demigodsrpg.aspect.crafting;

import com.demigodsrpg.aspect.Aspect;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.material.MaterialData;

public class CraftingAspect implements Aspect.Group {
    @Override
    public String getName() {
        return "Crafting Aspect";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.DARK_GRAY;
    }

    @Override
    public Sound getSound() {
        return Sound.BLOCK_ANVIL_USE;
    }

    @Override
    public BlockData getClaimMaterial() {
        return Bukkit.getServer().createBlockData(Material.ANVIL);
    }
}
