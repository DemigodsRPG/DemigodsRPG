package com.demigodsrpg.aspect.bloodlust;

import com.demigodsrpg.ability.Ability;
import com.demigodsrpg.ability.AbilityResult;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Groups;
import com.demigodsrpg.util.ItemUtil;
import com.demigodsrpg.util.TargetingUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class BloodlustAspectI implements Aspect {
    @Override
    public Group getGroup() {
        return Groups.BLOODLUST_ASPECT;
    }

    @Override
    public ItemStack getItem() {
        return ItemUtil.create(Material.IRON_SWORD, getName(), Arrays.asList(getInfo()), null);
    }

    @Override
    public int getId() {
        return 10;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Adept level power over bloodlust."};
    }

    @Override
    public Tier getTier() {
        return Tier.I;
    }

    @Override
    public String getName() {
        return "Bloodthirsty";
    }

    @Ability(name = "Blitz", command = "blitz", info = "Rush to a target entity and deal extra damage.", cost = 170,
            delay = 3000)
    public AbilityResult blitzAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        LivingEntity target = TargetingUtil.autoTarget(player, 250);

        if (target == null) return AbilityResult.NO_TARGET_FOUND;

        if (player.getLocation().toVector().distance(target.getLocation().toVector()) > 2) {
            float pitch = player.getLocation().getPitch();
            float yaw = player.getLocation().getYaw();
            Location tar = target.getLocation();
            tar.setPitch(pitch);
            tar.setYaw(yaw);
            player.teleport(tar);
            target.damage(2, player);

            player.sendMessage(getGroup().getColor() + "*shooom*");

            return AbilityResult.SUCCESS;
        }
        return AbilityResult.NO_TARGET_FOUND;
    }
}