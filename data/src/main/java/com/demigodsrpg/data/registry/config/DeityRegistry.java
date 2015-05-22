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

package com.demigodsrpg.data.registry.config;

import com.demigodsrpg.data.deity.Deity;
import com.demigodsrpg.families.data.Family;
import com.demigodsrpg.util.datasection.DataSection;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DeityRegistry extends AbstractConfigRegistry<Deity> {
    private static final String FILE_NAME = "deities";

    public Deity deityFromName(String name) {
        Optional<Deity> found = getRegistered().stream().filter(deity -> deity.getName().equalsIgnoreCase(name)).findAny();
        if (found.isPresent()) {
            return found.get();
        }
        return null;
    }

    public List<Deity> deitiesInFamily(Family family) {
        return getRegistered().stream().filter(deity -> deity.getFamilies().contains(family)).collect(Collectors.toList());
    }

    @Override
    protected Deity valueFromData(String stringKey, DataSection data) {
        return new Deity(stringKey, data);
    }

    @Override
    protected String getName() {
        return FILE_NAME;
    }

    @Override
    protected boolean isPretty() {
        return true;
    }
}
