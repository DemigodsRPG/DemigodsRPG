package com.demigodsrpg.demigods.classic.deity.god.major;

import com.demigodsrpg.demigods.classic.ability.Ability;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public class Poseidon implements IDeity {
    @Override
    public String getDeityName() {
        return "Poseidon";
    }

    @Override
    public String getNomen() {
        return "child of " + getDeityName();
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.AQUA;
    }

    @Override
    public Sound getSound() {
        return Sound.WATER;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.INK_SACK);
    }

    @Override
    public Importance getImportance() {
        return Importance.MAJOR;
    }

    @Override
    public Alliance getDefaultAlliance() {
        return Alliance.OLYMPIAN;
    }

    @Override
    public Pantheon getPantheon() {
        return Pantheon.OLYMPIAN;
    }

    @Ability(name = "Swim", info = "Swim like quickly poseidon through the water.", type = Ability.Type.PLACEHOLDER)
    public void swimAbility() {
        // Do nothing, handled directly in the ability listener to save time
    }
}
