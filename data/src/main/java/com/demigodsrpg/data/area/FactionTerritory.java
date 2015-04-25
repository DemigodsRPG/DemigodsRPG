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

package com.demigodsrpg.data.area;

import com.demigodsrpg.data.DGData;
import com.demigodsrpg.data.deity.Faction;
import com.demigodsrpg.util.JsonSection;
import com.demigodsrpg.util.LocationUtil;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FactionTerritory extends Area {
    private final String uuid;
    private final Faction faction;

    public FactionTerritory(Faction faction, AreaPriority priority, List<Location> corners) {
        super(priority, corners);
        this.uuid = UUID.randomUUID().toString();
        this.faction = faction;
    }

    public FactionTerritory(String id, final JsonSection conf) {
        super(AreaPriority.valueOf(conf.getString("priority")), new ArrayList<Location>() {{
            addAll(conf.getStringList("locations").stream().map(LocationUtil::locationFromString).collect(Collectors.toList()));
        }});
        this.uuid = id.split("\\$")[2];
        this.faction = DGData.FACTION_R.factionFromName(id.split("\\$")[1]);
    }

    public Faction getFaction() {
        return faction;
    }

    @Override
    public String getPersistentId() {
        return "faction$" + getFaction().getName() + "$" + uuid;
    }
}
