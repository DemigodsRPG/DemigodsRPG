package com.demigodsrpg.demigods.classic.model;

import java.util.Map;

public abstract class AbstractPersistentModel<P> implements Model<P> {
    @Override
    public Type getType() {
        return Type.PERSISTENT;
    }

    public abstract Map<String, Object> serialize();
}
