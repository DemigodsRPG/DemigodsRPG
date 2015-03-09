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

package com.demigodsrpg.game.shrine;

import org.bukkit.Location;

import java.util.Collection;

public enum Shrine implements IShrine {
    OVERWORLD(new OverworldShrine()), NETHER(new NetherShrine());

    private final IShrine parent;

    private Shrine(IShrine parent) {
        this.parent = parent;
    }

    public void generate(Location reference) {
        parent.generate(reference);
    }

    public Location getClickable(Location reference) {
        return parent.getClickable(reference);
    }

    public Location getSafeTeleport(Location reference) {
        return parent.getSafeTeleport(reference);
    }

    public int getGroundRadius() {
        return parent.getGroundRadius();
    }

    @Override
    public Collection<Location> getLocations(Location reference) {
        return parent.getLocations(reference);
    }

    public enum Status {
        HOLY, SCORN, CORRUPT
    }
}
