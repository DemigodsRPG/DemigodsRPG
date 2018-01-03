package com.demigodsrpg.registry;

import com.demigodsrpg.family.Family;
import com.demigodsrpg.model.SpawnModel;
import com.demigodsrpg.util.datasection.DataSection;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Optional;

public class SpawnRegistry extends AbstractDemigodsDataRegistry<SpawnModel> {
    private static final String FILE_NAME = "spawns";

    public Location getSpawn(final Family family) {
        Optional<SpawnModel> point = getRegistered().stream().filter(model -> model.getAlliance().equals(family)).findAny();
        if (!point.isPresent()) {
            return Bukkit.getWorlds().get(0).getSpawnLocation();
        }
        return point.get().getLocation();
    }

    @Override
    public SpawnModel valueFromData(String stringKey, DataSection data) {
        // FIXME return better faction
        return new SpawnModel(Family.NEUTRAL, data);
    }

    @Override
    public String getName() {
        return FILE_NAME;
    }
}
