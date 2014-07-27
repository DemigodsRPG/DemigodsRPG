package com.demigodsrpg.demigods.classic.listener;

import com.censoredsoftware.library.util.StringUtil2;
import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;

/**
 * Created by TheFatDemon on 7/26/2014.
 * If Used please give Credit
 */
@SuppressWarnings("unused")
public class PlayerChatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event){
        if(DGClassic.SERV_R.exists("alliance_chat", event.getPlayer().getUniqueId().toString())){
            event.getRecipients().clear();
            PlayerModel model = DGClassic.PLAYER_R.fromPlayer(event.getPlayer());
            Set<PlayerModel> playerModelSet = DGClassic.PLAYER_R.getOnlineInAlliance(model.getAlliance());
            for (PlayerModel playerModel : playerModelSet){
                event.getRecipients().add(playerModel.getOfflinePlayer().getPlayer());
            }
            String format = event.getFormat();
            event.setFormat( "[" + StringUtil2.beautify(model.getAlliance().name()) + "] " + format);
        }
    }

}
