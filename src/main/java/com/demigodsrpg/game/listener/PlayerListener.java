package com.demigodsrpg.game.listener;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Set;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(event.getPlayer());

        event.getPlayer().sendMessage("Welcome!");
        if (model.getAspects().isEmpty()) {
            event.getPlayer().sendMessage("TEMP: " + ChatColor.GRAY + "Use " + ChatColor.YELLOW + "/aspect claim" + ChatColor.GRAY + " to claim an aspect.");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(event.getPlayer());
        if (DGGame.SERV_R.contains("alliance_chat", event.getPlayer().getUniqueId().toString()) && !Faction.NEUTRAL.equals(model.getFaction()) && !Faction.EXCOMMUNICATED.equals(model.getFaction())) {
            event.getRecipients().clear();
            Set<PlayerModel> playerModelSet = DGGame.PLAYER_R.getOnlineInAlliance(model.getFaction());
            for (PlayerModel playerModel : playerModelSet) {
                event.getRecipients().add(playerModel.getOfflinePlayer().getPlayer());
            }
        }
    }
}
