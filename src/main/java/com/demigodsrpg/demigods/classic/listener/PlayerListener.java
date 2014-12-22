package com.demigodsrpg.demigods.classic.listener;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.deity.Deity;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
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
        PlayerModel model = DGClassic.PLAYER_R.fromPlayer(event.getPlayer());

        event.getPlayer().sendMessage("Welcome!");
        if (model.getMajorDeity().equals(Deity.HUMAN)) {
            event.getPlayer().sendMessage(ChatColor.GRAY + "Use " + ChatColor.YELLOW + "/deity claim" + ChatColor.GRAY + " to claim a deity.");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        PlayerModel model = DGClassic.PLAYER_R.fromPlayer(event.getPlayer());
        if (DGClassic.SERV_R.contains("alliance_chat", event.getPlayer().getUniqueId().toString()) && !IDeity.Alliance.NEUTRAL.equals(model.getAlliance()) && !IDeity.Alliance.EXCOMMUNICATED.equals(model.getAlliance())) {
            event.getRecipients().clear();
            Set<PlayerModel> playerModelSet = DGClassic.PLAYER_R.getOnlineInAlliance(model.getAlliance());
            for (PlayerModel playerModel : playerModelSet) {
                event.getRecipients().add(playerModel.getOfflinePlayer().getPlayer());
            }
        }
    }
}
