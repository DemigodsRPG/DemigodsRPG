package com.demigodsrpg.aspect.water;


import com.demigodsrpg.ability.Ability;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Groups;
import com.demigodsrpg.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;


public class WaterAspectHero implements Aspect {
    @Override
    public Group getGroup() {
        return Groups.WATER_ASPECT;
    }

    @Override
    public ItemStack getItem() {
        return ItemUtil.create(Material.SEA_LANTERN, getName(), Arrays.asList(getInfo()), null);
    }

    @Override
    public int getId() {
        return 8;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Expert level power over water."};
    }

    @Override
    public Tier getTier() {
        return Tier.HERO;
    }

    @Override
    public String getName() {
        return "Soul of the Sea";
    }

    @Ability(name = "Swim", info = "Swim like quickly poseidon through the water.", type = Ability.Type.SUPPORT, placeholder = true)
    public void swimAbility() {
        // Do nothing, handled directly in the ability listener to save time
    }

    @Ability(name = "No Drown Damage", info = "Take no drown damage.", type = Ability.Type.PASSIVE, placeholder = true)
    public void noDrownDamageAbility() {
        // Do nothing, handled directly in the ability listener to save time
    }
}
