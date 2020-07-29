package com.demigodsrpg.aspect.bloodlust;

import com.demigodsrpg.DGData;
import com.demigodsrpg.ability.Ability;
import com.demigodsrpg.ability.AbilityResult;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Groups;
import com.demigodsrpg.model.PlayerModel;
import com.demigodsrpg.util.ItemUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BloodlustAspectII implements Aspect {
    @Override
    public Group getGroup() {
        return Groups.BLOODLUST_ASPECT;
    }

    @Override
    public ItemStack getItem() {
        return ItemUtil.create(Material.GOLDEN_SWORD, getName(), Arrays.asList(getInfo()), null);
    }

    @Override
    public int getId() {
        return 11;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Expert level power over bloodlust."};
    }

    @Override
    public Tier getTier() {
        return Tier.II;
    }

    @Override
    public String getName() {
        return "Bloodrage";
    }

    @Ability(name = "Deathblow", command = "deathblow",
            info = "Deal massive amounts of damage, increasing with each kill.", cost = 3500, cooldown = 200000,
            type = Ability.Type.ULTIMATE)
    public AbilityResult deathblowAbility(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        PlayerModel model = DGData.PLAYER_R.fromPlayer(player);

        final AtomicDouble damage = new AtomicDouble(10.0);
        final AtomicInteger deaths = new AtomicInteger(0);

        final List<LivingEntity> targets = new ArrayList<LivingEntity>();
        final Location startLoc = player.getLocation();
        for (LivingEntity e : player.getWorld().getLivingEntities()) {
            if (e.getLocation().toVector().isInSphere(player.getLocation().toVector(), 35) &&
                    !targets.contains(e)) // jumps to the nearest entity
            {
                if (e instanceof Player &&
                        DGData.PLAYER_R.fromPlayer((Player) e).getFamily().equals(model.getFamily())) { continue; }
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
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DGData.PLUGIN, () -> {
                player.teleport(targets.get(ii));
                player.setHealth(savedHealth);
                player.getLocation().setPitch(targets.get(ii).getLocation().getPitch());
                player.getLocation().setYaw(targets.get(ii).getLocation().getYaw());
                if (targets.get(ii).getHealth() - damage.doubleValue() <= 0.0) {
                    damage.set(10.0 + deaths.incrementAndGet() * 1.2);
                    player.sendMessage(getGroup().getColor() + "Damage is now " + damage.doubleValue());
                }
                targets.get(ii).damage(damage.doubleValue(), player);
            }, i * 10);
        }

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DGData.PLUGIN, () -> {
            player.teleport(startLoc);
            player.setHealth(savedHealth);
            player.sendMessage(targets.size() + " targets were struck with the power of bloodlust.");
        }, targets.size() * 10);

        return AbilityResult.SUCCESS;
    }
}