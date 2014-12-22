package com.demigodsrpg.demigods.classic.integration.chitchat;

import com.demigodsrpg.chitchat.tag.PlayerTag;
import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
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
        PlayerModel model = DGClassic.PLAYER_R.fromPlayer(player);
        return DGClassic.SERV_R.contains("alliance_chat", player.getUniqueId().toString()) && !IDeity.Alliance.NEUTRAL.equals(model.getAlliance()) && !IDeity.Alliance.EXCOMMUNICATED.equals(model.getAlliance());
    }
}
