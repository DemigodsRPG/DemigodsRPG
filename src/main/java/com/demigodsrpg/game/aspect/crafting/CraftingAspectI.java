/*
 * Copyright 2015 Demigods RPG
 * Copyright 2015 Alexander Chauncey
 * Copyright 2015 Alex Bennett
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
