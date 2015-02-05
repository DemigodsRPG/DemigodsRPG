package com.demigodsrpg.game.aspect.god;

import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.aspect.IAspect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class Hephaestus implements IAspect {
    @Override
    public String getDeityName() {
        return "Hephaestus";
    }

    @Override
    public String getNomen() {
        return "acolyte of " + getDeityName();
    }

    @Override
    public String getInfo() {
        return "God of blacksmiths.";
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

    @Override
    public Strength getImportance() {
        return Strength.MINOR;
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
