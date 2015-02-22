package com.demigodsrpg.game.aspect.water;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityResult;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Groups;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.util.TargetingUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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

        LivingEntity hit = TargetingUtil.autoTarget(player);

        if (hit != null) {
            player.sendMessage(ChatColor.AQUA + "*shploosh*");
            hit.damage(damage);
            hit.setLastDamageCause(new EntityDamageByEntityEvent(player, hit, EntityDamageEvent.DamageCause.DROWNING, damage));

            if (hit.getLocation().getBlock().getType().equals(Material.AIR)) {
                hit.getLocation().getBlock().setTypeIdAndData(Material.WATER.getId(), (byte) 0x8, true);
            }

            return AbilityResult.SUCCESS;
        }

        return AbilityResult.NO_TARGET_FOUND;
    }
}
