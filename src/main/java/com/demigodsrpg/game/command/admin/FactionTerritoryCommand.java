package com.demigodsrpg.game.command.admin;

import com.demigodsrpg.game.command.type.BaseCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class FactionTerritoryCommand extends BaseCommand {
    @Override
    protected CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        return null;
    }

    // TODO Create a selection type object where it keeps track of locations until the selection process is over,
    // TODO same with claim rooms. Should be an easy way to do this sort of thing.
}
