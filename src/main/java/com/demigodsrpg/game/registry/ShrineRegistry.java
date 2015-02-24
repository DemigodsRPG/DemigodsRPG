package com.demigodsrpg.game.registry;

import com.censoredsoftware.library.schematic.Point;
import com.demigodsrpg.game.model.ShrineModel;
import com.demigodsrpg.game.shrine.ShrineWorld;
import com.demigodsrpg.game.util.JsonSection;
import org.bukkit.Location;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ShrineRegistry extends AbstractRegistry<ShrineModel> {
    private static final String FILE_NAME = "shrines.dgdat";

    public Collection<ShrineModel> getShrines(final Location location, final int range) {
        return getRegistered().stream().filter(model -> model.getLocation().getWorld().equals(location.getWorld()) && model.getLocation().distance(location) <= range).collect(Collectors.toList());
    }

    public ShrineModel getShrine(final Location location) {
        Optional<ShrineModel> model = getRegistered().stream().filter(new Predicate<ShrineModel>() {
            @Override
            public boolean test(ShrineModel model) {
                Point point = new Point(location.getBlockX(), location.getBlockY(), location.getBlockZ(), new ShrineWorld(location.getWorld()));
                return model.getShrineType().getLocations(model.getPoint()).contains(point);
            }
        }).findAny();

        if (model.isPresent()) {
            return model.get();
        }
        return null;
    }

    public void generate() {
        for (ShrineModel model : getRegistered()) {
            model.getShrineType().generate(model.getPoint());
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