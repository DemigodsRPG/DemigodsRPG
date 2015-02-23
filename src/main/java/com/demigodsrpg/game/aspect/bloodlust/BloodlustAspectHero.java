package com.demigodsrpg.game.aspect.bloodlust;

import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Groups;

public class BloodlustAspectHero implements Aspect {
    @Override
    public Aspect.Group getGroup() {
        return Groups.BLOODLUST_ASPECT;
    }

    @Override
    public int getId() {
        return 16;
    }

    @Override
    public String getInfo() {
        return "Heroic power over bloodlust.";
    }

    @Override
    public Aspect.Tier getTier() {
        return Aspect.Tier.HERO;
    }

    @Ability(name = "Blessings of Battle", info = "Can only die while being attacked.", type = Ability.Type.PASSIVE, placeholder = true)
    public void cheatDeathAbility() {
        // Do nothing, handled directly in the ability listener to save time
    }
}
