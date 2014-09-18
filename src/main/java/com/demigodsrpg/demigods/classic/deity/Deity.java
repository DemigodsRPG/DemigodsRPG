package com.demigodsrpg.demigods.classic.deity;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.deity.god.Hephaestus;
import com.demigodsrpg.demigods.classic.deity.god.major.Poseidon;
import com.demigodsrpg.demigods.classic.deity.god.major.Zeus;
import com.demigodsrpg.demigods.classic.deity.neutral.Human;
import com.demigodsrpg.demigods.classic.deity.neutral.Satyr;
import com.demigodsrpg.demigods.classic.deity.titan.major.Cronus;
import com.demigodsrpg.demigods.classic.deity.titan.major.Rhea;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

public enum Deity implements IDeity {
    // -- MORTAL -- //

    HUMAN(new Human()), SATYR(new Satyr()),

    // -- OLYMPIAN -- //

    ZEUS(new Zeus()), POSEIDON(new Poseidon()), HEPHAESTUS(new Hephaestus()),

    // -- TITAN -- //

    CRONUS(new Cronus()), RHEA(new Rhea());

    private IDeity deity;

    private Deity(IDeity deity) {
        this.deity = deity;
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

    public static boolean hasDeity(Player player, Deity deity) {
        PlayerModel model = DGClassic.PLAYER_R.fromPlayer(player);
        return model.getMajorDeity().equals(deity) || model.getContractedDeities().contains(deity);
    }
}
