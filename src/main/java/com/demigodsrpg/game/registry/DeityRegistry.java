package com.demigodsrpg.game.registry;

import com.demigodsrpg.game.deity.Deity;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.util.JsonSection;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DeityRegistry extends AbstractRegistry<Deity> {
    private static final String FILE_NAME = "deities.dgcfg";

    public Deity deityFromName(String name) {
        Optional<Deity> found = getRegistered().stream().filter(deity -> deity.getName().equalsIgnoreCase(name)).findAny();
        if (found.isPresent()) {
            return found.get();
        }
        return null;
    }

    public List<Deity> deitiesInFaction(Faction faction) {
        return getRegistered().stream().filter(deity -> deity.getFaction().equals(faction)).collect(Collectors.toList());
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
