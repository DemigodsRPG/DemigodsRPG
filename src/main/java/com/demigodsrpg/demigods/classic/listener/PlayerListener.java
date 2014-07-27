package com.demigodsrpg.demigods.classic.listener;

import com.censoredsoftware.library.util.StringUtil2;
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

import java.util.Random;
import java.util.Set;

public class PlayerListener implements Listener {
    Random random = new Random();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PlayerModel model = DGClassic.PLAYER_R.fromPlayer(event.getPlayer());

        // TODO REMOVE THIS DEBUG TESTING ONLY
        if (model.getMajorDeity().equals(Deity.HUMAN)) {
            model = new PlayerModel(event.getPlayer());
            DGClassic.PLAYER_R.register(model);

            if (random.nextBoolean()) {
                model.giveMajorDeity(Deity.ZEUS);
                model.giveDeity(Deity.HEPHAESTUS);
            } else {
                model.giveMajorDeity(Deity.CRONUS);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        PlayerModel model = DGClassic.PLAYER_R.fromPlayer(event.getPlayer());

        if (DGClassic.SERV_R.exists("alliance_chat", event.getPlayer().getUniqueId().toString()) && !IDeity.Alliance.NEUTRAL.equals(model.getAlliance()) && !IDeity.Alliance.EXCOMMUNICATED.equals(model.getAlliance())) {
            event.getRecipients().clear();
            Set<PlayerModel> playerModelSet = DGClassic.PLAYER_R.getOnlineInAlliance(model.getAlliance());
            for (PlayerModel playerModel : playerModelSet) {
                event.getRecipients().add(playerModel.getOfflinePlayer().getPlayer());
            }
            event.setFormat(ChatColor.GRAY + "[ALL]" + model.getMajorDeity().getColor() + "[" + model.getMajorDeity().getColor() + StringUtil2.beautify(model.getAlliance().name()) + "] " + event.getPlayer().getDisplayName() + ChatColor.GRAY + ":" + event.getMessage());
        } else {
            String format = event.getFormat();
            event.setFormat(ChatColor.GRAY + "[TEAM]" + model.getMajorDeity().getColor() + "[" + model.getMajorDeity().getColor() + StringUtil2.beautify(model.getAlliance().name()) + "] " + format);
        }
    }
}
