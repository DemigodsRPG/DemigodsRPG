package com.demigodsrpg.listener;

import com.demigodsrpg.DGData;
import com.demigodsrpg.aspect.Aspects;
import com.demigodsrpg.model.PlayerModel;
import com.demigodsrpg.util.ZoneUtil;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
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
                if (EntityDamageEvent.DamageCause.FIRE.equals(event.getCause()) ||
                        EntityDamageEvent.DamageCause.FIRE_TICK.equals(event.getCause())) {
                    event.setCancelled(true);
                    player.setRemainingAir(player.getMaximumAir());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onEvent(FurnaceSmeltEvent event) {
        for (PlayerModel model : DGData.PLAYER_R.fromAspect(Aspects.CRAFTING_ASPECT_I)) {
            try {
                if (model.getOnline() && model.getLocation().getWorld().equals(event.getBlock().getWorld()) &&
                        model.getLocation().distance(event.getBlock().getLocation()) <
                                (int) Math.round(20 * Math.pow(model.getExperience(Aspects.CRAFTING_ASPECT_I), 0.15))) {
                    int amount = event.getResult().getAmount() * 2;
                    ItemStack out = event.getResult();
                    out.setAmount(amount);
                    event.setResult(out);
                }
            } catch (Exception oops) {
                oops.printStackTrace();
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
            if (player.isSneaking() &&
                    (locationMaterial.equals(Material.LEGACY_STATIONARY_WATER) ||
                            locationMaterial.equals(Material.WATER))) {
                Vector victor = (player.getPassenger() != null && player.getLocation().getDirection().getY() > 0 ?
                        player.getLocation().getDirection().clone().setY(0) : player.getLocation().getDirection())
                        .normalize().multiply(1.3D);
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
