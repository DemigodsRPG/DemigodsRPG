package com.demigodsrpg.demigods.classic.territory;

import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.model.AbstractPersistentModel;
import com.demigodsrpg.demigods.classic.util.JsonSection;
import com.demigodsrpg.demigods.classic.util.LocationUtil;
import org.bukkit.Location;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Territory extends AbstractPersistentModel<String> {
    private final IDeity.Alliance alliance;
    private final Priority priority;
    private final List<Location> corners;
    private final Polygon shape;

    private Territory(IDeity.Alliance alliance, Priority priority, java.util.List<Location> corners) {
        this.alliance = alliance;
        this.priority = priority;
        int[] x = new int[corners.size()];
        int[] z = new int[corners.size()];

        for (int i = 0; i < corners.size(); i++) {
            x[i] = corners.get(i).getBlockX();
            z[i] = corners.get(i).getBlockZ();
        }

        this.corners = corners;
        shape = new Polygon(x, z, corners.size());
    }

    public Territory(IDeity.Alliance alliance, final JsonSection conf) {
        this(alliance, Priority.valueOf(conf.getString("priority")), new ArrayList<Location>() {
            {
                for (String locString : conf.getStringList("locations")) {
                    add(LocationUtil.locationFromString(locString));
                }
            }
        });
    }

    public boolean contains(Location location) {
        return shape.contains(location.getX(), location.getZ());
    }

    IDeity.Alliance getAlliance() {
        return alliance;
    }

    public java.util.List<Location> getCorners() {
        return corners;
    }

    public Priority getPriority() {
        return priority;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("priority", priority.name());
        java.util.List<String> locStrings = new ArrayList<>();
        for (Location location : corners) {
            locStrings.add(LocationUtil.stringFromLocation(location));
        }
        map.put("locations", locStrings);
        return map;
    }

    @Override
    public String getPersistentId() {
        return getAlliance().name();
    }

    public enum Priority {
        LOW, NORMAL, HIGH
    }
}
