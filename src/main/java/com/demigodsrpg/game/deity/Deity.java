package com.demigodsrpg.game.deity;

import com.demigodsrpg.game.aspect.Aspect;

public interface Deity {
    String getName();

    Faction getFaction();

    boolean isEnabled();

    Aspect.Group getAspectGroup();
}
