package com.demigodsrpg.aspect.crafting;


import com.demigodsrpg.ability.Ability;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Groups;
import com.demigodsrpg.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class CraftingAspectI implements Aspect {
    @Override
    public Group getGroup() {
        return Groups.CRAFTING_ASPECT;
    }

    @Override
    public ItemStack getItem() {
        return ItemUtil.create(Material.FURNACE, getName(), Arrays.asList(getInfo()), null);
    }

    @Override
    public int getId() {
        return 13;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Adept level power over crafting."};
    }

    @Override
    public Tier getTier() {
        return Tier.I;
    }

    @Override
    public String getName() {
        return "Sweat of the Brow";
    }

    // -- ABILITIES -- //

    @Ability(name = "Furnace Love", info = {"Doubles the output of nearby furnaces."}, type = Ability.Type.PASSIVE,
            placeholder = true)
    public void furnaceLoveAbility() {
        // Do nothing, handled directly in the ability listener to save time
    }
}
