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

import org.bukkit.*;

import java.util.List;

public interface AbilityCaster {
    List<String> getAspects();

    boolean getOnline();

    String getMojangId();

    OfflinePlayer getOfflinePlayer();

    Location getLocation();

    AbilityMetaData getBound(Material material);

    Material getBound(AbilityMetaData ability);

    Material getBound(String abilityCommand);

    void bind(AbilityMetaData ability, Material material);

    void bind(String abilityCommand, Material material);

    void unbind(AbilityMetaData ability);

    void unbind(String abilityCommand);

    void unbind(Material material);

    double getFavor();

    void setFavor(double favor);
}
