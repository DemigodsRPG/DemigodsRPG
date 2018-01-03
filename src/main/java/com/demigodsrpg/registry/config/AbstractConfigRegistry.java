package com.demigodsrpg.registry.config;

import com.demigodsrpg.registry.AbstractDemigodsDataRegistry;
import com.demigodsrpg.util.datasection.AbstractPersistentModel;

public abstract class AbstractConfigRegistry<T extends AbstractPersistentModel<String>> extends AbstractDemigodsDataRegistry<T> {
    @Override
    protected String getExtention() {
        return ".dgcfg";
    }
}
