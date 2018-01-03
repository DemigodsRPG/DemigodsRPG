/*
 * Copyright 2015 Demigods RPG
 * Copyright 2015 Alexander Chauncey
 * Copyright 2015 Alex Bennett
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        return ItemUtil.create(Material.GOLD_SWORD, getName(), Arrays.asList(getInfo()), null);
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

    @Ability(name = "Deathblow", command = "deathblow", info = "Deal massive amounts of damage, increasing with each kill.", cost = 3500, cooldown = 200000, type = Ability.Type.ULTIMATE)
    public AbilityResult deathblowAbility(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        PlayerModel model = DGData.PLAYER_R.fromPlayer(player);

        final AtomicDouble damage = new AtomicDouble(10.0);
        final AtomicInteger deaths = new AtomicInteger(0);

        final List<LivingEntity> targets = new ArrayList<LivingEntity>();
        final Location startloc = player.getLocation();
        for (LivingEntity e : player.getWorld().getLivingEntities()) {
            if (e.getLocation().toVector().isInSphere(player.getLocation().toVector(), 35) && !targets.contains(e)) // jumps to the nearest entity
            {
                if (e instanceof Player && DGData.PLAYER_R.fromPlayer((Player) e).getFamily().equals(model.getFamily()))
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
            player.teleport(startloc);
            player.setHealth(savedHealth);
            player.sendMessage(targets.size() + " targets were struck with the power of bloodlust.");
        }, targets.size() * 10);

        return AbilityResult.SUCCESS;
    }
}