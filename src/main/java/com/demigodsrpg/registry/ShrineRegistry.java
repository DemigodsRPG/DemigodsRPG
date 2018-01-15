package com.demigodsrpg.registry;

import com.demigodsrpg.model.ShrineModel;
import com.demigodsrpg.util.datasection.DataSection;
import com.demigodsrpg.util.datasection.Registry;
import org.bukkit.Location;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public interface ShrineRegistry extends Registry<ShrineModel> {
    String NAME = "shrines";

    default Collection<ShrineModel> getShrines(final Location location, final int range) {
        return getRegisteredData().values().stream()
                .filter(model -> model.getLocation().getWorld().equals(location.getWorld()) &&
                        model.getLocation().distance(location) <= range).collect(Collectors.toList());
    }

    default ShrineModel getShrine(final Location location) {
        Optional<ShrineModel> model = getRegisteredData().values().stream()
                .filter(model1 -> model1.getShrineType().getLocations(model1.getLocation()).contains(location))
                .findAny();
        return model.orElse(null);
    }

    default void generate() {
        loadAllFromDb();
        for (ShrineModel model : getRegisteredData().values()) {
            model.getShrineType().generate(model.getLocation());
        }
    }

    @Override
    default ShrineModel fromDataSection(String stringKey, DataSection data) {
        return new ShrineModel(stringKey, data);
    }
}
