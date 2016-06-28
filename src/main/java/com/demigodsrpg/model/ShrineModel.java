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

import com.demigodsrpg.DGData;
import com.demigodsrpg.deity.Deity;
import com.demigodsrpg.family.Family;
import com.demigodsrpg.shrine.Shrine;
import com.demigodsrpg.util.datasection.AbstractPersistentModel;
import com.demigodsrpg.util.datasection.DataSection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ShrineModel extends AbstractPersistentModel<String> {
    private final String shrineId;
    private final String ownerMojangId;
    private final Deity deity;
    private final Shrine shrine;
    private final Location location;

    public ShrineModel(String name, Player player, Deity deity, Shrine shrine, final Location location) {
        shrineId = name;
        ownerMojangId = player.getUniqueId().toString();
        this.deity = deity;
        this.shrine = shrine;
        this.location = location;
    }

    public ShrineModel(String shrineId, DataSection conf) {
        this.shrineId = shrineId;
        ownerMojangId = conf.getString("owner_id");
        deity = DGData.DEITY_R.deityFromName(conf.getString("deity"));
        shrine = Shrine.valueOf(conf.getString("type"));

        World world = Bukkit.getWorld(conf.getString("world_name"));
        if (world != null) {
            int x = conf.getInt("x");
            int y = conf.getInt("y");
            int z = conf.getInt("z");
            location = new Location(world, x, y, z);
        } else {
            throw new NullPointerException("World not found for a shrine location.");
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("owner_id", ownerMojangId);
        map.put("deity", deity.getName());
        map.put("type", shrine.name());
        map.put("world_name", location.getWorld().getName());
        map.put("x", location.getBlockX());
        map.put("y", location.getBlockY());
        map.put("z", location.getBlockZ());
        return map;
    }

    public Family getFaction() {
        return DGData.PLAYER_R.fromId(ownerMojangId).getFamily();
    }

    public String getOwnerMojangId() {
        return ownerMojangId;
    }

    public Deity getDeity() {
        return deity;
    }

    public Shrine getShrineType() {
        return shrine;
    }

    public Location getLocation() {
        return location;
    }

    public Location getClickable() {
        return getShrineType().getClickable(location);
    }

    public Location getSafeTeleport() {
        return getShrineType().getSafeTeleport(location);
    }

    @Override
    public String getPersistentId() {
        return shrineId;
    }
}
