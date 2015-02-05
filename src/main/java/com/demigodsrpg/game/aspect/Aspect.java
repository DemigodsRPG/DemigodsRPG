package com.demigodsrpg.game.aspect;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.aspect.god.Ares;
import com.demigodsrpg.game.aspect.god.Hephaestus;
import com.demigodsrpg.game.aspect.god.major.Hades;
import com.demigodsrpg.game.aspect.god.major.Poseidon;
import com.demigodsrpg.game.aspect.god.major.Zeus;
import com.demigodsrpg.game.aspect.titan.Oceanus;
import com.demigodsrpg.game.aspect.titan.Prometheus;
import com.demigodsrpg.game.aspect.titan.major.Coeus;
import com.demigodsrpg.game.aspect.titan.major.Cronus;
import com.demigodsrpg.game.aspect.titan.major.Iapetus;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

public enum Aspect implements IAspect {
    // -- OLYMPIAN -- //

    // - Major - //

    ZEUS(new Zeus(), 2), POSEIDON(new Poseidon(), 3), HADES(new Hades(), 7),

    // - Minor - //

    HEPHAESTUS(new Hephaestus(), 4), ARES(new Ares(), 9), PROMETHEUS(new Prometheus(), 10),

    // -- TITAN -- //

    CRONUS(new Cronus(), 5), COEUS(new Coeus(), 6), IAPETUS(new Iapetus(), 11),

    // - Minor - //

    OCEANUS(new Oceanus(), 8);

    private final IAspect aspect;
    private final int id;

    private Aspect(IAspect aspect, int id) {
        this.aspect = aspect;
        this.id = id;
    }

    @Override
    public String getName() {
        return aspect.getName();
    }

    @Override
    public String getInfo() {
        return aspect.getInfo();
    }

    @Override
    public ChatColor getColor() {
        return aspect.getColor();
    }

    @Override
    public Sound getSound() {
        return aspect.getSound();
    }

    @Override
    public MaterialData getClaimMaterial() {
        return aspect.getClaimMaterial();
    }

    @Override
    public Strength getStrength() {
        return aspect.getStrength();
    }

    public IAspect getParentObject() {
        return aspect;
    }

    public Class<? extends IAspect> getParentObjectClass() {
        return aspect.getClass();
    }

    public int getId() {
        return id;
    }

    public static Aspect fromId(int id) {
        for (Aspect aspect : values()) {
            if (id == aspect.id) {
                return aspect;
            }
        }
        throw new NullPointerException();
    }

    public static boolean hasDeity(Player player, Aspect aspect) {
        return DGGame.PLAYER_R.fromPlayer(player).hasAspect(aspect);
    }
}
