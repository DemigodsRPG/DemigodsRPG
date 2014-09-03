package com.demigodsrpg.demigods.classic.registry;

import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.territory.Territory;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

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
    public Territory valueFromData(String stringKey, ConfigurationSection data) {
        return new Territory(IDeity.Alliance.valueOf(stringKey), data);
    }

    @Override
    public String getFileName() {
        return FILE_NAME;
    }
}

