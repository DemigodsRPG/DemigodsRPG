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

package com.demigodsrpg.game.listener;

import com.demigodsrpg.aspect.Aspects;
import com.demigodsrpg.data.DGData;
import com.demigodsrpg.util.ZoneUtil;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class AbilityListener implements Listener {
    /**
     * Various no damage abilities, these must be done by hand, and directly in this method.
     *
     * @param event The damage event.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    private void onNoDamageAbilities(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (DGData.PLAYER_R.hasAspect(player, Aspects.BLOODLUST_ASPECT_HERO)) {
                if (player.getHealth() <= event.getDamage()) {
                    switch (event.getCause()) {
                        case ENTITY_ATTACK:
                            break;
                        case PROJECTILE:
                            break;
                        case CUSTOM:
                            break;
                        default:
                            event.setDamage(player.getHealth() - 1);
                    }
                }
                event.setDamage(event.getDamage() / 2);
            }

            if (DGData.PLAYER_R.hasAspect(player, Aspects.MAGNETISM_ASPECT_HERO)) {
                if (EntityDamageEvent.DamageCause.FALL.equals(event.getCause())) {
                    event.setCancelled(true);
                }
            }

            if (DGData.PLAYER_R.hasAspect(player, Aspects.WATER_ASPECT_HERO)) {
                if (EntityDamageEvent.DamageCause.DROWNING.equals(event.getCause())) {
                    event.setCancelled(true);
                    player.setRemainingAir(player.getMaximumAir());
                }
            }

            if (DGData.PLAYER_R.hasAspect(player, Aspects.FIRE_ASPECT_I)) {
                if (EntityDamageEvent.DamageCause.FIRE.equals(event.getCause()) || EntityDamageEvent.DamageCause.FIRE_TICK.equals(event.getCause())) {
                    event.setCancelled(true);
                    player.setRemainingAir(player.getMaximumAir());
                }
            }
        }
    }

    /**
     * Various player move abilities, these must be done by hand, and directly in this method.
     *
     * @param event The move event.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerMoveEvent(PlayerMoveEvent event) {
        if (ZoneUtil.inNoDGZone(event.getPlayer().getLocation())) return;

        Player player = event.getPlayer();

        if (DGData.PLAYER_R.hasAspect(player, Aspects.WATER_ASPECT_HERO)) {
            Material locationMaterial = player.getLocation().getBlock().getType();
            if (player.isSneaking() && (locationMaterial.equals(Material.STATIONARY_WATER) || locationMaterial.equals(Material.WATER))) {
                Vector victor = (player.getPassenger() != null && player.getLocation().getDirection().getY() > 0 ? player.getLocation().getDirection().clone().setY(0) : player.getLocation().getDirection()).normalize().multiply(1.3D);
                player.setVelocity(victor);
            }
        }
    }

    /**
     * Various entity target events, these must be done by hand, and directly in this method.
     *
     * @param event The target event.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onEvent(EntityTargetEvent event) {
        Entity entity = event.getEntity();

        if (event.getTarget() instanceof Player) {
            // Demon Aspect III
            if (entity instanceof Zombie || entity instanceof Skeleton) {
                if (DGData.PLAYER_R.fromPlayer((Player) event.getTarget()).hasAspect(Aspects.DEMON_ASPECT_HERO)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
