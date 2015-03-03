package com.demigodsrpg.game.aspect.lightning;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityResult;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.aspect.Groups;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.util.TargetingUtil;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.entity.living.player.PlayerInteractEvent;
import org.spongepowered.api.text.format.TextColors;

public class LightningAspectII implements Aspect {

    // -- ASPECT META -- //

    @Override
    public Group getGroup() {
        return Groups.LIGHTNING_ASPECT;
    }

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public String getInfo() {
        return "Expert level power over lightning";
    }

    @Override
    public Tier getTier() {
        return Tier.II;
    }

    // -- ABILITIES -- //

    @Ability(name = "Shove", command = "shove", info = "Use the force of wind to shove your enemies.", cost = 170, delay = 1500)
    public AbilityResult pullAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);

        double devotion = model.getExperience(Aspects.LIGHTNING_ASPECT_II);
        double multiply = 0.1753 * Math.pow(devotion, 0.322917);

        Entity hit = TargetingUtil.autoTarget(player);

        if (hit instanceof Living) {
            player.sendMessage(TextColors.YELLOW + "*whoosh*");

            Vector3d v = player.getVelocity();
            Vector3d victor = hit.getVelocity().sub(v);
            victor.mul(multiply);
            hit.setVelocity(victor);

            return AbilityResult.SUCCESS;
        }

        return AbilityResult.NO_TARGET_FOUND;
    }


    @Ability(name = "No Fall Damage", info = "Take no fall damage.", type = Ability.Type.PASSIVE, placeholder = true)
    public void noFallDamageAbility() {
        // Do nothing, handled directly in the ability listener to save time
    }
}
