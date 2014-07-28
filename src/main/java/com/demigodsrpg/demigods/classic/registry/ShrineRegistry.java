package com.demigodsrpg.demigods.classic.registry;

import com.demigodsrpg.demigods.classic.model.ShrineModel;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;

public class ShrineRegistry extends AbstractRegistry<String, ShrineModel> {
    public static final String FILE_NAME = "shrines.dgc";

    public Collection<ShrineModel> getShrines(final Location location, final int range) {
        return Collections2.filter(getRegistered(), new Predicate<ShrineModel>() {
            @Override
            public boolean apply(ShrineModel shrineModel) {
                return shrineModel.getLocation().getWorld().equals(location.getWorld()) && shrineModel.getLocation().distance(location) <= range;
            }
        });
    }

    public ShrineModel getShrine(final Location location) {
        return Iterables.find(getRegistered(), new Predicate<ShrineModel>() {
            @Override
            public boolean apply(ShrineModel shrineModel) {
                return shrineModel.getShrineType().getLocations(shrineModel.getLocation()).contains(location);
            }
        }, null);
    }

    @Override
    public String keyFromString(String stringKey) {
        return stringKey;
    }

    @Override
    public ShrineModel valueFromData(String stringKey, ConfigurationSection data) {
        return new ShrineModel(stringKey, data);
    }

    @Override
    public String getFileName() {
        return FILE_NAME;
    }
}
