package com.demigodsrpg.registry;

import com.demigodsrpg.DGData;
import com.demigodsrpg.Setting;
import com.demigodsrpg.util.datasection.AbstractDataRegistry;
import com.demigodsrpg.util.datasection.AbstractPersistentModel;

public abstract class AbstractDemigodsDataRegistry<T extends AbstractPersistentModel<String>> extends AbstractDataRegistry<T> {
    public AbstractDemigodsDataRegistry() {
        super(DGData.SAVE_PATH, Setting.SAVE_PRETTY, Setting.PSQL_PERSISTENCE, Setting.PSQL_CONNECTION);
    }
}
