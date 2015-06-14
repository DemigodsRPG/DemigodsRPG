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

package com.demigodsrpg.ability;

import java.util.concurrent.TimeUnit;

public interface CooldownHandler {
    default boolean hasDelay(AbilityCaster model, AbilityMetaData ability) {
        return contains(model.getMojangId(), ability.getName() + ":delay");
    }

    default boolean hasCooldown(AbilityCaster model, AbilityMetaData ability) {
        return contains(model.getMojangId(), ability.getName() + ":cooldown");
    }

    default void delay(AbilityCaster model, AbilityMetaData ability) {
        put(model.getMojangId(), ability.getName() + ":delay", true, ability.getDelay(), TimeUnit.MILLISECONDS);
    }

    default void cooldown(AbilityCaster model, AbilityMetaData ability) {
        put(model.getMojangId(), ability.getName() + ":cooldown", true, ability.getCooldown(), TimeUnit.MILLISECONDS);
    }

    boolean contains(String row, String column);

    void put(String row, String column, Object value, long time, TimeUnit unit);
}
