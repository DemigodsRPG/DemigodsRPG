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

package com.demigodsrpg.model;

import com.demigodsrpg.family.Family;
import com.demigodsrpg.util.datasection.AbstractPersistentModel;
import com.demigodsrpg.util.datasection.DataSection;
import org.bukkit.*;

import java.util.HashMap;
import java.util.Map;

public class SpawnModel extends AbstractPersistentModel<String> {
    private final Family family;
    private Location location;

    public SpawnModel(Family family, Location location) {
        this.family = family;
        this.location = location;
    }

    public SpawnModel(Family family, DataSection conf) {
        this.family = family;

        World world = Bukkit.getWorld(conf.getString("world_name"));
        if (world != null) {
            double x = conf.getDouble("x");
            double y = conf.getDouble("y");
            double z = conf.getDouble("z");
            float yaw = Float.valueOf(conf.getString("yaw"));
            float pitch = Float.valueOf(conf.getString("pitch"));
            location = new Location(world, x, y, z, yaw, pitch);
        }

        throw new NullPointerException("World not found for the " + family.getName() + " spawn location.");
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("world_name", location.getWorld().getName());
        map.put("x", location.getX());
        map.put("y", location.getY());
        map.put("z", location.getZ());
        map.put("yaw", location.getYaw());
        map.put("pitch", location.getPitch());
        return map;
    }

    public Family getAlliance() {
        return family;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String getPersistentId() {
        return getAlliance().getName();
    }
}
