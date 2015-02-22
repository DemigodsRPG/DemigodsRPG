package com.demigodsrpg.game.aspect;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.aspect.bloodlust.BloodlustAspectI;
import com.demigodsrpg.game.aspect.bloodlust.BloodlustAspectII;
import com.demigodsrpg.game.aspect.bloodlust.BloodlustAspectIII;
import com.demigodsrpg.game.aspect.crafting.CraftingAspectI;
import com.demigodsrpg.game.aspect.demon.DemonAspectI;
import com.demigodsrpg.game.aspect.demon.DemonAspectII;
import com.demigodsrpg.game.aspect.demon.DemonAspectIII;
import com.demigodsrpg.game.aspect.fire.FireAspectI;
import com.demigodsrpg.game.aspect.lightning.LightningAspectI;
import com.demigodsrpg.game.aspect.lightning.LightningAspectII;
import com.demigodsrpg.game.aspect.lightning.LightningAspectIII;
import com.demigodsrpg.game.aspect.water.WaterAspectI;
import com.demigodsrpg.game.aspect.water.WaterAspectII;
import org.bukkit.entity.Player;

public class Aspects {
    // -- PUBLIC RELEASE -- //

    public static final LightningAspectI LIGHTNING_ASPECT_I = new LightningAspectI();
    public static final LightningAspectII LIGHTNING_ASPECT_II = new LightningAspectII();
    public static final LightningAspectIII LIGHTNING_ASPECT_III = new LightningAspectIII();

    public static final FireAspectI FIRE_ASPECT_I = new FireAspectI();

    public static final WaterAspectI WATER_ASPECT_I = new WaterAspectI();
    public static final WaterAspectII WATER_ASPECT_II = new WaterAspectII();

    public static final BloodlustAspectI BLOODLUST_ASPECT_I = new BloodlustAspectI();
    public static final BloodlustAspectII BLOODLUST_ASPECT_II = new BloodlustAspectII();
    public static final BloodlustAspectIII BLOODLUST_ASPECT_III = new BloodlustAspectIII();

    public static final CraftingAspectI CRAFTING_ASPECT_I = new CraftingAspectI();

    // -- EXPANSION 1 -- //

    public static final DemonAspectI DEMON_ASPECT_I = new DemonAspectI();
    public static final DemonAspectII DEMON_ASPECT_II = new DemonAspectII();
    public static final DemonAspectIII DEMON_ASPECT_III = new DemonAspectIII();

    // -- ASPECT LIST -- //

    private static final Aspect[] aspectList = new Aspect[]{
            // Lightning Aspect
            LIGHTNING_ASPECT_I, LIGHTNING_ASPECT_II, LIGHTNING_ASPECT_III,

            // Fire Aspect
            FIRE_ASPECT_I,

            // Water Aspect
            WATER_ASPECT_I, WATER_ASPECT_II,

            // Bloodlust Aspect
            BLOODLUST_ASPECT_I, BLOODLUST_ASPECT_II, BLOODLUST_ASPECT_III,

            // Crafting Aspect
            CRAFTING_ASPECT_I,
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
            if ((aspect.getGroup().getName() + " " + aspect.getTier().name()).equals(name)) {
                return aspect;
            }
        }
        return null;
    }

    public static boolean hasAspect(Player player, Aspect aspect) {
        return DGGame.PLAYER_R.fromPlayer(player).hasAspect(aspect);
    }
}
