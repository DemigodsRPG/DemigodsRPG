package com.demigodsrpg.game.integration.chitchat;

import com.demigodsrpg.chitchat.tag.PlayerTag;
import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AllianceChatTag extends PlayerTag {
    @Override
    public String getName() {
        return "alliance_chat";
    }

    @Override
    public String getFor(Player player) {
        if (isInChat(player)) {
            return ChatColor.DARK_GRAY + "[.]";
        }
        return ChatColor.DARK_RED + "[!]";
    }

    @Override
    public boolean cancelBungee(Player player) {
        return isInChat(player);
    }

    @Override
    public int getPriority() {
        return 0;
    }

    private boolean isInChat(Player player) {
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);
        return DGGame.SERV_R.contains("alliance_chat", player.getUniqueId().toString()) && !Faction.NEUTRAL.equals(model.getFaction()) && !Faction.EXCOMMUNICATED.equals(model.getFaction());
    }
}
