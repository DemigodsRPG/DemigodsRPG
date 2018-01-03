package com.demigodsrpg.aspect.demon;


import com.demigodsrpg.DGData;
import com.demigodsrpg.ability.Ability;
import com.demigodsrpg.ability.AbilityResult;
import com.demigodsrpg.aspect.*;
import com.demigodsrpg.model.PlayerModel;
import com.demigodsrpg.util.ItemUtil;
import com.demigodsrpg.util.TargetingUtil;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class DemonAspectI implements Aspect {
    @Override
    public Group getGroup() {
        return Groups.DEMON_ASPECT;
    }

    @Override
    public ItemStack getItem() {
        return ItemUtil.create(Material.IRON_FENCE, getName(), Arrays.asList(getInfo()), null);
    }

    @Override
    public int getId() {
        return -1;
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
        return "Dark Secret";
    }

    @Ability(name = "Chain", command = "chain", info = "Fire a chain of smoke that damages and blinds.", cost = 250, delay = 1500)
    public AbilityResult chainAbility(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();
        PlayerModel model = DGData.PLAYER_R.fromPlayer(player);

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