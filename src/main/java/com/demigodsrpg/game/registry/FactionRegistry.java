package com.demigodsrpg.game.registry;

import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.util.JsonSection;

import java.util.Optional;

public class FactionRegistry extends AbstractRegistry<Faction> {

    private static final String FILE_NAME = "factions.dgcfg";

    public Faction factionFromName(final String name) {
        Optional<Faction> found = getRegistered().stream().filter(faction -> faction.getName().equalsIgnoreCase(name)).findAny();
        if (found.isPresent()) {
            return found.get();
        }
        return null;
    }

    @Override
    protected Faction valueFromData(String stringKey, JsonSection data) {
        return new Faction(stringKey, data);
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
