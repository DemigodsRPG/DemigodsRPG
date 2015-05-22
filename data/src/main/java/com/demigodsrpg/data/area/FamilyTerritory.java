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
import com.demigodsrpg.data.deity.Family;
import com.demigodsrpg.util.DataSection;
import com.demigodsrpg.util.LocationUtil;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FamilyTerritory extends Area {
    private final String uuid;
    private final Family family;

    public FamilyTerritory(Family family, AreaPriority priority, List<Location> corners) {
        super(priority, corners);
        this.uuid = UUID.randomUUID().toString();
        this.family = family;
    }

    public FamilyTerritory(String id, final DataSection conf) {
        super(AreaPriority.valueOf(conf.getString("priority")), new ArrayList<Location>() {{
            addAll(conf.getStringList("locations").stream().map(LocationUtil::locationFromString).collect(Collectors.toList()));
        }});
        this.uuid = id.split("\\$")[2];
        this.family = DGData.FAMILY_R.familyFromName(id.split("\\$")[1]);
    }

    public Family getFamily() {
        return family;
    }

    @Override
    public String getPersistentId() {
        return "family$" + getFamily().getName() + "$" + uuid;
    }
}
