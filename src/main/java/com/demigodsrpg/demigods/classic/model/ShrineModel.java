package com.demigodsrpg.demigods.classic.model;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.deity.Deity;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.shrine.Shrine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShrineModel extends AbstractPersistentModel<String> {
    private String shrineId;
    private UUID ownerMojangId;
    private Deity deity;
    private Shrine shrine;
    private Location location;

    public ShrineModel(String name, Player player, Deity deity, Shrine shrine, Location location) {
        shrineId = name;
        ownerMojangId = player.getUniqueId();
        this.deity = deity;
        this.shrine = shrine;
        this.location = location;
    }

    public ShrineModel(String shrineId, ConfigurationSection conf) {
        this.shrineId = shrineId;

        ownerMojangId = UUID.fromString(conf.getString("ownerId"));
        deity = Deity.valueOf(conf.getString("deity"));
        shrine = Shrine.valueOf(conf.getString("type"));

        World world = Bukkit.getWorld(conf.getString("world-name"));
        if (world != null) {
            double x = conf.getDouble("x");
            double y = conf.getDouble("y");
            double z = conf.getDouble("z");
            float yaw = Float.valueOf(conf.getString("yaw"));
            float pitch = Float.valueOf(conf.getString("pitch"));
            location = new Location(world, x, y, z, yaw, pitch);
        }

        throw new NullPointerException("World not found for a shrine location.");
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("ownerId", ownerMojangId.toString());
        map.put("deity", deity.name());
        map.put("type", shrine);
        map.put("world-name", location.getWorld().getName());
        map.put("x", location.getX());
        map.put("y", location.getY());
        map.put("z", location.getZ());
        map.put("yaw", location.getYaw());
        map.put("pitch", location.getPitch());
        return map;
    }

    public IDeity.Alliance getAlliance() {
        return DGClassic.PLAYER_R.fromId(ownerMojangId).getAlliance();
    }

    public UUID getOwnerMojangId() {
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

    @Override
    public String getPersistantId() {
        return shrineId;
    }
}
