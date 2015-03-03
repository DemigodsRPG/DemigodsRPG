package com.demigodsrpg.game.aspect.water;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityResult;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Groups;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.util.TargetingUtil;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.entity.living.player.PlayerInteractEvent;
import org.spongepowered.api.text.format.TextColors;

public class WaterAspectI implements Aspect {
    @Override
    public Group getGroup() {
        return Groups.WATER_ASPECT;
    }

    @Override
    public int getId() {
        return 7;
    }

    @Override
    public String getInfo() {
        return "Adept level power over water.";
    }

    @Override
    public Tier getTier() {
        return Tier.I;
    }

    @Ability(name = "Drown", command = "drown", info = "Use the power of water for a stronger attack.", cost = 120, delay = 1500)
    public AbilityResult drownAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);

        double damage = Math.ceil(0.37286 * Math.pow(model.getLevel() * 100, 0.371238)); // TODO Make damage do more?

        Entity hit = TargetingUtil.autoTarget(player);

        if (hit != null && hit instanceof Living) {
            player.sendMessage(TextColors.AQUA + "*shploosh*");
            ((Living) hit).damage(damage);
            ((Living) hit).setLastAttacker(player);

            if (hit.getLocation().getBlock().getType().equals(BlockTypes.AIR)) {
                hit.getLocation().getBlock().replaceWith(BlockTypes.WATER);
                hit.getLocation().getBlock().replaceWith(BlockTypes.WATER.getStateFromDataValue((byte) 0x8));
            }

            return AbilityResult.SUCCESS;
        }

        return AbilityResult.NO_TARGET_FOUND;
    }
}
