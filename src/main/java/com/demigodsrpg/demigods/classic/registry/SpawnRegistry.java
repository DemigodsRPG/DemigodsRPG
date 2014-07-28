package com.demigodsrpg.demigods.classic.registry;

import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.model.SpawnModel;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class SpawnRegistry extends AbstractDirectRegistry<IDeity.Alliance, SpawnModel> {
    public static final String FILE_NAME = "spawns.dgc";

    public Location getSpawn(final IDeity.Alliance alliance) {
        SpawnModel point = Iterables.find(getRegistered(), new Predicate<SpawnModel>() {
            @Override
            public boolean apply(SpawnModel spawnPoint) {
                return spawnPoint.getAlliance().equals(alliance);
            }
        }, null);
        if (point == null) {
            return Bukkit.getWorlds().get(0).getSpawnLocation();
        }
        return point.getLocation();
    }

    @Override
    public IDeity.Alliance keyFromString(String stringKey) {
        return IDeity.Alliance.valueOf(stringKey);
    }

    @Override
    public SpawnModel valueFromData(String stringKey, ConfigurationSection data) {
        return new SpawnModel(keyFromString(stringKey), data);
    }

    @Override
    public String getFileName() {
        return FILE_NAME;
    }
}
