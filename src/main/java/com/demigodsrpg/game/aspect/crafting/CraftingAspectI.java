package com.demigodsrpg.game.aspect.crafting;

import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Groups;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

public class CraftingAspectI implements Aspect {
    @Override
    public Group getGroup() {
        return Groups.CRAFTING_ASPECT;
    }

    @Override
    public int getId() {
        return 13;
    }

    @Override
    public String getInfo() {
        return "Adept level power over crafting.";
    }

    @Override
    public Tier getTier() {
        return Tier.I;
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
