package com.demigodsrpg.demigods.classic.deity.titan.major;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.ability.Ability;
import com.demigodsrpg.demigods.classic.ability.AbilityResult;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import net.minecraft.util.com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Iapetus implements IDeity {
    @Override
    public String getDeityName() {
        return "Iapetus";
    }

    @Override
    public String getNomen() {
        return "spawn of " + getDeityName();
    }

    @Override
    public String getInfo() {
        return "Titan of mortality.";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.WHITE;
    }

    @Override
    public Sound getSound() {
        return Sound.GHAST_SCREAM;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.ROTTEN_FLESH);
    }

    @Override
    public IDeity.Importance getImportance() {
        return Importance.MAJOR;
    }

    @Override
    public IDeity.Alliance getDefaultAlliance() {
        return Alliance.TITAN;
    }

    @Override
    public IDeity.Pantheon getPantheon() {
        return Pantheon.TITAN;
    }

    @Ability(name = "Deathblow", command = "deathblow", info = "Deal massive amounts of damage, increasing with each kill.", cost = 3500, cooldown = 200000, type = Ability.Type.ULTIMATE)
    public AbilityResult deathblowAbility(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        PlayerModel model = DGClassic.PLAYER_R.fromPlayer(player);

        final AtomicDouble damage = new AtomicDouble(10.0);
        final AtomicInteger deaths = new AtomicInteger(0);

        final List<LivingEntity> targets = new ArrayList<LivingEntity>();
        final Location startloc = player.getLocation();
        for (LivingEntity e : player.getWorld().getLivingEntities()) {
            if (e.getLocation().toVector().isInSphere(player.getLocation().toVector(), 35) && !targets.contains(e)) // jumps to the nearest entity
            {
                if (e instanceof Player && DGClassic.PLAYER_R.fromPlayer((Player) e).getAlliance().equals(model.getAlliance()))
                    continue;
                targets.add(e);
            }
        }
        if (targets.size() == 0) {
            player.sendMessage("There are no targets to attack.");
            return AbilityResult.OTHER_FAILURE;
        }

        final double savedHealth = player.getHealth();

        for (int i = 0; i < targets.size(); i++) {
            final int ii = i;
            DGClassic.getInst().getServer().getScheduler().scheduleSyncDelayedTask(DGClassic.getInst(), new Runnable() {
                public void run() {
                    player.teleport(targets.get(ii));
                    player.setHealth(savedHealth);
                    player.getLocation().setPitch(targets.get(ii).getLocation().getPitch());
                    player.getLocation().setYaw(targets.get(ii).getLocation().getYaw());
                    if (targets.get(ii).getHealth() - damage.doubleValue() <= 0.0) {
                        damage.set(10.0 + deaths.incrementAndGet() * 1.2);
                        player.sendMessage(getColor() + "Damage is now " + damage.doubleValue());
                    }
                    targets.get(ii).damage(damage.doubleValue(), player);
                }
            }, i * 10);
        }

        DGClassic.getInst().getServer().getScheduler().scheduleSyncDelayedTask(DGClassic.getInst(), new Runnable() {
            public void run() {
                player.teleport(startloc);
                player.setHealth(savedHealth);
                player.sendMessage(targets.size() + " targets were struck with the fury of " + ChatColor.GOLD + "Iapetus" + ChatColor.WHITE + ".");
            }
        }, targets.size() * 10);

        return AbilityResult.SUCCESS;
    }
}