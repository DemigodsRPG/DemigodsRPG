package com.demigodsrpg.game.area;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.deity.Deity;
import com.demigodsrpg.game.util.JsonSection;
import com.demigodsrpg.game.util.LocationUtil;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3f;
import com.google.common.base.Optional;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;
import java.util.stream.Collectors;

public class ClaimRoom extends Area {
    private final String uuid;
    private final Deity deity;
    private Location nextLocation;
    private Vector3f nextRotation;

    public ClaimRoom(Deity deity, AreaPriority priority, List<Location> corners) {
        super(priority, corners);
        this.uuid = UUID.randomUUID().toString();
        this.deity = deity;
    }

    public ClaimRoom(String id, JsonSection conf) {
        super(AreaPriority.valueOf(conf.getString("priority")), new ArrayList<Location>() {{
            addAll(conf.getStringList("locations").stream().map(LocationUtil::locationFromString).collect(Collectors.toList()));
        }});
        this.uuid = id.split("\\$")[2];
        this.deity = DGGame.DEITY_R.deityFromName(id.split("\\$")[1]);

        // Load next location if it exists
        if (conf.getSection("next-location") != null) {
            JsonSection next = conf.getSection("next-location");
            Optional<World> world = DGGame.SERVER.getWorld(next.getString("world"));

            // If the world doesn't exist anymore, the next location is invalid
            if (world.isPresent()) {
                double x = next.getDouble("x");
                double y = next.getDouble("y");
                double z = next.getDouble("z");
                nextLocation = new Location(world.get(), new Vector3d(x, y, z));

                float yaw = Float.valueOf(next.getString("yaw"));
                float pitch = Float.valueOf(next.getString("pitch"));
                nextRotation = new Vector3f(yaw, pitch, 0F);
            } else {
                DGGame.CONSOLE.error("The claim room with id " + uuid + " has an invalid next location.");
            }
        }
    }

    public void setNextLocation(Location nextLocation) {
        this.nextLocation = nextLocation;
    }

    public void setNextRotation(Vector3f nextRotation) {
        this.nextRotation = nextRotation;
    }

    public Location getNextLocation() {
        return nextLocation;
    }

    public Vector3f getNextRotation() {
        return nextRotation;
    }

    @Override
    public String getPersistentId() {
        return "claimroom$" + deity.getName() + "$" + uuid;
    }

    @Override
    public Map<String, Object> serialize() {
        // Save next location
        Map<String, Object> next = new HashMap<>();
        next.put("world", ((World) nextLocation.getExtent()).getName());
        next.put("x", nextLocation.getPosition().getX());
        next.put("y", nextLocation.getPosition().getY());
        next.put("z", nextLocation.getPosition().getZ());
        next.put("yaw", nextRotation.getX());
        next.put("pitch", nextRotation.getY());

        // Add to existing serialized map
        Map<String, Object> map = super.serialize();
        map.put("next-location", next);
        return map;
    }

    public Deity getDeity() {
        return deity;
    }
}
