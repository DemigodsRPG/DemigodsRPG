package com.demigodsrpg.demigods.classic.registry;

import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.territory.Territory;
import com.demigodsrpg.demigods.classic.util.JsonSection;
import org.bukkit.World;

public class TerritoryRegistry extends AbstractRegistry<Territory> {
    private final World WORLD;
    private final String FILE_NAME;

    public TerritoryRegistry(World world) {
        WORLD = world;
        FILE_NAME = world.getName() + ".territories.dgc";
    }

    public World getWorld() {
        return WORLD;
    }

    @Override
    public Territory valueFromData(String stringKey, JsonSection data) {
        return new Territory(IDeity.Alliance.valueOf(stringKey), data);
    }

    @Override
    public String getFileName() {
        return FILE_NAME;
    }
}

