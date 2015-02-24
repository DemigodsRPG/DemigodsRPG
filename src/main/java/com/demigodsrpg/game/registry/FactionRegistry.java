package com.demigodsrpg.game.registry;

import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.util.JsonSection;

public class FactionRegistry extends AbstractRegistry<Faction> {

    private static final String FILE_NAME = "factions.dgcfg";

    public Faction factionFromName(final String name) {
        return getRegistered().stream().filter(faction -> faction.getName().equals(name)).findAny().get();
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
