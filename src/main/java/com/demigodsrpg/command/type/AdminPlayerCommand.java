package com.demigodsrpg.command.type;

import com.demigodsrpg.DGData;
import com.demigodsrpg.model.PlayerModel;
import org.bukkit.command.*;

import java.util.ArrayList;
import java.util.List;

public abstract class AdminPlayerCommand extends BaseCommand implements TabCompleter {
    @SuppressWarnings("deprecation")
    @Override
    public CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (args.length > 0) {
            PlayerModel model = DGData.PLAYER_R.fromName(args[0]);
            if (model != null) {
                return onCommand(sender, model, args);
            }
        }
        return CommandResult.INVALID_SYNTAX;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return DGData.PLAYER_R.getNameStartsWith(args[0]);
        }
        return new ArrayList<>();
    }

    protected abstract CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args);
}
