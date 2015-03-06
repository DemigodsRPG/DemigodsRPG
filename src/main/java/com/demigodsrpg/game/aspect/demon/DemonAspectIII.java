package com.demigodsrpg.game.aspect.demon;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityResult;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.aspect.Groups;
import com.demigodsrpg.game.model.PlayerModel;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.entity.living.player.PlayerInteractEvent;
import org.spongepowered.api.potion.PotionEffectTypes;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

public class DemonAspectIII implements Aspect {
    @Override
    public Group getGroup() {
        return Groups.DEMON_ASPECT;
    }

    @Override
    public int getId() {
        return -3;
    }

    @Override
    public String getInfo() {
        return "Blood of a demon.";
    }

    @Override
    public Tier getTier() {
        return Tier.I;
    }

    @Ability(name = "Demon Friendlies", info = "Demon monsters will not attack you.", type = Ability.Type.PASSIVE, placeholder = true)
    public void friendlyAbility() {
        // Do nothing, handled directly in the ability listener to save time
    }

    @Ability(name = "Curse", command = "curse", info = "Turns day to night as nothingness corrupts your enemies.", cost = 4000, cooldown = 600000, type = Ability.Type.ULTIMATE)
    public AbilityResult curseAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);

        int amt = tartarus(player, model);
        if (amt > 0) {
            player.sendMessage(TextColors.DARK_RED + "Nothingness" + TextColors.GRAY + " has corrupted " + amt + " enemies.");
            // FIXME player.getWorld().setTime(18000);
            return AbilityResult.SUCCESS;
        } else {
            player.sendMessage(TextColors.YELLOW + "There were no valid targets or the ultimate could not be used.");
        }
        return AbilityResult.OTHER_FAILURE;
    }

    private int tartarus(Player p, PlayerModel m) {
        int range = (int) Math.round(18.83043 * Math.pow(m.getExperience(Aspects.DEMON_ASPECT_III), 0.088637));
        List<Living> entitylist = new ArrayList<>();
        for (Entity anEntity : p.getWorld().getEntities(entity -> entity.getLocation().getPosition().distance(p.getLocation().getPosition()) <= range)) {
            if (anEntity instanceof Player && m.getFaction().equals(DGGame.PLAYER_R.fromPlayer((Player) anEntity).getFaction())) {
                continue;
            }
            if (anEntity instanceof Living) {
                entitylist.add((Living) anEntity);
            }
        }
        int duration = (int) Math.round(30 * Math.pow(m.getExperience(Aspects.DEMON_ASPECT_III), 0.09)) * 20;
        for (Living le : entitylist) {
            target(le, duration);
        }
        return entitylist.size();
    }

    private void target(Living le, int duration) {
        le.addPotionEffect(DGGame.GAME.getRegistry().getPotionEffectBuilder().potionType(PotionEffectTypes.BLINDNESS).duration(duration).amplifier(5).build(), true);
        le.addPotionEffect(DGGame.GAME.getRegistry().getPotionEffectBuilder().potionType(PotionEffectTypes.WEAKNESS).duration(duration).amplifier(5).build(), true);
        le.addPotionEffect(DGGame.GAME.getRegistry().getPotionEffectBuilder().potionType(PotionEffectTypes.NAUSEA).duration(duration).amplifier(5).build(), true);
    }
}