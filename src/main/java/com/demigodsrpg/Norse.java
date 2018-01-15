package com.demigodsrpg;

import com.demigodsrpg.aspect.Groups;
import com.demigodsrpg.deity.*;
import com.demigodsrpg.family.Family;
import org.bukkit.ChatColor;

public class Norse implements Pantheon {

    // -- FAMILIES -- //

    public static final Family AESIR = new Family("AEsir", ChatColor.GREEN, "AESIR", "PLACEHOLDER.");
    public static final Family JOTUNN = new Family("Jotunn", ChatColor.YELLOW, "JOTUNN", "PLACEHOLDER.");

    private final Family[] FAMILIES = {};

    // -- DEITIES -- //

    // AESIR

    public static final Deity BALDR = new Deity(DeityType.GOD, "Baldr", Gender.MALE, AESIR); // TODO Speedy fire god
    public static final Deity BRAGI = new Deity(DeityType.GOD, "Bragi", Gender.MALE, AESIR); // TODO Musical god
    public static final Deity HEIMDALLR = new Deity(DeityType.GOD, "Heimdallr", Gender.MALE, AESIR);
    // TODO Flash and cancel damage god
    public static final Deity ODIN = new Deity(DeityType.GOD, "Odin", Gender.MALE, AESIR); // TODO God of time
    public static final Deity THOR = new Deity(DeityType.GOD, "Thor", Gender.MALE, AESIR, Groups.LIGHTNING_ASPECT);
    public static final Deity VIDAR = new Deity(DeityType.GOD, "Vidar", Gender.MALE, AESIR, Groups.BLOODLUST_ASPECT);
    public static final Deity ELF = new Deity(DeityType.HERO, "Elf", Gender.FEMALE, AESIR, Groups.WATER_ASPECT);

    // JOTUNN

    public static final Deity SURTR = new Deity(DeityType.GOD, "Surtr", Gender.MALE, JOTUNN, Groups.FIRE_ASPECT);
    public static final Deity YMIR = new Deity(DeityType.GOD, "Ymir", Gender.MALE, JOTUNN); // TODO Frost giant
    public static final Deity HEL = new Deity(DeityType.GOD, "Hel", Gender.FEMALE, JOTUNN, Groups.DEMON_ASPECT);
    public static final Deity JORD = new Deity(DeityType.GOD, "Jord", Gender.FEMALE, JOTUNN); // TODO Tree spirit
    public static final Deity JORMUNGANDR =
            new Deity(DeityType.GOD, "Jormungandr", Gender.NON_BINARY, JOTUNN, Groups.WATER_ASPECT);
    public static final Deity DWARF = new Deity(DeityType.HERO, "Dwarf", Gender.MALE, JOTUNN, Groups.CRAFTING_ASPECT);
    public static final Deity THYRMR =
            new Deity(DeityType.HERO, "Thrymr", Gender.MALE, JOTUNN, Groups.BLOODLUST_ASPECT);

    private final Deity[] DEITIES = {};

    // -- GETTERS -- //

    @Override
    public String getName() {
        return "Norse";
    }

    @Override
    public Family[] getFamilies() {
        return FAMILIES;
    }

    @Override
    public Deity[] getDeities() {
        return DEITIES;
    }

}
