package com.demigodsrpg.game.registry;

import com.demigodsrpg.game.model.ShrineModel;
import com.demigodsrpg.game.util.JsonSection;
import org.spongepowered.api.world.Location;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ShrineRegistry extends AbstractRegistry<ShrineModel> {
    private static final String FILE_NAME = "shrines.dgdat";

    public Collection<ShrineModel> getShrines(final Location location, final int range) {
        return getRegistered().stream().filter(model -> model.getLocation().getExtent().equals(location.getExtent()) && model.getLocation().getPosition().distance(location.getPosition()) <= range).collect(Collectors.toList());
    }

    public ShrineModel getShrine(final Location location) {
        Optional<ShrineModel> model = getRegistered().stream().filter(new Predicate<ShrineModel>() {
            @Override
            public boolean test(ShrineModel model) {
                return model.getShrineType().getLocations(model.getLocation()).contains(location);
            }
        }).findAny();

        if (model.isPresent()) {
            return model.get();
        }
        return null;
    }

    public void generate() {
        for (ShrineModel model : getRegistered()) {
            model.getShrineType().generate(model.getLocation());
        }
    }

    @Override
    public ShrineModel valueFromData(String stringKey, JsonSection data) {
        return new ShrineModel(stringKey, data);
    }

    @Override
    public String getFileName() {
        return FILE_NAME;
    }
}
