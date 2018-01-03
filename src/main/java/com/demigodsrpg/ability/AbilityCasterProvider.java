package com.demigodsrpg.ability;

import com.demigodsrpg.aspect.Aspect;
import org.bukkit.entity.Player;

import java.util.List;

public interface AbilityCasterProvider {
    AbilityCaster fromPlayer(Player player);

    List<? extends AbilityCaster> fromAspect(Aspect aspect);
}
