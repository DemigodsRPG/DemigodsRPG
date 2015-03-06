package com.demigodsrpg.game.aspect.lightning;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityResult;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Groups;
import com.demigodsrpg.game.model.PlayerModel;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.entity.living.player.PlayerInteractEvent;

public class LightningAspectIII implements Aspect {

    // -- ASPECT META -- //

    @Override
    public Group getGroup() {
        return Groups.LIGHTNING_ASPECT;
    }

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public String getInfo() {
        return "Mastery over lightning";
    }

    @Override
    public Tier getTier() {
        return Tier.III;
    }

    // -- ABILITIES -- //

    @Ability(name = "Storm", command = "storm", info = "Strike fear into the hearts of your enemies.", cost = 3700, cooldown = 600000, type = Ability.Type.ULTIMATE)
    public AbilityResult stormAbility(PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        // Define variables
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(event.getPlayer());

        // Define variables
        final int ultimateSkillLevel = model.getLevel();
        final int damage = 10 * model.getLevel();
        final int radius = (int) Math.log10(10 * ultimateSkillLevel) * 25;

        // Make it stormy for the caster
        setWeather(player, 100);

        // Strike targets
        for (final Entity entity : player.getWorld().getEntities(e -> e.getLocation().getPosition().distance(player.getLocation().getPosition()) <= radius)) {
            // Validate them first
            if (!(entity instanceof Living)) continue;
            if (entity instanceof Player) {
                PlayerModel opponent = DGGame.PLAYER_R.fromPlayer((Player) entity);
                if (opponent != null && model.getFaction().equals(opponent.getFaction())) continue;
            }
            if (DGGame.BATTLE_R.canParticipate(entity) && !DGGame.BATTLE_R.canTarget(entity)) continue;

            // Make it stormy for players
            if (entity instanceof Player) setWeather((Player) entity, 100);

            // Strike them with a small delay
            DGGame.GAME.getSyncScheduler().runTaskAfter(DGGame.getInst(), new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i <= 3; i++) {
                        // FIXME player.getWorld().strikeLightningEffect(entity.getLocation());
                        if (entity.getLocation().getBlock().getType().equals(BlockTypes.WATER)) {
                            ((Living) entity).damage(damage + 4);
                        } else {
                            ((Living) entity).damage(damage);
                        }
                        ((Living) entity).setLastAttacker(player);
                    }
                }
            }, 15);
        }

        return AbilityResult.SUCCESS;
    }

    private static void setWeather(final Player player, long ticks) {
        /* FIXME
        // Set the weather
        player.setPlayerWeather(WeatherType.DOWNFALL);

        // Create the runnable to switch back
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DGGame.getInst(), new BukkitRunnable() {
            @Override
            public void run() {
                player.resetPlayerWeather();
            }
        }, ticks);
        */
    }
}
