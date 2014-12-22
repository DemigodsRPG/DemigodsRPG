package com.demigodsrpg.demigods.classic.integration.chitchat;

import com.demigodsrpg.chitchat.tag.PlayerTag;
import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AllianceDeityIdTag extends PlayerTag {
    @Override
    public String getName() {
        return "alliance_deity_id";
    }

    @Override
    public String getFor(Player player) {
        PlayerModel model = DGClassic.PLAYER_R.fromPlayer(player);
        char symbol = IDeity.Alliance.EXCOMMUNICATED.equals(model.getAlliance()) ? 'X' : model.getAlliance().name().charAt(0);
        return model.getMajorDeity().getColor() + "[" + model.getMajorDeity().getColor() + symbol + "]" + ChatColor.RESET;
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
