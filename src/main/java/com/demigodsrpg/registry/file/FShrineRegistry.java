package com.demigodsrpg.registry.file;

import com.demigodsrpg.model.ShrineModel;
import com.demigodsrpg.registry.ShrineRegistry;

public class FShrineRegistry extends AbstractDemigodsFileRegistry<ShrineModel> implements ShrineRegistry {
    public FShrineRegistry() {
        super(NAME, 0);
    }
}
