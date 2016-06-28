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

package com.demigodsrpg.shrine;

import com.demigodsrpg.util.schematic.Schematic;
import com.demigodsrpg.util.schematic.Selection;
import org.bukkit.Location;
import org.bukkit.Material;

public class NetherShrine extends Schematic implements IShrine {
    public NetherShrine() {
        super("Nether Shrine", "HmmmQuestionMark", 6);

        // Create the main block
        add(new Selection(0, 1, 0, Material.GOLD_BLOCK.name()));

        // Create the ender chest and the block below
        add(new Selection(0, 0, 0, Material.ENDER_CHEST.name()));
        add(new Selection(0, -1, 0, Material.NETHER_BRICK.name()));

        // Create the rest
        add(new Selection(-1, 0, 0, Material.NETHER_BRICK_STAIRS.name()));
        add(new Selection(1, 0, 0, Material.NETHER_BRICK_STAIRS.name(), (byte) 1));
        add(new Selection(0, 0, -1, Material.NETHER_BRICK_STAIRS.name(), (byte) 2));
        add(new Selection(0, 0, 1, Material.NETHER_BRICK_STAIRS.name(), (byte) 3));

        // Safe zone
        add(new Selection(1, -1, 1, Material.NETHER_BRICK.name()));
        add(new Selection(1, 0, 1, Material.AIR.name()));
        add(new Selection(1, 1, 1, Material.AIR.name()));
    }

    @Override
    public Location getClickable(Location reference) {
        return reference.clone().add(0, 1, 0);
    }

    @Override
    public Location getSafeTeleport(Location reference) {
        return reference.clone().add(1.5, 0, 1.5);
    }
}
