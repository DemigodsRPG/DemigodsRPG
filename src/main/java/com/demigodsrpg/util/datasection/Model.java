package com.demigodsrpg.util.datasection;

public interface Model<P> {
    Type getType();

    // -- ENUMS -- //
    enum Type {
        TRANSIENT, PERSISTENT
    }
}
