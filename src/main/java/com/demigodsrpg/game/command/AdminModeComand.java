package com.demigodsrpg.game.command;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.command.type.BaseCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.model.PlayerModel;
import com.google.common.base.Optional;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;

import java.util.List;

public class AdminModeComand extends BaseCommand {
    @Override
    protected CommandResult onCommand(CommandSource sender, String command, List<String> args) {
        if (sender instanceof Player && sender.hasPermission("demigods.admin")) {
            PlayerModel model = DGGame.PLAYER_R.fromPlayer((Player) sender);
            if (model.getAdminMode()) {
                sender.sendMessage(TextColors.YELLOW + "Demigods admin mode disabled.");
            } else {
                sender.sendMessage(TextColors.YELLOW + "Demigods admin mode enabled.");
            }
            model.setAdminMode(!model.getAdminMode());

            return CommandResult.SUCCESS;
        }

        return CommandResult.NO_PERMISSIONS;
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
