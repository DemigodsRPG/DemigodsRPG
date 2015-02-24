package com.demigodsrpg.game.registry;

import com.demigodsrpg.game.deity.Deity;
import com.demigodsrpg.game.util.JsonSection;

import java.util.Optional;

public class DeityRegistry extends AbstractRegistry<Deity> {
    private static final String FILE_NAME = "deities.dgcfg";

    public Deity deityFromName(String name) {
        Optional<Deity> found = getRegistered().stream().filter(deity -> deity.getName().equals(name)).findAny();
        if (found.isPresent()) {
            return found.get();
        }
        return null;
    }

    @Override
    protected Deity valueFromData(String stringKey, JsonSection data) {
        return new Deity(stringKey, data);
    }

    @Override
    protected String getFileName() {
        return FILE_NAME;
    }

    @Override
    protected boolean isPretty() {
        return true;
    }
}
