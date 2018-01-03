package com.demigodsrpg.util.datasection;

import java.util.Map;

public abstract class AbstractPersistentModel<P> implements Model<P> {
    @Override
    public Type getType() {
        return Type.PERSISTENT;
    }

    public abstract P getPersistentId();

    public abstract Map<String, Object> serialize();
}
