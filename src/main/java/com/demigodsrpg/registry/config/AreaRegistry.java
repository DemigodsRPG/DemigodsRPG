package com.demigodsrpg.registry.config;


import com.demigodsrpg.area.*;
import com.demigodsrpg.util.datasection.DataSection;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.stream.Collectors;

public class AreaRegistry extends AbstractConfigRegistry<Area> {
    private final World WORLD;
    private final String FILE_NAME;

    public AreaRegistry(World world) {
        WORLD = world;
        FILE_NAME = world.getName() + ".areas";
    }

    public World getWorld() {
        return WORLD;
    }

    @Override
    public Area valueFromData(String stringKey, DataSection data) {
        String areaType = stringKey.split("\\$")[0];
        if ("faction".equals(areaType)) {
            return new FamilyTerritory(stringKey, data);
        } else if ("claimroom".equals(areaType)) {
            return new ClaimRoom(stringKey, data);
        }
        throw new NullPointerException("There is no area of type \"" + areaType + ".\"");
    }

    @Override
    public String getName() {
        return FILE_NAME;
    }

    public List<Area> fromLocation(final Location location) {
        return getRegistered().parallelStream().filter(area -> area.contains(location)).collect(Collectors.toList());
    }

    @Override
    protected boolean isPretty() {
        return true;
    }
}

