package com.demigodsrpg.game.command;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.command.type.BaseCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.model.PlayerModel;
import com.google.common.base.Optional;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.source.ConsoleSource;

import java.util.List;

public class FactionCommand extends BaseCommand {
    @Override
    public CommandResult onCommand(CommandSource sender, String command, List<String> args) {
        if (sender instanceof ConsoleSource) {
            return CommandResult.PLAYER_ONLY;
        }

        Player player = (Player) sender;
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);

        if (Faction.NEUTRAL.equals(model.getFaction()) || Faction.EXCOMMUNICATED.equals(model.getFaction())) {
            player.sendMessage(TextColors.RED + "You aren't in an alliance.");
            return CommandResult.QUIET_ERROR;
        }

        if (DGGame.MISC_R.contains("alliance_chat", player.getUniqueId().toString())) {
            DGGame.MISC_R.remove("alliance_chat", player.getUniqueId().toString());
            player.sendMessage(TextColors.RED + "You just disabled alliance chat.");
        } else {
            DGGame.MISC_R.put("alliance_chat", player.getUniqueId().toString(), true);
            player.sendMessage(TextColors.RED + "You just enabled alliance chat.");
        }
        return CommandResult.SUCCESS;
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

    @Override
    public List<String> getSuggestions(CommandSource commandSource, String s) throws CommandException {
        return null;
    }
}
