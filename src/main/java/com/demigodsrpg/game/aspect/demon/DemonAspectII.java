package com.demigodsrpg.game.aspect.demon;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityResult;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.aspect.Groups;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.util.TargetingUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class DemonAspectII implements Aspect {
    @Override
    public Group getGroup() {
        return Groups.DEMON_ASPECT;
    }

    @Override
    public int getId() {
        return -2;
    }

    @Override
    public String getInfo() {
        return "Blood of a demon.";
    }

    @Override
    public Tier getTier() {
        return Tier.I;
    }

    @Ability(name = "Entomb", command = "entomb", info = "Entomb an entity in obsidian.", cost = 470, cooldown = 20000)
    public AbilityResult entombAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);

        LivingEntity le = TargetingUtil.autoTarget(player);
        if (le == null) return AbilityResult.NO_TARGET_FOUND;
        int duration = (int) Math.round(2.18678 * Math.pow(model.getExperience(Aspects.DEMON_ASPECT_II), 0.24723)); // seconds
        final ArrayList<Block> tochange = new ArrayList<Block>();
        for (int x = -3; x <= 3; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -3; z <= 3; z++) {
                    Block block = player.getWorld().getBlockAt(le.getLocation().getBlockX() + x, le.getLocation().getBlockY() + y, le.getLocation().getBlockZ() + z);
                    if ((block.getLocation().distance(le.getLocation()) > 2) && (block.getLocation().distance(le.getLocation()) < 3.5))
                        if ((block.getType() == Material.AIR) || (block.getType() == Material.WATER) || (block.getType() == Material.LAVA)) {
                            block.setType(Material.OBSIDIAN);
                            tochange.add(block);
                        }
                }
            }
        }

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DGGame.getInst(), () -> {
            for (Block b : tochange)
                if (b.getType() == Material.OBSIDIAN) b.setType(Material.AIR);
        }, duration * 20);

        return AbilityResult.SUCCESS;
    }
}