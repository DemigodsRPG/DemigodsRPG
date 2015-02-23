package com.demigodsrpg.game.registry;

import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.model.SpawnModel;
import com.demigodsrpg.game.util.JsonSection;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SpawnRegistry extends AbstractRegistry<SpawnModel> {
    private static final String FILE_NAME = "spawns.dgc";

    public Location getSpawn(final Faction alliance) {
        SpawnModel point = getRegistered().stream().filter(model -> model.getAlliance().equals(alliance)).findAny().get();
        if (point == null) {
            return Bukkit.getWorlds().get(0).getSpawnLocation();
        }
        return point.getLocation();
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
