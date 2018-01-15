/*
 * Copyright 2014 Alex Bennett & Alexander Chauncey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demigodsrpg.util.schematic;

import com.google.common.collect.Sets;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Set;

public class Schematic extends ArrayList<Selection> {
    private final String name;
    private final String designer;
    private int radius;

    public Schematic(String name, String designer, int groundRadius) {
        this.name = name;
        this.designer = designer;
        this.radius = groundRadius;
    }

    public String getName() {
        return name;
    }

    public String getDesigner() {
        return designer;
    }

    public Set<Location> getLocations(Location reference) {
        Set<Location> locations = Sets.newHashSet();
        for (Selection cuboid : this) { locations.addAll(cuboid.getBlockLocations(reference)); }
        return locations;
    }

    public int getGroundRadius() {
        return this.radius;
    }

    public void generate(final Location reference) {
        for (Selection cuboid : this) {
            cuboid.generate(reference);
        }

        /*for (Item drop : reference.getWorld().getEntitiesByClass(Item.class)) {
            if (reference.distance(drop.getLocation()) <= (getGroundRadius() * 3)) drop.remove();
        }*/
    }

    @Override
    public String toString() {
        return this.name;
    }
}
