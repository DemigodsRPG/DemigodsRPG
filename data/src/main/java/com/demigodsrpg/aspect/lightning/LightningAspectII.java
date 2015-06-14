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

import com.censoredsoftware.library.bukkitutil.ItemUtil;
import com.demigodsrpg.ability.Ability;
import com.demigodsrpg.ability.AbilityResult;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Groups;
import com.demigodsrpg.data.DGData;
import com.demigodsrpg.data.model.PlayerModel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class LightningAspectII implements Aspect {

    // -- ASPECT META -- //

    @Override
    public Group getGroup() {
        return Groups.LIGHTNING_ASPECT;
    }

    @Override
    public ItemStack getItem() {
        return ItemUtil.create(Material.BLAZE_ROD, name(), Arrays.asList(getInfo()), null);
    }

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Mastery over lightning"};
    }

    @Override
    public Tier getTier() {
        return Tier.II;
    }

    @Override
    public String name() {
        return "Angry Skies";
    }

    // -- ABILITIES -- //

    @Ability(name = "Storm", command = "storm", info = "Strike fear into the hearts of your enemies.", cost = 3700, cooldown = 600000, type = Ability.Type.ULTIMATE)
    public AbilityResult stormAbility(PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        // Define variables
        PlayerModel model = DGData.PLAYER_R.fromPlayer(event.getPlayer());

        // Define variables
        final int ultimateSkillLevel = model.getLevel();
        final int damage = 10 * model.getLevel();
        final int radius = (int) Math.log10(10 * ultimateSkillLevel) * 25;

        // Make it stormy for the caster
        setWeather(player, 100);

        // Strike targets
        for (final Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            // Validate them first
            if (!(entity instanceof LivingEntity)) continue;
            if (entity instanceof Player) {
                PlayerModel opponent = DGData.PLAYER_R.fromPlayer((Player) entity);
                if (opponent != null && model.getFamily().equals(opponent.getFamily())) continue;
            }
            if (DGData.BATTLE_R.canParticipate(entity) && !DGData.BATTLE_R.canTarget(entity)) continue;

            // Make it stormy for players
            if (entity instanceof Player) setWeather((Player) entity, 100);

            // Strike them with a small delay
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DGData.PLUGIN, new BukkitRunnable() {
                @Override
                public void run() {
                    for (int i = 0; i <= 3; i++) {
                        player.getWorld().strikeLightningEffect(entity.getLocation());
                        if (entity.getLocation().getBlock().getType().equals(Material.WATER)) {
                            ((LivingEntity) entity).damage(damage + 4);
                        } else {
                            ((LivingEntity) entity).damage(damage);
                        }
                        entity.setLastDamageCause(new EntityDamageByEntityEvent(player, entity, EntityDamageEvent.DamageCause.LIGHTNING, damage));
                    }
                }
            }, 15);
        }

        return AbilityResult.SUCCESS;
    }

    private static void setWeather(final Player player, long ticks) {
        // Set the weather
        player.setPlayerWeather(WeatherType.DOWNFALL);

        // Create the runnable to switch back
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DGData.PLUGIN, new BukkitRunnable() {
            @Override
            public void run() {
                player.resetPlayerWeather();
            }
        }, ticks);
    }
}
