package com.demigodsrpg.game.model;

import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.util.JsonSection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

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

        World world = Bukkit.getWorld(conf.getString("world_name"));
        if (world != null) {
            double x = conf.getDouble("x");
            double y = conf.getDouble("y");
            double z = conf.getDouble("z");
            float yaw = Float.valueOf(conf.getString("yaw"));
            float pitch = Float.valueOf(conf.getString("pitch"));
            location = new Location(world, x, y, z, yaw, pitch);
        }

        throw new NullPointerException("World not found for the " + alliance.getName() + " spawn location.");
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
