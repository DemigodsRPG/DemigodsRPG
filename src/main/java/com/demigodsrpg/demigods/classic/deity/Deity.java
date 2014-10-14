package com.demigodsrpg.demigods.classic.deity;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.deity.god.Ares;
import com.demigodsrpg.demigods.classic.deity.god.Hephaestus;
import com.demigodsrpg.demigods.classic.deity.god.major.Hades;
import com.demigodsrpg.demigods.classic.deity.god.major.Poseidon;
import com.demigodsrpg.demigods.classic.deity.god.major.Zeus;
import com.demigodsrpg.demigods.classic.deity.neutral.Human;
import com.demigodsrpg.demigods.classic.deity.neutral.Satyr;
import com.demigodsrpg.demigods.classic.deity.titan.Oceanus;
import com.demigodsrpg.demigods.classic.deity.titan.Prometheus;
import com.demigodsrpg.demigods.classic.deity.titan.major.Coeus;
import com.demigodsrpg.demigods.classic.deity.titan.major.Cronus;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

public enum Deity implements IDeity {
    // -- MORTAL -- //

    HUMAN(new Human(), 0), SATYR(new Satyr(), 1),

    // -- OLYMPIAN -- //

    // - Major - //

    ZEUS(new Zeus(), 2), POSEIDON(new Poseidon(), 3), HADES(new Hades(), 7),

    // - Minor - //

    HEPHAESTUS(new Hephaestus(), 4), ARES(new Ares(), 9), PROMETHEUS(new Prometheus(), 10),

    // -- TITAN -- //

    CRONUS(new Cronus(), 5), COEUS(new Coeus(), 6),

    // - Minor - //

    OCEANUS(new Oceanus(), 8);

    private final IDeity deity;
    private final int id;

    private Deity(IDeity deity, int id) {
        this.deity = deity;
        this.id = id;
    }

    @Override
    public String getDeityName() {
        return deity.getDeityName();
    }

    @Override
    public String getNomen() {
        return deity.getNomen();
    }

    @Override
    public String getInfo() {
        return deity.getInfo();
    }

    @Override
    public ChatColor getColor() {
        return deity.getColor();
    }

    @Override
    public Sound getSound() {
        return deity.getSound();
    }

    @Override
    public MaterialData getClaimMaterial() {
        return deity.getClaimMaterial();
    }

    @Override
    public Importance getImportance() {
        return deity.getImportance();
    }

    @Override
    public Alliance getDefaultAlliance() {
        return deity.getDefaultAlliance();
    }

    @Override
    public Pantheon getPantheon() {
        return deity.getPantheon();
    }

    public IDeity getParentObject() {
        return deity;
    }

    public Class<? extends IDeity> getParentObjectClass() {
        return deity.getClass();
    }

    public int getId() {
        return id;
    }

    public static Deity fromId(int id) {
        for (Deity deity : values()) {
            if (id == deity.id) {
                return deity;
            }
        }
        throw new NullPointerException();
    }

    public static boolean hasDeity(Player player, Deity deity) {
        return DGClassic.PLAYER_R.fromPlayer(player).hasDeity(deity);
    }
}
