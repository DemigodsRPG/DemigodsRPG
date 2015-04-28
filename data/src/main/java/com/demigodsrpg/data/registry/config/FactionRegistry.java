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

import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Groups;
import com.demigodsrpg.data.DGData;
import com.demigodsrpg.data.deity.Deity;
import com.demigodsrpg.data.deity.Faction;
import com.demigodsrpg.util.DataSection;

import java.util.Optional;

public class FactionRegistry extends AbstractConfigRegistry<Faction> {

    private static final String FILE_NAME = "factions";

    public Faction factionFromName(final String name) {
        Optional<Faction> found = getRegistered().stream().filter(faction -> faction.getName().equalsIgnoreCase(name)).findAny();
        if (found.isPresent()) {
            return found.get();
        }
        return null;
    }

    @Override
    protected Faction valueFromData(String stringKey, DataSection data) {
        return new Faction(stringKey, data);
    }

    @Override
    protected String getName() {
        return FILE_NAME;
    }

    @Override
    protected boolean isPretty() {
        return true;
    }

    public boolean isInFaction(Faction faction, Aspect aspect) {
        for (Deity inFaction : DGData.DEITY_R.deitiesInFaction(faction)) {
            for (Aspect.Group group : inFaction.getAspectGroups()) {
                if (Groups.aspectsInGroup(group).contains(aspect)) {
                    return true;
                }
            }
        }
        return false;
    }
}
