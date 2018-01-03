package com.demigodsrpg.registry.config;

import com.demigodsrpg.deity.Deity;
import com.demigodsrpg.family.Family;
import com.demigodsrpg.util.datasection.DataSection;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DeityRegistry extends AbstractConfigRegistry<Deity> {
    private static final String FILE_NAME = "deities";

    public Deity deityFromName(String name) {
        Optional<Deity> found = getRegistered().stream().filter(deity -> deity.getName().equalsIgnoreCase(name)).findAny();
        if (found.isPresent()) {
            return found.get();
        }
        return null;
    }

    public List<Deity> deitiesInFamily(Family family) {
        return getRegistered().stream().filter(deity -> deity.getFamilies().contains(family)).collect(Collectors.toList());
    }

    @Override
    protected Deity valueFromData(String stringKey, DataSection data) {
        return new Deity(stringKey, data);
    }

    @Override
    protected String getName() {
        return FILE_NAME;
    }

    @Override
    protected boolean isPretty() {
        return true;
    }
}
