package com.demigodsrpg.game.aspect.bloodlust;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityResult;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Groups;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.entity.EntityChangeHealthEvent;


public class BloodlustAspectIII implements Aspect {
    @Override
    public Group getGroup() {
        return Groups.BLOODLUST_ASPECT;
    }

    @Override
    public int getId() {
        return 12;
    }

    @Override
    public String getInfo() {
        return "Mastery over bloodlust.";
    }

    @Override
    public Tier getTier() {
        return Tier.III;
    }

    @Ability(name = "Mighty Fists", info = "Attacking with no item does extra damage.", type = Ability.Type.PASSIVE)
    public AbilityResult fistsAbility(EntityChangeHealthEvent event) {
        if (event.getCause().isPresent() && event.getCause().get() instanceof Player) {
            Player player = (Player) event.getCause().get();
            if (DGGame.PLAYER_R.fromPlayer(player).getAspects().contains(getGroup() + " " + getTier().name())) {
                if (!player.getItemInHand().isPresent()) {
                    event.setNewHealth(event.getOldHealth() - 15.0);
                }
            }
        }

        return AbilityResult.SUCCESS;
    }
}