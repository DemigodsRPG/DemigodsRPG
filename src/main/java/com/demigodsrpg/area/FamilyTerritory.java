package com.demigodsrpg.area;

import com.demigodsrpg.DGData;
import com.demigodsrpg.family.Family;
import com.demigodsrpg.util.LocationUtil;
import com.demigodsrpg.util.datasection.DataSection;
import org.bukkit.Location;

import java.util.*;
import java.util.stream.Collectors;

public class FamilyTerritory extends Area {
    private final String uuid;
    private final Family family;

    public FamilyTerritory(Family family, AreaPriority priority, List<Location> corners) {
        super(priority, corners);
        this.uuid = UUID.randomUUID().toString();
        this.family = family;
    }

    public FamilyTerritory(String id, final DataSection conf) {
        super(AreaPriority.valueOf(conf.getString("priority")), new ArrayList<Location>() {{
            addAll(conf.getStringList("locations").stream().map(LocationUtil::locationFromString)
                    .collect(Collectors.toList()));
        }});
        this.uuid = id.split("\\$")[2];
        this.family = DGData.getFamily(id.split("\\$")[1]);
    }

    public Family getFamily() {
        return family;
    }

    @Override
    public String getKey() {
        return "family$" + getFamily().getName() + "$" + uuid;
    }
}
