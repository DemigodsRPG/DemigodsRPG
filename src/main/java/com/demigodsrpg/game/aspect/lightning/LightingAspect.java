package com.demigodsrpg.game.aspect.lightning;

import com.demigodsrpg.game.aspect.Aspect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
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
        return Sound.AMBIENCE_THUNDER;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.FEATHER);
    }
}
