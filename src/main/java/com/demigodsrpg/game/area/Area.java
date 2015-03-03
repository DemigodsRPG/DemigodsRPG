package com.demigodsrpg.game.area;

import com.demigodsrpg.game.model.AbstractPersistentModel;
import com.demigodsrpg.game.util.LocationUtil;
import org.spongepowered.api.world.Location;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Area extends AbstractPersistentModel<String> {
    protected final AreaPriority priority;
    protected final List<Location> corners;
    protected final transient Polygon shape;

    public Area(AreaPriority priority, List<Location> corners) {
        this.priority = priority;

        int[] x = new int[corners.size()];
        int[] z = new int[corners.size()];

        for (int i = 0; i < corners.size(); i++) {
            x[i] = corners.get(i).getBlock().getX();
            z[i] = corners.get(i).getBlock().getZ();
        }

        this.corners = corners;
        shape = new Polygon(x, z, corners.size());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("priority", priority.name());
        java.util.List<String> locStrings = corners.stream().map(LocationUtil::stringFromLocation).collect(Collectors.toList());
        map.put("locations", locStrings);
        return map;
    }

    public boolean contains(Location location) {
        return shape.contains(location.getPosition().getX(), location.getPosition().getZ());
    }

    public List<Location> getCorners() {
        return corners;
    }

    public AreaPriority getPriority() {
        return priority;
    }
}
