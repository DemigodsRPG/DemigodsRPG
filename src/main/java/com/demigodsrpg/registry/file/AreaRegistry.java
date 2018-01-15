package com.demigodsrpg.registry.file;


import com.demigodsrpg.area.*;
import com.demigodsrpg.util.datasection.DataSection;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.stream.Collectors;

public class AreaRegistry extends AbstractConfigRegistry<Area> {
    private final World WORLD;

    public AreaRegistry(World world) {
        super(world.getName() + ".areas");
        WORLD = world;
    }

    public World getWorld() {
        return WORLD;
    }

    @Override
    public Area fromDataSection(String key, DataSection section) {
        String areaType = key.split("\\$")[0];
        if ("faction".equals(areaType)) {
            return new FamilyTerritory(key, section);
        } else if ("claimroom".equals(areaType)) {
            return new ClaimRoom(key, section);
        }
        throw new NullPointerException("There is no area of type \"" + areaType + ".\"");
    }

    public List<Area> fromLocation(final Location location) {
        return REGISTERED_DATA.asMap().values().parallelStream().filter(area -> area.contains(location))
                .collect(Collectors.toList());
    }
}

