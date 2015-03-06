package com.demigodsrpg.game.aspect.demon;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityResult;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.aspect.Groups;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.util.TargetingUtil;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.entity.living.player.PlayerInteractEvent;
import org.spongepowered.api.world.Location;

import java.util.ArrayList;
import java.util.List;

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

        Living le = TargetingUtil.autoTarget(player);
        if (le == null) return AbilityResult.NO_TARGET_FOUND;
        int duration = (int) Math.round(2.18678 * Math.pow(model.getExperience(Aspects.DEMON_ASPECT_II), 0.24723)); // seconds
        final List<BlockLoc> tochange = new ArrayList<>();
        for (int x = -3; x <= 3; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -3; z <= 3; z++) {
                    BlockLoc block = new Location(le.getLocation().getExtent(), new Vector3d(le.getLocation().getBlock().getX() + x, le.getLocation().getBlock().getY() + y, le.getLocation().getBlock().getZ() + z)).getBlock();
                    if ((block.getPosition().toDouble().distance(le.getLocation().getPosition()) > 2) && (block.getLocation().getPosition().toDouble().distance(le.getLocation().getPosition()) < 3.5))
                        if (BlockTypes.AIR.equals(block.getType()) || BlockTypes.WATER.equals(block.getType()) || BlockTypes.LAVA.equals(block.getType())) {
                            block.replaceWith(BlockTypes.OBSIDIAN);
                            tochange.add(block);
                        }
                }
            }
        }

        DGGame.GAME.getSyncScheduler().runTaskAfter(DGGame.getInst(), () -> {
            for (BlockLoc b : tochange)
                if (BlockTypes.OBSIDIAN.equals(b.getType())) {
                    b.replaceWith(BlockTypes.AIR);
                }
        }, duration * 20);

        return AbilityResult.SUCCESS;
    }
}