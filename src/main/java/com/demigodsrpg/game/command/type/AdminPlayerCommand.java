package com.demigodsrpg.game.command.type;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.model.PlayerModel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public abstract class AdminPlayerCommand extends BaseCommand implements TabCompleter {
    @SuppressWarnings("deprecation")
    @Override
    public CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (args.length > 0) {
            PlayerModel model = DGGame.PLAYER_R.fromName(args[0]);
            if (model != null) {
                return onCommand(sender, model, args);
            }
        }
        return CommandResult.INVALID_SYNTAX;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return DGGame.PLAYER_R.getNameStartsWith(args[0]);
        }
        return new ArrayList<>();
    }

    protected abstract CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args);
}
