package com.demigodsrpg.game.aspect.demon;

import com.demigodsrpg.game.aspect.Aspect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public class DemonAspect implements Aspect.Group {
    @Override
    public String getName() {
        return "Demon Aspect";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.DARK_PURPLE;
    }

    @Override
    public Sound getSound() {
        return Sound.GHAST_DEATH;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.BONE);
    }
}
