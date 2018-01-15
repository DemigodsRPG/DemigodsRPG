package com.demigodsrpg.ability;

import com.demigodsrpg.aspect.Aspect;
import org.bukkit.OfflinePlayer;

import java.util.List;

public interface AbilityCasterProvider {
    AbilityCaster fromPlayer(OfflinePlayer player);

    List<? extends AbilityCaster> fromAspect(Aspect aspect);
}
