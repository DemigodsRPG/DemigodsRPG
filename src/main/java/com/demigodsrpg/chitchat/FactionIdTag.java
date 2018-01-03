package com.demigodsrpg.chitchat;

import com.demigodsrpg.DGData;
import com.demigodsrpg.chitchat.tag.PlayerTag;
import com.demigodsrpg.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FactionIdTag extends PlayerTag {
    @Override
    public String getName() {
        return "faction_id";
    }

    @Override
    public String getFor(Player player) {
        PlayerModel model = DGData.PLAYER_R.fromPlayer(player);
        String symbol = model.getFamily().getChatSymbol();
        return ChatColor.DARK_GRAY + "[" + model.getFamily().getColor() + symbol + ChatColor.DARK_GRAY + "]" +
                ChatColor.RESET;
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
