package com.demigodsrpg.demigods.classic.deity.god;

import com.demigodsrpg.demigods.classic.ability.Ability;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class Hephaestus implements IDeity {
    @Override
    public String getDeityName() {
        return "Hephaestus";
    }

    @Override
    public String getNomen() {
        return "acolyte of " + getDeityName();
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.BLACK;
    }

    @Override
    public Sound getSound() {
        return Sound.ANVIL_USE;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.ANVIL);
    }

    @Override
    public Importance getImportance() {
        return Importance.MINOR;
    }

    @Override
    public Alliance getDefaultAlliance() {
        return Alliance.OLYMPIAN;
    }

    @Override
    public Pantheon getPantheon() {
        return Pantheon.OLYMPIAN;
    }

    // -- ABILITIES -- //

    @Ability(name = "Furnace Love", info = {"Doubles the output of nearby furnaces."}, type = Ability.Type.PASSIVE)
    public void furnaceLoveAbility(FurnaceSmeltEvent event) {
        int amount = event.getResult().getAmount() * 2;
        ItemStack out = event.getResult();
        out.setAmount(amount);
        event.setResult(out);
    }
}
