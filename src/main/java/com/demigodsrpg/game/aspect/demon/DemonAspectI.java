package com.demigodsrpg.game.aspect.demon;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityResult;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.aspect.Groups;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.util.TargetingUtil;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.entity.EntityChangeHealthEvent;
import org.spongepowered.api.potion.PotionEffectTypes;
import org.spongepowered.api.util.Direction;

public class DemonAspectI implements Aspect {
    @Override
    public Group getGroup() {
        return Groups.DEMON_ASPECT;
    }

    @Override
    public int getId() {
        return -1;
    }

    @Override
    public String getInfo() {
        return "Blood of a demon.";
    }

    @Override
    public Tier getTier() {
        return Tier.I;
    }

    @Ability(name = "Chain", command = "chain", info = "Fire a chain of smoke that damages and blinds.", cost = 250, delay = 1500)
    public AbilityResult chainAbility(EntityChangeHealthEvent event) {
        Player player = (Player) event.getCause().get();
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);

        double devotion = model.getExperience(Aspects.DEMON_ASPECT_I);
        double damage = Math.round(Math.pow(devotion, 0.20688));
        int blindpower = (int) Math.round(1.26985 * Math.pow(devotion, 0.13047));
        int blindduration = (int) Math.round(0.75 * Math.pow(devotion, 0.323999));
        chain(player, damage, blindpower, blindduration);

        return AbilityResult.SUCCESS;
    }

    private boolean chain(Player p, double damage, int blindpower, int blindduration) {
        Living target = TargetingUtil.autoTarget(p);
        if (target == null) return false;
        target.addPotionEffect(DGGame.GAME.getRegistry().getPotionEffectBuilder().potionType(PotionEffectTypes.BLINDNESS).duration(blindduration).amplifier(blindpower).build(), true);
        target.damage(damage);
        target.setLastAttacker(p);
        for (Direction direction : Direction.values()) {
            p.getWorld().spawnParticles(DGGame.GAME.getRegistry().getParticleEffectBuilder(ParticleTypes.SMOKE_LARGE).count(1).build(), target.getLocation().getBlock().getRelative(direction).getLocation().getPosition(), 1);
        }
        return true;
    }
}