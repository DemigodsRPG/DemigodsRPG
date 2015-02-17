package com.demigodsrpg.game.aspect;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.aspect.i.LightningAspectI;
import com.demigodsrpg.game.aspect.ii.LightningAspectII;
import com.demigodsrpg.game.aspect.iii.LightningAspectIII;
import org.bukkit.entity.Player;

public class Aspects {

    // -- TIER I -- //

    public static final LightningAspectI LIGHTNING_ASPECT_I = new LightningAspectI();

    // -- TIER II -- //

    public static final LightningAspectII LIGHTNING_ASPECT_II = new LightningAspectII();

    // -- TIER III -- //

    public static final LightningAspectIII LIGHTNING_ASPECT_III = new LightningAspectIII();

    // -- ASPECT LIST -- //

    private static final Aspect[] aspectList = new Aspect[]{
            LIGHTNING_ASPECT_I, LIGHTNING_ASPECT_II, LIGHTNING_ASPECT_III
    };

    // -- PRIVATE CONSTRUCTOR -- //

    private Aspects() {
    }

    // -- HELPER METHODS -- //

    public static Aspect[] values() {
        return aspectList;
    }

    public static Aspect valueOf(final String name) {
        for (Aspect aspect : aspectList) {
            if (aspect.name().equals(name)) {
                return aspect;
            }
        }
        return null;
    }

    public static boolean hasAspect(Player player, Aspect aspect) {
        return DGGame.PLAYER_R.fromPlayer(player).hasAspect(aspect);
    }
}
