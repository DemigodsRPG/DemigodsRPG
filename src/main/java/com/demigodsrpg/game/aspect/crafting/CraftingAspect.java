package com.demigodsrpg.game.aspect.crafting;

import com.demigodsrpg.game.aspect.Aspect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
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
        return Sound.ANVIL_USE;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.ANVIL);
    }
}
