package com.demigodsrpg.demigods.classic.model;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.deity.Deity;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.shrine.Shrine;
import com.demigodsrpg.demigods.classic.util.JsonSection;
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
    private Location location;

    public ShrineModel(String name, Player player, Deity deity, Shrine shrine, Location location) {
        shrineId = name;
        ownerMojangId = player.getUniqueId().toString();
        this.deity = deity;
        this.shrine = shrine;
        this.location = location;
    }

    public ShrineModel(String shrineId, JsonSection conf) {
        this.shrineId = shrineId;
        ownerMojangId = conf.getString("owner_id");
        deity = Deity.valueOf(conf.getString("deity"));
        shrine = Shrine.valueOf(conf.getString("type"));

        World world = Bukkit.getWorld(conf.getString("world_name"));
        if (world != null) {
            double x = conf.getDouble("x");
            double y = conf.getDouble("y");
            double z = conf.getDouble("z");
            float yaw = Float.valueOf(conf.getString("yaw"));
            float pitch = Float.valueOf(conf.getString("pitch"));
            location = new Location(world, x, y, z, yaw, pitch);
        } else {
            throw new NullPointerException("World not found for a shrine location.");
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("owner_id", ownerMojangId);
        map.put("deity", deity.name());
        map.put("type", shrine.name());
        map.put("world_name", location.getWorld().getName());
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
