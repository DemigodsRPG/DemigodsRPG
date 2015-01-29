package com.demigodsrpg.game.deity.god.major;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityResult;
import com.demigodsrpg.game.deity.IDeity;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.util.TargetingUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;

public class Poseidon implements IDeity {
    @Override
    public String getDeityName() {
        return "Poseidon";
    }

    @Override
    public String getNomen() {
        return "child of " + getDeityName();
    }

    @Override
    public String getInfo() {
        return "God of the sea.";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.AQUA;
    }

    @Override
    public Sound getSound() {
        return Sound.WATER;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.INK_SACK);
    }

    @Override
    public Importance getImportance() {
        return Importance.MAJOR;
    }

    @Override
    public Alliance getDefaultAlliance() {
        return Alliance.OLYMPIAN;
    }

    @Override
    public Pantheon getPantheon() {
        return Pantheon.OLYMPIAN;
    }

    @Ability(name = "Swim", info = "Swim like quickly poseidon through the water.", type = Ability.Type.SUPPORT, placeholder = true)
    public void swimAbility() {
        // Do nothing, handled directly in the ability listener to save time
    }

    @Ability(name = "Drown", command = "drown", info = "Use the power of water for a stronger attack.", cost = 120, delay = 1500)
    public AbilityResult drownAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);

        double damage = Math.ceil(0.37286 * Math.pow(model.getAscensions() * 100, 0.371238)); // TODO Make damage do more?

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

    @Ability(name = "No Drown Damage", info = "Take no drown damage.", type = Ability.Type.PASSIVE, placeholder = true)
    public void noDrownDamageAbility() {
        // Do nothing, handled directly in the ability listener to save time
    }
}
