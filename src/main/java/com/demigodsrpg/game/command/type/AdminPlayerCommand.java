package com.demigodsrpg.game.command.type;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.model.PlayerModel;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;

import java.util.ArrayList;
import java.util.List;

public abstract class AdminPlayerCommand extends BaseCommand {
    @SuppressWarnings("deprecation")
    @Override
    public CommandResult onCommand(CommandSource sender, String command, List<String> args) {
        if (args.size() > 0) {
            PlayerModel model = DGGame.PLAYER_R.fromName(args.get(0));
            if (model != null) {
                return onCommand(sender, model, args);
            }
        }
        return CommandResult.INVALID_SYNTAX;
    }

    @Override
    public List<String> getSuggestions(CommandSource commandSource, String s) throws CommandException {
        if (s.split(" ").length == 1) {
            return DGGame.PLAYER_R.getNameStartsWith(s.split(" ")[0]);
        }
        return new ArrayList<>();
    }

    protected abstract CommandResult onCommand(CommandSource sender, PlayerModel model, List<String> args);
}
