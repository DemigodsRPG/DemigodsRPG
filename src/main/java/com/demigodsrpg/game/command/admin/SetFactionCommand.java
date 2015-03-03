package com.demigodsrpg.game.command.admin;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.command.type.AdminPlayerCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.model.PlayerModel;
import com.google.common.base.Optional;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandSource;

import java.util.List;

public class SetFactionCommand extends AdminPlayerCommand {
    @Override
    public CommandResult onCommand(CommandSource sender, PlayerModel model, List<String> args) {
        if (args.size() == 2) {
            PlayerModel player = DGGame.PLAYER_R.fromName(args.get(0));
            Faction faction = DGGame.FACTION_R.factionFromName(args.get(1).toUpperCase());
            if (player == null || faction == null) {
                sender.sendMessage(TextColors.RED + "Wrong player or alliance! Please try a little harder.");
                return CommandResult.QUIET_ERROR;
            }
            player.setFaction(faction);
            sender.sendMessage(TextColors.YELLOW + player.getLastKnownName() + " has been set to the " + faction.getName() + " faction.");
            return CommandResult.SUCCESS;
        }
        return CommandResult.INVALID_SYNTAX;
    }

    @Override
    public boolean testPermission(CommandSource commandSource) {
        return false;
    }

    @Override
    public Optional<String> getShortDescription() {
        return null;
    }

    @Override
    public Optional<String> getHelp() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }
}
