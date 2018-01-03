package com.demigodsrpg.aspect.bloodlust;

import com.demigodsrpg.ability.Ability;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Groups;
import com.demigodsrpg.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class BloodlustAspectHero implements Aspect {
    @Override
    public Aspect.Group getGroup() {
        return Groups.BLOODLUST_ASPECT;
    }

    @Override
    public ItemStack getItem() {
        return ItemUtil.create(Material.GOLD_CHESTPLATE, getName(), Arrays.asList(getInfo()), null);
    }

    @Override
    public int getId() {
        return 16;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Heroic power over bloodlust."};
    }

    @Override
    public Aspect.Tier getTier() {
        return Aspect.Tier.HERO;
    }

    @Override
    public String getName() {
        return "Bloodline";
    }

    @Ability(name = "Blessings of Battle", info = "Can only die while being attacked.", type = Ability.Type.PASSIVE, placeholder = true)
    public void cheatDeathAbility() {
        // Do nothing, handled directly in the ability listener to save time
    }
}
