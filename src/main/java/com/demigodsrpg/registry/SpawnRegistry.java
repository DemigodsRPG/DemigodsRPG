package com.demigodsrpg.registry;

import com.demigodsrpg.family.Family;
import com.demigodsrpg.model.SpawnModel;
import com.demigodsrpg.util.datasection.DataSection;
import com.demigodsrpg.util.datasection.Registry;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Optional;

public interface SpawnRegistry extends Registry<SpawnModel> {
    String NAME = "spawns";

    default Location getSpawn(final Family family) {
        Optional<SpawnModel> point =
                getRegisteredData().values().stream().filter(model -> model.getAlliance().equals(family)).findAny();
        if (!point.isPresent()) {
            return Bukkit.getWorlds().get(0).getSpawnLocation();
        }
        return point.get().getLocation();
    }

    @Override
    default SpawnModel fromDataSection(String stringKey, DataSection data) {
        // FIXME return better faction
        return new SpawnModel(Family.NEUTRAL, data);
    }
}
