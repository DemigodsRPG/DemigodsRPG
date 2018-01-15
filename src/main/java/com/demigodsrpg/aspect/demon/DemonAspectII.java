package com.demigodsrpg.aspect.demon;


import com.demigodsrpg.DGData;
import com.demigodsrpg.ability.Ability;
import com.demigodsrpg.ability.AbilityResult;
import com.demigodsrpg.aspect.*;
import com.demigodsrpg.model.PlayerModel;
import com.demigodsrpg.util.ItemUtil;
import com.demigodsrpg.util.TargetingUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class DemonAspectII implements Aspect {
    @Override
    public Group getGroup() {
        return Groups.DEMON_ASPECT;
    }

    @Override
    public ItemStack getItem() {
        return ItemUtil.create(Material.OBSIDIAN, getName(), Arrays.asList(getInfo()), null);
    }

    @Override
    public int getId() {
        return -2;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Blood of a demon."};
    }

    @Override
    public Tier getTier() {
        return Tier.I;
    }

    @Override
    public String getName() {
        return "Soulless";
    }

    @Ability(name = "Entomb", command = "entomb", info = "Entomb an entity in obsidian.", cost = 470, cooldown = 20000)
    public AbilityResult entombAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerModel model = DGData.PLAYER_R.fromPlayer(player);

        LivingEntity le = TargetingUtil.autoTarget(player);
        if (le == null) return AbilityResult.NO_TARGET_FOUND;
        int duration =
                (int) Math.round(2.18678 * Math.pow(model.getExperience(Aspects.DEMON_ASPECT_II), 0.24723)); // seconds
        final ArrayList<Block> tochange = new ArrayList<Block>();
        for (int x = -3; x <= 3; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -3; z <= 3; z++) {
                    Block block = player.getWorld()
                            .getBlockAt(le.getLocation().getBlockX() + x, le.getLocation().getBlockY() + y,
                                    le.getLocation().getBlockZ() + z);
                    if ((block.getLocation().distance(le.getLocation()) > 2) &&
                            (block.getLocation().distance(le.getLocation()) < 3.5)) {
                        if ((block.getType() == Material.AIR) || (block.getType() == Material.WATER) ||
                                (block.getType() == Material.LAVA)) {
                            block.setType(Material.OBSIDIAN);
                            tochange.add(block);
                        }
                    }
                }
            }
        }

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DGData.PLUGIN, () -> {
            tochange.stream().filter(b -> b.getType() == Material.OBSIDIAN).forEach(b -> b.setType(Material.AIR));
        }, duration * 20);

        return AbilityResult.SUCCESS;
    }
}