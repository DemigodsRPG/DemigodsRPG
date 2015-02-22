package com.demigodsrpg.game.aspect.water;

import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Groups;


public class WaterAspectII implements Aspect {
    @Override
    public Group getGroup() {
        return Groups.WATER_ASPECT;
    }

    @Override
    public int getId() {
        return 8;
    }

    @Override
    public String getInfo() {
        return "Expert level power over water.";
    }

    @Override
    public Tier getTier() {
        return Tier.II;
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
