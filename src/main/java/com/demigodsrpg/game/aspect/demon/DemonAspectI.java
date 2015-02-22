package com.demigodsrpg.game.aspect.demon;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityResult;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.aspect.Groups;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.util.TargetingUtil;
import org.bukkit.Effect;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
    public AbilityResult chainAbility(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);

        double devotion = model.getExperience(Aspects.DEMON_ASPECT_I);
        double damage = Math.round(Math.pow(devotion, 0.20688));
        int blindpower = (int) Math.round(1.26985 * Math.pow(devotion, 0.13047));
        int blindduration = (int) Math.round(0.75 * Math.pow(devotion, 0.323999));
        chain(player, damage, blindpower, blindduration);

        return AbilityResult.SUCCESS;
    }

    private boolean chain(Player p, double damage, int blindpower, int blindduration) {
        LivingEntity target = TargetingUtil.autoTarget(p);
        if (target == null) return false;
        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, blindduration, blindpower));
        target.damage(damage);
        target.setLastDamageCause(new EntityDamageByEntityEvent(p, target, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage));
        for (BlockFace bf : BlockFace.values()) {
            p.getWorld().playEffect(target.getLocation().getBlock().getRelative(bf).getLocation(), Effect.SMOKE, 1);
        }
        return true;
    }
}