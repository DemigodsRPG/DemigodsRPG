package com.demigodsrpg.registry.file;

import com.demigodsrpg.util.datasection.Model;

public abstract class AbstractConfigRegistry<T extends Model> extends AbstractDemigodsFileRegistry<T> {
    public AbstractConfigRegistry(String name) {
        super(name, 0);
    }
}
