package com.demigodsrpg.area;

import com.demigodsrpg.DGData;
import com.demigodsrpg.deity.Deity;
import com.demigodsrpg.util.LocationUtil;
import com.demigodsrpg.util.datasection.DataSection;
import org.bukkit.*;

import java.util.*;
import java.util.stream.Collectors;

public class ClaimRoom extends Area {
    private final String uuid;
    private final Deity deity;
    private Location nextLocation;

    public ClaimRoom(Deity deity, AreaPriority priority, List<Location> corners) {
        super(priority, corners);
        this.uuid = UUID.randomUUID().toString();
        this.deity = deity;
    }

    public ClaimRoom(String id, DataSection conf) {
        super(AreaPriority.valueOf(conf.getString("priority")), new ArrayList<Location>() {{
            addAll(conf.getStringList("locations").stream().map(LocationUtil::locationFromString)
                    .collect(Collectors.toList()));
        }});
        this.uuid = id.split("\\$")[2];
        this.deity = DGData.getDeity(id.split("\\$")[1]);

        // Load next location if it exists
        if (conf.getSectionNullable("next-location") != null) {
            DataSection next = conf.getSectionNullable("next-location");
            World world = Bukkit.getWorld(next.getString("world"));

            // If the world doesn't exist anymore, the next location is invalid
            if (world != null) {
                double x = next.getDouble("x");
                double y = next.getDouble("y");
                double z = next.getDouble("z");
                float yaw = Float.parseFloat(next.getString("yaw"));
                float pitch = Float.parseFloat(next.getString("pitch"));
                nextLocation = new Location(world, x, y, z, yaw, pitch);
            } else {
                DGData.CONSOLE.warning("The claim room with id " + uuid + " has an invalid next location.");
            }
        }
    }

    public void setNextLocation(Location nextLocation) {
        this.nextLocation = nextLocation;
    }

    public Location getNextLocation() {
        return nextLocation;
    }

    @Override
    public String getKey() {
        return "claimroom$" + deity.getName() + "$" + uuid;
    }

    @SuppressWarnings("ConstantConditions") // World should be loaded when saving
    @Override
    public Map<String, Object> serialize() {
        // Save next location
        Map<String, Object> next = new HashMap<>();
        next.put("world", nextLocation.getWorld().getName());
        next.put("x", nextLocation.getX());
        next.put("y", nextLocation.getY());
        next.put("z", nextLocation.getZ());
        next.put("yaw", nextLocation.getY());
        next.put("pitch", nextLocation.getPitch());

        // Add to existing serialized map
        Map<String, Object> map = super.serialize();
        map.put("next-location", next);
        return map;
    }

    public Deity getDeity() {
        return deity;
    }
}
