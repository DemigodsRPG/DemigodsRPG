package com.demigodsrpg.game.aspect.lightning;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityResult;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Groups;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.util.TargetingUtil;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.entity.living.player.PlayerInteractEvent;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;


public class LightningAspectI implements Aspect {

    // -- ASPECT META -- //

    @Override
    public Group getGroup() {
        return Groups.LIGHTNING_ASPECT;
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public String getInfo() {
        return "Adept level power over lightning";
    }

    @Override
    public Tier getTier() {
        return Tier.I;
    }

    // -- ABILITIES -- //

    @Ability(name = "Lightning", command = "lightning", info = "Strike lightning at a target location.", cost = 140, delay = 1000)
    public AbilityResult lightningAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Define variables
        Location target;
        Entity entity = TargetingUtil.autoTarget(player);
        boolean notify;

        if (entity instanceof Living) {
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
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);

        if (!player.getWorld().equals(target.getExtent())) return false;
        Location toHit = TargetingUtil.adjustedAimLocation(model, target);

        // player.getWorld().strikeLightningEffect(toHit); FIXME

        /* FIXME
        for (Entity entity : toHit.getBlock().getChunk().getEntities()) {
            if (entity instanceof LivingEntity) {
                if (!DGGame.BATTLE_R.canTarget(entity)) continue;
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
        */

        if (!TargetingUtil.isHit(target, toHit) && notify) player.sendMessage(TextColors.RED + "Missed...");

        return true;
    }
}
