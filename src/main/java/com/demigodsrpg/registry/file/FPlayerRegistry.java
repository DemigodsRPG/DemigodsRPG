package com.demigodsrpg.registry.file;

import com.demigodsrpg.model.PlayerModel;
import com.demigodsrpg.registry.PlayerRegistry;

public class FPlayerRegistry extends AbstractDemigodsFileRegistry<PlayerModel> implements PlayerRegistry {
    public FPlayerRegistry() {
        super(NAME, 0);
    }
}
