package com.demigodsrpg.game.integration.chitchat;

import com.demigodsrpg.chitchat.tag.PlayerTag;
import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FactionIdTag extends PlayerTag {
    @Override
    public String getName() {
        return "faction_id";
    }

    @Override
    public String getFor(Player player) {
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);
        String symbol = model.getFaction().getChatSymbol();
        return model.getFaction().getColor() + "[" + model.getFaction().getColor() + symbol + "]" + ChatColor.RESET;
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
