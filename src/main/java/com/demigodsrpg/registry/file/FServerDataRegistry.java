package com.demigodsrpg.registry.file;

import com.demigodsrpg.model.ServerDataModel;
import com.demigodsrpg.registry.ServerDataRegistry;

public class FServerDataRegistry extends AbstractDemigodsFileRegistry<ServerDataModel> implements ServerDataRegistry {
    public FServerDataRegistry() {
        super(NAME, 0);
    }
}
