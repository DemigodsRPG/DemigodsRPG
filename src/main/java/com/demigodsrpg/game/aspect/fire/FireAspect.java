package com.demigodsrpg.game.aspect.fire;

import com.demigodsrpg.game.aspect.Aspect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
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
        return Sound.FIRE;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.FIREBALL);
    }
}
