package com.demigodsrpg.game.model;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.util.JsonSection;
import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Optional;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.HashMap;
import java.util.Map;

public class SpawnModel extends AbstractPersistentModel<String> {
    private final Faction alliance;
    private Location location;

    public SpawnModel(Faction alliance, Location location) {
        this.alliance = alliance;
        this.location = location;
    }

    public SpawnModel(Faction alliance, JsonSection conf) {
        this.alliance = alliance;

        Optional<World> world = DGGame.SERVER.getWorld(conf.getString("world_name"));
        if (world.isPresent()) {
            double x = conf.getDouble("x");
            double y = conf.getDouble("y");
            double z = conf.getDouble("z");
            // float yaw = Float.valueOf(conf.getString("yaw")); TODO
            // float pitch = Float.valueOf(conf.getString("pitch")); TODO
            location = new Location(world.get(), new Vector3d(x, y, z));
        } else {
            throw new NullPointerException("World not found for the " + alliance.getName() + " spawn location.");
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("world_name", ((World) location.getExtent()).getName());
        map.put("x", location.getPosition().getX());
        map.put("y", location.getPosition().getY());
        map.put("z", location.getPosition().getZ());
        // map.put("yaw", location.getYaw());
        // map.put("pitch", location.getPitch());
        return map;
    }

    public Faction getAlliance() {
        return alliance;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String getPersistentId() {
        return getAlliance().getName();
    }
}
