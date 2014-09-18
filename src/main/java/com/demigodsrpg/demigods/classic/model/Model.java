package com.demigodsrpg.demigods.classic.model;

public interface Model<P> {
    Type getType();

    // -- ENUMS -- //
    public enum Type {
        TRANSIENT, PERSISTENT
    }
}
