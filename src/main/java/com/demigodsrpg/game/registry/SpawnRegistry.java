package com.demigodsrpg.game.registry;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.model.SpawnModel;
import com.demigodsrpg.game.util.JsonSection;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.world.Location;

import java.util.Optional;

public class SpawnRegistry extends AbstractRegistry<SpawnModel> {
    private static final String FILE_NAME = "spawns.dgdat";

    public Location getSpawn(final Faction alliance) {
        Optional<SpawnModel> point = getRegistered().stream().filter(model -> model.getAlliance().equals(alliance)).findAny();
        if (!point.isPresent()) {
            return new Location(DGGame.SERVER.getWorlds().stream().findFirst().get(), new Vector3d(0, 0, 0)); // FIXME Can't get the default spawn location
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
