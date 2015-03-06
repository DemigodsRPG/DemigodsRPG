package com.demigodsrpg.game.aspect.bloodlust;

import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityResult;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Groups;
import com.demigodsrpg.game.util.TargetingUtil;
import com.flowpowered.math.vector.Vector3f;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.entity.living.player.PlayerInteractEvent;

public class BloodlustAspectI implements Aspect {
    @Override
    public Group getGroup() {
        return Groups.BLOODLUST_ASPECT;
    }

    @Override
    public int getId() {
        return 10;
    }

    @Override
    public String getInfo() {
        return "Adept level power over bloodlust.";
    }

    @Override
    public Tier getTier() {
        return Tier.I;
    }

    @Ability(name = "Blitz", command = "blitz", info = "Rush to a target entity and deal extra damage.", cost = 170, delay = 3000)
    public AbilityResult blitzAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Entity target = TargetingUtil.autoTarget(player, 250);

        if (!(target instanceof Living)) {
            return AbilityResult.NO_TARGET_FOUND;
        }

        if (player.getLocation().getPosition().distance(target.getLocation().getPosition()) > 2) {
            float pitch = player.getRotation().getX(); // TODO does this work?
            float yaw = player.getRotation().getY();
            Vector3f tar = new Vector3f(pitch, yaw, target.getRotation().getZ());
            player.setRotation(tar);
            ((Living) target).damage(2.0);
            ((Living) target).setLastAttacker(player);

            player.sendMessage(getGroup().getColor() + "*shooom*");

            return AbilityResult.SUCCESS;
        }
        return AbilityResult.NO_TARGET_FOUND;
    }
}