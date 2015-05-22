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

package com.demigodsrpg.data.registry;

import com.demigodsrpg.data.deity.Family;
import com.demigodsrpg.data.model.SpawnModel;
import com.demigodsrpg.util.DataSection;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Optional;

public class SpawnRegistry extends AbstractDataRegistry<SpawnModel> {
    private static final String FILE_NAME = "spawns";

    public Location getSpawn(final Family family) {
        Optional<SpawnModel> point = getRegistered().stream().filter(model -> model.getAlliance().equals(family)).findAny();
        if (!point.isPresent()) {
            return Bukkit.getWorlds().get(0).getSpawnLocation();
        }
        return point.get().getLocation();
    }

    @Override
    public SpawnModel valueFromData(String stringKey, DataSection data) {
        // FIXME return better faction
        return new SpawnModel(Family.NEUTRAL, data);
    }

    @Override
    public String getName() {
        return FILE_NAME;
    }
}
