/*
 * Copyright 2015 Demigods RPG
 * Copyright 2015 Alexander Chauncey
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

package com.demigodsrpg.game.shrine;

import com.censoredsoftware.library.schematic.Point;
import com.censoredsoftware.library.schematic.PotentialMaterial;
import com.censoredsoftware.library.schematic.World;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class ShrineWorld implements World {
    private final org.bukkit.World world;

    public ShrineWorld(org.bukkit.World world) {
        this.world = world;
    }

    public ShrineWorld(String worldName) {
        this.world = Bukkit.getWorld(worldName);
    }

    @Override
    public PotentialMaterial getMaterialAt(int x, int y, int z) {
        Block block = world.getBlockAt(x, y, z);
        return new PotentialMaterial(block.getType().name(), block.getData());
    }

    @Override
    public void setPoint(Point point, PotentialMaterial material) {
        Block block = world.getBlockAt(point.getX(), point.getY(), point.getZ());
        block.setType(Material.valueOf(material.getMaterial()));
        block.setData(material.getData());
    }

    @Override
    public String getName() {
        return world.getName();
    }

    public org.bukkit.World getBukkitWorld() {
        return world;
    }
}
