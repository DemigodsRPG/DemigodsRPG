package com.demigodsrpg.game.aspect.i;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityResult;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.util.TargetingUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;

public class LightningAspectI implements Aspect {
    @Override
    public String getName() {
        return "Lightning I";
    }

    @Override
    public String name() {
        return "LIGHTNING_ASPECT_I";
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public String getInfo() {
        return "Power over lightning";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.YELLOW;
    }

    @Override
    public Sound getSound() {
        return Sound.AMBIENCE_THUNDER;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.FEATHER);
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
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);

        if (!player.getWorld().equals(target.getWorld())) return false;
        Location toHit = TargetingUtil.adjustedAimLocation(model, target);

        player.getWorld().strikeLightningEffect(toHit);

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

        if (!TargetingUtil.isHit(target, toHit) && notify) player.sendMessage(ChatColor.RED + "Missed...");

        return true;
    }
}
