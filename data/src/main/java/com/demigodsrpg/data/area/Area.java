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

import com.demigodsrpg.util.LocationUtil;
import com.demigodsrpg.util.datasection.AbstractPersistentModel;
import org.bukkit.Location;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Area extends AbstractPersistentModel<String> {
    protected final AreaPriority priority;
    protected final List<Location> corners;
    protected final transient Polygon shape;

    public Area(AreaPriority priority, List<Location> corners) {
        this.priority = priority;

        int[] x = new int[corners.size()];
        int[] z = new int[corners.size()];

        for (int i = 0; i < corners.size(); i++) {
            x[i] = corners.get(i).getBlockX();
            z[i] = corners.get(i).getBlockZ();
        }

        this.corners = corners;
        shape = new Polygon(x, z, corners.size());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("priority", priority.name());
        java.util.List<String> locStrings = corners.stream().map(LocationUtil::stringFromLocation).collect(Collectors.toList());
        map.put("locations", locStrings);
        return map;
    }

    public boolean contains(Location location) {
        return shape.contains(location.getX(), location.getZ());
    }

    public List<Location> getCorners() {
        return corners;
    }

    public AreaPriority getPriority() {
        return priority;
    }
}
