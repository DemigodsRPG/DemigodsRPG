package com.demigodsrpg.chitchat;

import com.demigodsrpg.DGData;
import com.demigodsrpg.chitchat.tag.PlayerTag;
import com.demigodsrpg.family.Family;
import com.demigodsrpg.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FactionChatTag extends PlayerTag {
    @Override
    public String getName() {
        return "faction_chat";
    }

    @Override
    public String getFor(Player player) {
        if (isInChat(player)) {
            return ChatColor.DARK_GRAY + "[.]";
        }
        return ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "!" + ChatColor.DARK_GRAY + "]";
    }

    @Override
    public boolean cancelRedis(Player player) {
        return isInChat(player);
    }

    @Override
    public int getPriority() {
        return 0;
    }

    private boolean isInChat(Player player) {
        PlayerModel model = DGData.PLAYER_R.fromPlayer(player);
        return DGData.SERVER_R.contains("faction_chat", player.getUniqueId().toString()) &&
                !Family.NEUTRAL.equals(model.getFamily()) && !Family.EXCOMMUNICATED.equals(model.getFamily());
    }
}
