package com.demigodsrpg.game.aspect.bloodlust;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityResult;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Groups;
import com.demigodsrpg.game.model.PlayerModel;
import com.flowpowered.math.vector.Vector3f;
import com.google.common.util.concurrent.AtomicDouble;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.entity.living.player.PlayerInteractEvent;
import org.spongepowered.api.world.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BloodlustAspectII implements Aspect {
    @Override
    public Group getGroup() {
        return Groups.BLOODLUST_ASPECT;
    }

    @Override
    public int getId() {
        return 11;
    }

    @Override
    public String getInfo() {
        return "Expert level power over bloodlust.";
    }

    @Override
    public Tier getTier() {
        return Tier.II;
    }

    @Ability(name = "Deathblow", command = "deathblow", info = "Deal massive amounts of damage, increasing with each kill.", cost = 3500, cooldown = 200000, type = Ability.Type.ULTIMATE)
    public AbilityResult deathblowAbility(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);

        final AtomicDouble damage = new AtomicDouble(10.0);
        final AtomicInteger deaths = new AtomicInteger(0);

        final List<Living> targets = new ArrayList<>();
        final Location startloc = player.getLocation();
        for (Entity e : player.getWorld().getEntities(entity -> entity instanceof Living)) {
            if (e.getLocation().getPosition().distance(player.getLocation().getPosition()) <= 35 && !targets.contains(e)) // jumps to the nearest entity
            {
                if (e instanceof Player && DGGame.PLAYER_R.fromPlayer((Player) e).getFaction().equals(model.getFaction()))
                    continue;
                targets.add((Living) e);
            }
        }
        if (targets.size() == 0) {
            player.sendMessage("There are no targets to attack.");
            return AbilityResult.OTHER_FAILURE;
        }

        final double savedHealth = player.getHealth();

        for (int i = 0; i < targets.size(); i++) {
            final int ii = i;
            DGGame.GAME.getSyncScheduler().runTaskAfter(DGGame.getInst(), () -> {
                player.setLocation(targets.get(ii).getLocation());
                player.setHealth(savedHealth);
                Vector3f targetRotation = targets.get(ii).getRotation();
                Vector3f newRotation = new Vector3f(targetRotation.getX(), targetRotation.getY(), player.getRotation().getZ());
                player.setRotation(newRotation);
                if (targets.get(ii).getHealth() - damage.doubleValue() <= 0.0) {
                    damage.set(10.0 + deaths.incrementAndGet() * 1.2);
                    player.sendMessage(getGroup().getColor() + "Damage is now " + damage.doubleValue());
                }
                targets.get(ii).damage(damage.doubleValue());
                targets.get(ii).setLastAttacker(player);
            }, i * 10);
        }

        DGGame.GAME.getSyncScheduler().runTaskAfter(DGGame.getInst(), () -> {
            player.setLocation(startloc);
            player.setHealth(savedHealth);
            player.sendMessage(targets.size() + " targets were struck with the power of bloodlust.");
        }, targets.size() * 10);

        return AbilityResult.SUCCESS;
    }
}