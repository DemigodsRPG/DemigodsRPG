package com.demigodsrpg.demigods.classic.registry;

import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.model.SpawnModel;
import com.demigodsrpg.demigods.classic.util.JsonSection;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SpawnRegistry extends AbstractRegistry<SpawnModel> {
    private static final String FILE_NAME = "spawns.dgc";

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
    public SpawnModel valueFromData(String stringKey, JsonSection data) {
        return new SpawnModel(IDeity.Alliance.valueOf(stringKey), data);
    }

    @Override
    public String getFileName() {
        return FILE_NAME;
    }
}
