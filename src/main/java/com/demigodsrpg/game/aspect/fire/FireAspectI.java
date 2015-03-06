package com.demigodsrpg.game.aspect.fire;

import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityResult;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Groups;
import com.demigodsrpg.game.util.TargetingUtil;
import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Optional;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.entity.projectile.explosive.fireball.Fireball;
import org.spongepowered.api.event.entity.living.player.PlayerInteractEvent;
import org.spongepowered.api.world.Location;

public class FireAspectI implements Aspect {

    // -- ASPECT META -- //

    @Override
    public Group getGroup() {
        return Groups.FIRE_ASPECT;
    }

    @Override
    public int getId() {
        return 4;
    }

    @Override
    public String getInfo() {
        return "Adept level power over fire.";
    }

    @Override
    public Tier getTier() {
        return Tier.I;
    }

    // -- ABILITIES -- //

    @Ability(name = "Fireball", command = "fireball", info = "Bring fire to your enemies.", cost = 120, delay = 2000)
    public AbilityResult fireballAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Living targetEntity = TargetingUtil.autoTarget(player, 250);
        Location target;

        if (targetEntity != null) {
            target = targetEntity.getLocation();
        } else {
            target = TargetingUtil.directTarget(player);
        }

        shootFireball(player.getEyeLocation().toDouble(), target, player);

        player.sendMessage(getGroup().getColor() + "*fhhoom*");

        return AbilityResult.SUCCESS;
    }

    public static void shootFireball(Vector3d from, Location to, Player shooter) {
        Optional<Entity> fireballOptional = shooter.getWorld().createEntity(EntityTypes.FIREBALL, from);
        if (fireballOptional.isPresent()) {
            Fireball fireball = (Fireball) fireballOptional.get();
            shooter.getWorld().spawnEntity(fireball);
            to.add(0.5, 0.5, 0.5);
            Vector3d path = to.getPosition().sub(from);
            Vector3d victor = from.add(from.mul(2));
            fireball.setLocation(new Location(shooter.getWorld(), victor));
            fireball.setVelocity(path);
            fireball.setShooter(shooter);
        }
    }

    @Ability(name = "No Fire Damage", info = "Fire will not damage you.", type = Ability.Type.PASSIVE, placeholder = true)
    public void noFireDamageAbility() {
        // Do nothing, handled directly in the ability listener to save time
    }
}
