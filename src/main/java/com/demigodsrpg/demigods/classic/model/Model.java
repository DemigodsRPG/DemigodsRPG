package com.demigodsrpg.demigods.classic.model;

public interface Model<P> {
    Type getType();

    P getPersistantId();

    // -- ENUMS -- //
    public enum Type {
        TRANSIENT, PERSISTENT
    }
}
