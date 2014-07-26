package com.demigodsrpg.demigods.classic.deity;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;

public interface IDeity {
    String getDeityName();

    String getNomen();

    ChatColor getColor();

    Sound getSound();

    MaterialData getClaimMaterial();

    Importance getImportance();

    Alliance getDefaultAlliance();

    Pantheon getPantheon();

    public enum Importance {
        NONE, MINOR, MAJOR
    }

    public enum Alliance {
        NEUTRAL, OLYMPIAN, TITAN, EXCOMMUNICATED
    }

    public enum Pantheon {
        MORTAL, OLYMPIAN, TITAN, GIANT
    }
}
