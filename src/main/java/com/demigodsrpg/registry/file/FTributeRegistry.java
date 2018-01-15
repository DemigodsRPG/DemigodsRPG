package com.demigodsrpg.registry.file;

import com.demigodsrpg.model.TributeModel;
import com.demigodsrpg.registry.TributeRegistry;

public class FTributeRegistry extends AbstractDemigodsFileRegistry<TributeModel> implements TributeRegistry {
    public FTributeRegistry() {
        super(NAME, 0);
    }
}
