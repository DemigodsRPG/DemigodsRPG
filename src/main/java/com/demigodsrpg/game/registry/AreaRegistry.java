package com.demigodsrpg.game.registry;


import com.demigodsrpg.game.area.Area;
import com.demigodsrpg.game.area.ClaimRoom;
import com.demigodsrpg.game.area.FactionTerritory;
import com.demigodsrpg.game.util.JsonSection;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.stream.Collectors;


public class AreaRegistry extends AbstractRegistry<Area> {
    private final World WORLD;
    private final String FILE_NAME;

    public AreaRegistry(World world) {
        WORLD = world;
        FILE_NAME = world.getName() + ".areas.dgcfg";
    }

    public World getWorld() {
        return WORLD;
    }

    @Override
    public Area valueFromData(String stringKey, JsonSection data) {
        String areaType = stringKey.split("\\$")[0];
        if ("faction".equals(areaType)) {
            return new FactionTerritory(stringKey, data);
        } else if ("claimroom".equals(areaType)) {
            return new ClaimRoom(stringKey, data);
        }
        throw new NullPointerException("There is no area of type \"" + areaType + ".\"");
    }

    @Override
    public String getFileName() {
        return FILE_NAME;
    }

    public List<Area> fromLocation(final Location location) {
        return getRegistered().stream().filter(area -> area.contains(location)).collect(Collectors.toList());
    }

    @Override
    protected boolean isPretty() {
        return true;
    }
}

