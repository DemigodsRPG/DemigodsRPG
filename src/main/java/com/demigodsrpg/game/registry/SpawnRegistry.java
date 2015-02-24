package com.demigodsrpg.game.registry;

import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.model.SpawnModel;
import com.demigodsrpg.game.util.JsonSection;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Optional;

public class SpawnRegistry extends AbstractRegistry<SpawnModel> {
    private static final String FILE_NAME = "spawns.dgdat";

    public Location getSpawn(final Faction alliance) {
        Optional<SpawnModel> point = getRegistered().stream().filter(model -> model.getAlliance().equals(alliance)).findAny();
        if (!point.isPresent()) {
            return Bukkit.getWorlds().get(0).getSpawnLocation();
        }
        return point.get().getLocation();
    }

    @Override
    public SpawnModel valueFromData(String stringKey, JsonSection data) {
        // FIXME return better faction
        return new SpawnModel(Faction.NEUTRAL, data);
    }

    @Override
    public String getFileName() {
        return FILE_NAME;
    }
}
