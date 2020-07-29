package com.demigodsrpg.aspect.lightning;


import com.demigodsrpg.DGData;
import com.demigodsrpg.ability.Ability;
import com.demigodsrpg.ability.AbilityResult;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Groups;
import com.demigodsrpg.model.PlayerModel;
import com.demigodsrpg.util.ItemUtil;
import org.bukkit.*;
import org.bukkit.entity.*;
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
        return ItemUtil.create(Material.BLAZE_ROD, getName(), Arrays.asList(getInfo()), null);
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
    public String getName() {
        return "Angry Skies";
    }

    // -- ABILITIES -- //

    @Ability(name = "Storm", command = "storm", info = "Strike fear into the hearts of your enemies.", cost = 3700,
            cooldown = 600000, type = Ability.Type.ULTIMATE)
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
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (int i = 0; i <= 3; i++) {
                        player.getWorld().strikeLightningEffect(entity.getLocation());
                        if (entity.getLocation().getBlock().getType().equals(Material.WATER)) {
                            ((LivingEntity) entity).damage(damage + 4);
                        } else {
                            ((LivingEntity) entity).damage(damage);
                        }
                        entity.setLastDamageCause(
                                new EntityDamageByEntityEvent(player, entity, EntityDamageEvent.DamageCause.LIGHTNING,
                                        damage));
                    }
                }
            }.runTaskLater(DGData.PLUGIN, 15);
        }

        return AbilityResult.SUCCESS;
    }

    private static void setWeather(final Player player, long ticks) {
        // Set the weather
        player.setPlayerWeather(WeatherType.DOWNFALL);

        // Create the runnable to switch back
        new BukkitRunnable() {
            @Override
            public void run() {
                player.resetPlayerWeather();
            }
        }.runTaskLater(DGData.PLUGIN, ticks);
    }
}
