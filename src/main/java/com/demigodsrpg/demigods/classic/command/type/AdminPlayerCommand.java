package com.demigodsrpg.demigods.classic.command.type;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
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
            PlayerModel model = DGClassic.PLAYER_R.fromName(args[0]);
            if (model != null) {
                return onCommand(sender, model, args);
            }
        }
        return CommandResult.NOT_ENOUGH_ARGS;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return DGClassic.PLAYER_R.getNameStartsWith(args[0]);
        }
        return new ArrayList<>();
    }

    public abstract CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args);
}
