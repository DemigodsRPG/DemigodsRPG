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

package com.demigodsrpg.aspect.water;


import com.demigodsrpg.DGData;
import com.demigodsrpg.ability.Ability;
import com.demigodsrpg.ability.AbilityResult;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Groups;
import com.demigodsrpg.model.PlayerModel;
import com.demigodsrpg.util.ItemUtil;
import com.demigodsrpg.util.TargetingUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class WaterAspectI implements Aspect {
    @Override
    public Group getGroup() {
        return Groups.WATER_ASPECT;
    }

    @Override
    public ItemStack getItem() {
        return ItemUtil.create(Material.INK_SACK, getName(), Arrays.asList(getInfo()), null);
    }

    @Override
    public int getId() {
        return 7;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Adept level power over water."};
    }

    @Override
    public Tier getTier() {
        return Tier.I;
    }

    @Override
    public String getName() {
        return "Living Water";
    }

    @Ability(name = "Drown", command = "drown", info = "Use the power of water for a stronger attack.", cost = 120, delay = 1500)
    public AbilityResult drownAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerModel model = DGData.PLAYER_R.fromPlayer(player);

        double damage = Math.ceil(0.37286 * Math.pow(model.getLevel() * 100, 0.371238)); // TODO Make damage do more?

        LivingEntity hit = TargetingUtil.autoTarget(player);

        if (hit != null) {
            player.sendMessage(ChatColor.AQUA + "*shploosh*");
            hit.damage(damage);
            hit.setLastDamageCause(new EntityDamageByEntityEvent(player, hit, EntityDamageEvent.DamageCause.DROWNING, damage));

            if (hit.getLocation().getBlock().getType().equals(Material.AIR)) {
                player.getWorld().spawnFallingBlock(hit.getLocation(), Material.WATER, (byte) 0);
            }

            return AbilityResult.SUCCESS;
        }

        return AbilityResult.NO_TARGET_FOUND;
    }
}
