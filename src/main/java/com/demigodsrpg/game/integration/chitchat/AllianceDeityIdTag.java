package com.demigodsrpg.game.integration.chitchat;

import com.demigodsrpg.chitchat.tag.PlayerTag;
import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.deity.IDeity;
import com.demigodsrpg.game.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AllianceDeityIdTag extends PlayerTag {
    @Override
    public String getName() {
        return "alliance_deity_id";
    }

    @Override
    public String getFor(Player player) {
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);
        char symbol = IDeity.Alliance.EXCOMMUNICATED.equals(model.getAlliance()) ? 'X' : model.getAlliance().name().charAt(0);
        return model.getMajorDeity().getColor() + "[" + model.getMajorDeity().getColor() + symbol + "]" + ChatColor.RESET;
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
