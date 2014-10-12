package com.demigodsrpg.demigods.classic.deity.god;

import com.demigodsrpg.demigods.classic.ability.Ability;
import com.demigodsrpg.demigods.classic.ability.AbilityResult;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.util.TargetingUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;

public class Ares implements IDeity {
    @Override
    public String getDeityName() {
        return "Ares";
    }

    @Override
    public String getNomen() {
        return "acolyte of " + getDeityName();
    }

    @Override
    public String getInfo() {
        return "God of war.";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.RED;
    }

    @Override
    public Sound getSound() {
        return Sound.VILLAGER_HIT;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.GOLD_SWORD);
    }

    @Override
    public IDeity.Importance getImportance() {
        return Importance.MINOR;
    }

    @Override
    public IDeity.Alliance getDefaultAlliance() {
        return Alliance.OLYMPIAN;
    }

    @Override
    public IDeity.Pantheon getPantheon() {
        return Pantheon.OLYMPIAN;
    }

    @Ability(name = "Blitz", command = "blitz", info = "Rush to a target entity and deal extra damage.", cost = 170, delay = 3000)
    public AbilityResult blitzAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        LivingEntity target = TargetingUtil.autoTarget(player, 250);

        if (target == null) return AbilityResult.NO_TARGET_FOUND;

        if (player.getLocation().toVector().distance(target.getLocation().toVector()) > 2) {
            float pitch = player.getLocation().getPitch();
            float yaw = player.getLocation().getYaw();
            Location tar = target.getLocation();
            tar.setPitch(pitch);
            tar.setYaw(yaw);
            player.teleport(tar);
            target.damage(2, player);

            player.sendMessage(getColor() + "*shooom*");

            return AbilityResult.SUCCESS;
        }
        return AbilityResult.NO_TARGET_FOUND;

    }
}