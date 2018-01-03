package com.demigodsrpg.aspect.demon;

import com.demigodsrpg.aspect.Aspect;
import org.bukkit.*;
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
        return Sound.ENTITY_GHAST_DEATH;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.BONE);
    }
}
