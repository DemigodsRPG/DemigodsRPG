package com.demigodsrpg.listener;

import com.demigodsrpg.DGData;
import com.demigodsrpg.Setting;
import com.demigodsrpg.family.Family;
import com.demigodsrpg.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import java.util.Set;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerModel model = DGData.PLAYER_R.fromPlayer(event.getPlayer());

        player.setMaxHealth(model.getMaxHealth());
        player.setHealthScale(20.0);
        player.setHealthScaled(true);
        player.setHealth(model.getMaxHealth());

        player.sendMessage("Welcome!");
        if (Setting.DEBUG_DATA) {
            player.sendMessage(ChatColor.RED + "This server is currently in " + ChatColor.BOLD + "Demigods Demo Mode" +
                    ChatColor.RED + ".");
        }
        if (model.getAspects().isEmpty()) {
            // TODO IDK what to do here yet
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        PlayerModel model = DGData.PLAYER_R.fromPlayer(event.getPlayer());

        player.setMaxHealth(model.getMaxHealth());
        player.setHealthScale(20.0);
        player.setHealthScaled(true);
        player.setHealth(model.getMaxHealth());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        PlayerModel model = DGData.PLAYER_R.fromPlayer(event.getPlayer());
        if (DGData.SERVER_R.contains("faction_chat", event.getPlayer().getUniqueId().toString()) &&
                !Family.NEUTRAL.equals(model.getFamily()) && !Family.EXCOMMUNICATED.equals(model.getFamily())) {
            event.getRecipients().clear();
            Set<PlayerModel> playerModelSet = DGData.PLAYER_R.getOnlineInAlliance(model.getFamily());
            for (PlayerModel playerModel : playerModelSet) {
                event.getRecipients().add(playerModel.getOfflinePlayer().getPlayer());
            }
        }
    }
}
