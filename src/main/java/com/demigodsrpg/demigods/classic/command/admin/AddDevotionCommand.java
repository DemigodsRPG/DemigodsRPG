package com.demigodsrpg.demigods.classic.command.admin;

import com.demigodsrpg.demigods.classic.command.type.AdminPlayerCommand;
import com.demigodsrpg.demigods.classic.command.type.CommandResult;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import org.bukkit.command.CommandSender;

public class AddDevotionCommand extends AdminPlayerCommand {
    @Override
    public CommandResult onPlayerCommand(CommandSender sender, PlayerModel model, String[] args) {
        if (args.length == 2) {

        }
        return CommandResult.NOT_ENOUGH_ARGS;
    }
}
