package com.demigodsrpg.game.command.type;

import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.message.Messages;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandSource;

import java.util.List;

public abstract class BaseCommand implements CommandCallable {
    @Override
    public boolean call(CommandSource sender, String alias, List<String> args) {
        CommandResult result = onCommand(sender, alias, args);
        switch (result) {
            case SUCCESS:
            case QUIET_ERROR:
                break;
            case INVALID_SYNTAX:
                sender.sendMessage(Messages.builder().color(TextColors.RED).append(Messages.of("Invalid syntax, please try again.")).build());
                return false;
            case NO_PERMISSIONS:
                sender.sendMessage(Messages.builder().color(TextColors.RED).append(Messages.of("You don't have the permissions to use this command.")).build());
                break;
            case CONSOLE_ONLY:
                sender.sendMessage(Messages.builder().color(TextColors.RED).append(Messages.of("This command is for the console only.")).build());
                break;
            case PLAYER_ONLY:
                sender.sendMessage(Messages.builder().color(TextColors.RED).append(Messages.of("This command can only be used by a player.")).build());
                break;
            case ERROR:
                sender.sendMessage(Messages.builder().color(TextColors.RED).append(Messages.of("An error occurred, please check the console.")).build());
                break;
            case UNKNOWN:
            default:
                sender.sendMessage(Messages.builder().color(TextColors.RED).append(Messages.of("The command can't run for some unknown reason.")).build());
                break;
        }
        return true;
    }

    protected abstract CommandResult onCommand(CommandSource sender, String alias, List<String> args);
}
