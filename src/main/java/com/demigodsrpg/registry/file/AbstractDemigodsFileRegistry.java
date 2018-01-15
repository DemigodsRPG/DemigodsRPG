package com.demigodsrpg.registry.file;

import com.demigodsrpg.DGData;
import com.demigodsrpg.Setting;
import com.demigodsrpg.util.datasection.AbstractFileRegistry;
import com.demigodsrpg.util.datasection.Model;

public abstract class AbstractDemigodsFileRegistry<T extends Model> extends AbstractFileRegistry<T> {
    public AbstractDemigodsFileRegistry(String name, int expireInMins) {
        super(DGData.SAVE_PATH, name, Setting.SAVE_PRETTY, expireInMins);
    }
}
