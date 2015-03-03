package com.demigodsrpg.game.command.admin;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.area.AreaSelection;
import com.demigodsrpg.game.command.type.BaseCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.model.PlayerModel;
import com.google.common.base.Optional;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;

import java.util.List;

public class SelectAreaCommand extends BaseCommand {

    @Override
    protected CommandResult onCommand(CommandSource sender, String command, List<String> args) {
        if (sender instanceof Player) {
            PlayerModel model = DGGame.PLAYER_R.fromPlayer((Player) sender);
            if (model.getAdminMode()) {
                AreaSelection selection = new AreaSelection((Player) sender);
                selection.register();
                AreaSelection.AREA_SELECTION_CACHE.put(model.getMojangId(), selection);

                sender.sendMessage(TextColors.YELLOW + "You may now make a selection.");

                // TODO Disabling a selection

            } else {
                return CommandResult.NO_PERMISSIONS;
            }
        } else {
            return CommandResult.PLAYER_ONLY;
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
