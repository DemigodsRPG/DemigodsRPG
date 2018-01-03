package com.demigodsrpg.listener;

import com.demigodsrpg.DGData;
import com.demigodsrpg.battle.Battle;
import com.demigodsrpg.model.PlayerModel;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BattleListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (DGData.BATTLE_R.battleReady(event.getDamager(), event.getEntity())) {
            // Get the models
            PlayerModel damagerModel = DGData.PLAYER_R.fromPlayer((Player) event.getDamager());
            PlayerModel damageeModel = DGData.PLAYER_R.fromPlayer((Player) event.getEntity());

            // Declare the battle
            Battle battle;

            // Check if anyone is already in a battle
            if (DGData.BATTLE_R.isInBattle(damagerModel) || DGData.BATTLE_R.isInBattle(damageeModel)) {
                // Get the battle
                battle = DGData.BATTLE_R.getBattle(damagerModel, damageeModel);
            } else {
                battle = new Battle(damagerModel, damageeModel);
            }

            battle.hit(damagerModel, damageeModel);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        // Get the player and model
        Player player = event.getEntity();
        PlayerModel model = DGData.PLAYER_R.fromPlayer(player);

        // Check there is a battle
        if (DGData.BATTLE_R.isInBattle(model)) {
            // Get the battle
            Battle battle = DGData.BATTLE_R.getBattle(model);

            // Is it because of a battle?
            if (player.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                Entity damager = ((EntityDamageByEntityEvent) player.getLastDamageCause()).getDamager();
                if (damager instanceof Player) {
                    PlayerModel killer = DGData.PLAYER_R.fromPlayer((Player) damager);
                    battle.kill(killer, model);
                } else if (damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Player) {
                    PlayerModel killer = DGData.PLAYER_R.fromPlayer((Player) ((Projectile) damager).getShooter());
                    battle.kill(killer, model);
                }
                // They just died while in battle
            } else {
                battle.die(model);
            }
        }
    }
}
