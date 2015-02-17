package com.demigodsrpg.game.aspect.titan;

import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.aspect.Aspect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public class Oceanus implements Aspect {
    @Override
    public String getDeityName() {
        return "Oceanus";
    }

    @Override
    public String getNomen() {
        return "agent of " + getDeityName();
    }

    @Override
    public String getInfo() {
        return "Titan of the oceans.";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.DARK_AQUA;
    }

    @Override
    public Sound getSound() {
        return Sound.SPLASH;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.WATER_BUCKET);
    }

    @Override
    public Tier getImportance() {
        return Tier.MINOR;
    }

    @Override
    public Aspect.Alliance getDefaultAlliance() {
        return Alliance.TITAN;
    }

    @Override
    public Aspect.Pantheon getPantheon() {
        return Pantheon.TITAN;
    }

    @Ability(name = "Swim", info = "Swim quickly through the water.", type = Ability.Type.SUPPORT, placeholder = true)
    public void swimAbility() {
        // Do nothing, handled directly in the ability listener to save time
    }
}