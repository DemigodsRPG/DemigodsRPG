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

package com.demigodsrpg.aspect.lightning;


import com.demigodsrpg.DGData;
import com.demigodsrpg.ability.Ability;
import com.demigodsrpg.ability.AbilityResult;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Groups;
import com.demigodsrpg.model.PlayerModel;
import com.demigodsrpg.util.ItemUtil;
import com.demigodsrpg.util.TargetingUtil;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class LightningAspectI implements Aspect {

    // -- ASPECT META -- //

    @Override
    public Group getGroup() {
        return Groups.LIGHTNING_ASPECT;
    }

    @Override
    public ItemStack getItem() {
        return ItemUtil.create(Material.FEATHER, getName(), Arrays.asList(getInfo()), null);
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Adept level power over lightning"};
    }

    @Override
    public Tier getTier() {
        return Tier.I;
    }

    @Override
    public String getName() {
        return "Shock Touch";
    }

    // -- ABILITIES -- //

    @Ability(name = "Lightning", command = "lightning", info = "Strike lightning at a target location.", cost = 140, delay = 1000)
    public AbilityResult lightningAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Define variables
        Location target;
        LivingEntity entity = TargetingUtil.autoTarget(player);
        boolean notify;

        if (entity != null) {
            target = entity.getLocation();
            notify = true;
        } else {
            target = TargetingUtil.directTarget(player);
            notify = false;
        }

        return strikeLightning(player, target, notify) ? AbilityResult.SUCCESS : AbilityResult.OTHER_FAILURE;
    }

    private static boolean strikeLightning(Player player, Location target, boolean notify) {
        // Set variables
        PlayerModel model = DGData.PLAYER_R.fromPlayer(player);

        if (!player.getWorld().equals(target.getWorld())) return false;
        Location toHit = TargetingUtil.adjustedAimLocation(model, target);

        player.getWorld().strikeLightningEffect(toHit);

        for (Entity entity : toHit.getBlock().getChunk().getEntities()) {
            if (entity instanceof LivingEntity) {
                if (!DGData.BATTLE_R.canTarget(entity)) continue;
                LivingEntity livingEntity = (LivingEntity) entity;
                if (livingEntity.equals(player)) continue;
                double damage = 10 * model.getLevel();
                if ((toHit.getBlock().getType().equals(Material.WATER) || toHit.getBlock().getType().equals(Material.STATIONARY_WATER)) && livingEntity.getLocation().distance(toHit) < 8) {
                    damage += 4;
                    livingEntity.damage(damage);
                    entity.setLastDamageCause(new EntityDamageEvent(livingEntity, EntityDamageEvent.DamageCause.LIGHTNING, damage));
                } else if (livingEntity.getLocation().distance(toHit) < 2) {
                    damage += 3;
                    livingEntity.damage(damage);
                    entity.setLastDamageCause(new EntityDamageEvent(livingEntity, EntityDamageEvent.DamageCause.LIGHTNING, damage));
                }
            }
        }

        if (!TargetingUtil.isHit(target, toHit) && notify) player.sendMessage(ChatColor.RED + "Missed...");

        return true;
    }
}
