package com.demigodsrpg.registry.file;

import com.demigodsrpg.model.SpawnModel;
import com.demigodsrpg.registry.SpawnRegistry;

public class FSpawnRegistry extends AbstractDemigodsFileRegistry<SpawnModel> implements SpawnRegistry {
    public FSpawnRegistry() {
        super(NAME, 0);
    }
}
