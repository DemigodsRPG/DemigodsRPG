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

    @Override
    public String name() {
        return "Bloodline";
    }

    @Ability(name = "Blessings of Battle", info = "Can only die while being attacked.", type = Ability.Type.PASSIVE, placeholder = true)
    public void cheatDeathAbility() {
        // Do nothing, handled directly in the ability listener to save time
    }
}
