/*
 * Copyright 2015 Demigods RPG
 * Copyright 2015 Alexander Chauncey
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

package com.demigodsrpg.game.ability;

import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Aspects;

import java.lang.reflect.Method;

public class AbilityMetaData {
    // -- PRIVATE FIELDS -- //

    private final String aspect;
    private final Method method;
    private final Ability ability;

    // -- CONSTRUCTOR -- //

    public AbilityMetaData(Aspect aspect, Method method, Ability ability) {
        this.aspect = aspect.getGroup() + " " + aspect.getTier().name();
        this.method = method;
        this.ability = ability;
    }

    // -- GETTERS -- //

    public Aspect getAspect() {
        return Aspects.valueOf(aspect);
    }

    public String getName() {
        return ability.name();
    }

    public String getCommand() {
        return ability.command();
    }

    public String[] getInfo() {
        return ability.info();
    }

    public Ability.Type getType() {
        return ability.type();
    }

    public double getCost() {
        return ability.cost();
    }

    public long getDelay() {
        return ability.delay();
    }

    public long getCooldown() {
        return ability.cooldown();
    }

    public Method getMethod() {
        return method;
    }

    public Ability getAbility() {
        return ability;
    }
}
