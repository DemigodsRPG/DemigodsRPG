package com.demigodsrpg.game.aspect.fire;

import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityResult;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Groups;
import com.demigodsrpg.game.util.TargetingUtil;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

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

        LivingEntity targetEntity = TargetingUtil.autoTarget(player, 250);
        Location target;

        if (targetEntity != null) {
            target = targetEntity.getLocation();
        } else {
            target = TargetingUtil.directTarget(player);
        }

        shootFireball(player.getEyeLocation(), target, player);

        player.sendMessage(getGroup().getColor() + "*fhhoom*");

        return AbilityResult.SUCCESS;
    }

    public static void shootFireball(Location from, Location to, Player shooter) {
        Fireball fireball = (org.bukkit.entity.Fireball) shooter.getWorld().spawnEntity(from, EntityType.FIREBALL);
        to.setX(to.getX() + .5);
        to.setY(to.getY() + .5);
        to.setZ(to.getZ() + .5);
        Vector path = to.toVector().subtract(from.toVector());
        Vector victor = from.toVector().add(from.getDirection().multiply(2));
        fireball.teleport(new Location(shooter.getWorld(), victor.getX(), victor.getY(), victor.getZ()));
        fireball.setDirection(path);
        fireball.setShooter(shooter);
    }

    @Ability(name = "No Fire Damage", info = "Fire will not damage you.", type = Ability.Type.PASSIVE, placeholder = true)
    public void noFireDamageAbility() {
        // Do nothing, handled directly in the ability listener to save time
    }
}
