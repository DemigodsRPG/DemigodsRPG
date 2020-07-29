package com.demigodsrpg.aspect.water;


import com.demigodsrpg.DGData;
import com.demigodsrpg.ability.Ability;
import com.demigodsrpg.ability.AbilityResult;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Groups;
import com.demigodsrpg.model.PlayerModel;
import com.demigodsrpg.util.ItemUtil;
import com.demigodsrpg.util.TargetingUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class WaterAspectI implements Aspect {
    @Override
    public Group getGroup() {
        return Groups.WATER_ASPECT;
    }

    @Override
    public ItemStack getItem() {
        return ItemUtil.create(Material.INK_SAC, getName(), Arrays.asList(getInfo()), null);
    }

    @Override
    public int getId() {
        return 7;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Adept level power over water."};
    }

    @Override
    public Tier getTier() {
        return Tier.I;
    }

    @Override
    public String getName() {
        return "Living Water";
    }

    @Ability(name = "Drown", command = "drown", info = "Use the power of water for a stronger attack.", cost = 120,
            delay = 1500)
    public AbilityResult drownAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerModel model = DGData.PLAYER_R.fromPlayer(player);

        double damage = Math.ceil(0.37286 * Math.pow(model.getLevel() * 100, 0.371238)); // TODO Make damage do more?

        LivingEntity hit = TargetingUtil.autoTarget(player);

        if (hit != null) {
            player.sendMessage(ChatColor.AQUA + "*shploosh*");
            hit.damage(damage);
            hit.setLastDamageCause(
                    new EntityDamageByEntityEvent(player, hit, EntityDamageEvent.DamageCause.DROWNING, damage));

            if (hit.getLocation().getBlock().getType().equals(Material.AIR)) {
                player.getWorld().spawnFallingBlock(hit.getLocation(), Bukkit.getServer().createBlockData(Material.WATER));
            }

            return AbilityResult.SUCCESS;
        }

        return AbilityResult.NO_TARGET_FOUND;
    }
}
