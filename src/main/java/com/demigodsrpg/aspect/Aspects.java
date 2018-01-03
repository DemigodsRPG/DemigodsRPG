package com.demigodsrpg.aspect;

import com.demigodsrpg.aspect.bloodlust.*;
import com.demigodsrpg.aspect.crafting.CraftingAspectI;
import com.demigodsrpg.aspect.demon.*;
import com.demigodsrpg.aspect.fire.FireAspectI;
import com.demigodsrpg.aspect.lightning.LightningAspectI;
import com.demigodsrpg.aspect.lightning.LightningAspectII;
import com.demigodsrpg.aspect.magnetism.MagnetismAspectHero;
import com.demigodsrpg.aspect.water.WaterAspectHero;
import com.demigodsrpg.aspect.water.WaterAspectI;

public class Aspects {
    // -- PUBLIC RELEASE -- //

    public static final MagnetismAspectHero MAGNETISM_ASPECT_HERO = new MagnetismAspectHero();

    public static final LightningAspectI LIGHTNING_ASPECT_I = new LightningAspectI();
    public static final LightningAspectII LIGHTNING_ASPECT_II = new LightningAspectII();

    public static final FireAspectI FIRE_ASPECT_I = new FireAspectI();

    public static final WaterAspectHero WATER_ASPECT_HERO = new WaterAspectHero();
    public static final WaterAspectI WATER_ASPECT_I = new WaterAspectI();

    public static final BloodlustAspectHero BLOODLUST_ASPECT_HERO = new BloodlustAspectHero();
    public static final BloodlustAspectI BLOODLUST_ASPECT_I = new BloodlustAspectI();
    public static final BloodlustAspectII BLOODLUST_ASPECT_II = new BloodlustAspectII();
    public static final BloodlustAspectIII BLOODLUST_ASPECT_III = new BloodlustAspectIII();

    public static final CraftingAspectI CRAFTING_ASPECT_I = new CraftingAspectI();

    // -- EXPANSION 1 -- //

    public static final DemonAspectHero DEMON_ASPECT_HERO = new DemonAspectHero();
    public static final DemonAspectI DEMON_ASPECT_I = new DemonAspectI();
    public static final DemonAspectII DEMON_ASPECT_II = new DemonAspectII();


    // -- ASPECT LIST -- //

    private static final Aspect[] aspectList = new Aspect[]{
            // Magnetism Aspect
            MAGNETISM_ASPECT_HERO,

            // Lightning Aspect
            LIGHTNING_ASPECT_I, LIGHTNING_ASPECT_II,

            // Fire Aspect
            FIRE_ASPECT_I,

            // Water Aspect
            WATER_ASPECT_HERO, WATER_ASPECT_I,

            // Bloodlust Aspect
            BLOODLUST_ASPECT_HERO, BLOODLUST_ASPECT_I, BLOODLUST_ASPECT_II, BLOODLUST_ASPECT_III,

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
        if (name != null) {
            for (Aspect aspect : aspectList) {
                if (aspect.name().equalsIgnoreCase(name)) {
                    return aspect;
                }
            }
        }
        return null;
    }

    public static Aspect fromId(int id) {
        for (Aspect aspect : aspectList) {
            if (aspect.getId() == id) {
                return aspect;
            }
        }
        return null;
    }
}
