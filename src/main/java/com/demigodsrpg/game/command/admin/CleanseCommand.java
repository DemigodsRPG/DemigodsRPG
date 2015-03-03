package com.demigodsrpg.game.command.admin;

public class CleanseCommand /* extends BaseCommand */ {
    /*
    @Override
    protected CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        // Check for no args
        if (args.length == 0) {
            sender.sendMessage(TextColors.YELLOW + "To cleanse all, do /cleanse all");
            return CommandResult.SUCCESS;
        }
        // Check for only 1 arg
        else if (args.length == 1) {
            // This subcommand doesn't allow the console
            if (sender instanceof ConsoleCommandSender) {
                return CommandResult.PLAYER_ONLY;
            }

            // Get the player object
            Player player = (Player) sender;
            PlayerModel playerModel = DGGame.PLAYER_R.fromPlayer(player);

            // Check that the player model is not null
            if (playerModel == null) {
                sender.sendMessage(TextColors.DARK_RED + "Something is wrong with your player data. Try to re-log.");
                return CommandResult.QUIET_ERROR;
            }

            // Do '/forsake all'
            if ("all".equalsIgnoreCase(args[0])) {
                // Loop over the player's deities
                for (String deityName : playerModel.getAspects()) {
                    // Get the deity object
                    Aspect aspect = Aspects.valueOf(deityName.toUpperCase());
                    if (aspect == null) {
                        sender.sendMessage(TextColors.RED + deityName + " does not exist, something is wrong with the player data...");
                        return CommandResult.QUIET_ERROR;
                    }

                    playerModel.removeAspect(aspect);
                    sender.sendMessage(TextColors.YELLOW + "You have cleansed " + aspect.getGroup().getName());
                }

                // Set the alliance to neutral
                playerModel.setFaction(Faction.NEUTRAL);
                return CommandResult.SUCCESS;
            } else {
                // TODO Check for individual deity forsake requests.
                sender.sendMessage(TextColors.YELLOW + "To cleanse all do /cleanse all");
                return CommandResult.SUCCESS;
            }
        }
        // Handle admin command to forsake other players
        else if (args.length == 2) {
            // Check if the sender has permission to do the forsaking.
            if (!sender.hasPermission("demigods.admin.cleanse")) {
                return CommandResult.NO_PERMISSIONS;
            }

            // Handle '/forsake <player> all' subcommand
            if ("all".equalsIgnoreCase(args[1])) {
                // Get the target's player model
                PlayerModel playerModel = DGGame.PLAYER_R.fromName(args[0]);
                if (playerModel == null) {
                    sender.sendMessage(TextColors.DARK_RED + "No such player! Please try a little harder.");
                    return CommandResult.QUIET_ERROR;
                }

                // Loop over the target's deities
                for (String deityName : playerModel.getAspects()) {
                    // Get the deity from the name
                    Aspect aspect = Aspects.valueOf(deityName);
                    if (aspect == null) {
                        continue;
                    }

                    playerModel.removeAspect(aspect);
                }

                // Set the target's alliance to neutral.
                playerModel.setFaction(Faction.NEUTRAL);
                sender.sendMessage(TextColors.YELLOW + "You have removed all aspects from " + playerModel.getLastKnownName());

                return CommandResult.SUCCESS;
            } else {
                // TODO Check for individual deity forsake requests.
                return CommandResult.INVALID_SYNTAX;
            }
        }

        // Something went wrong...
        return CommandResult.QUIET_ERROR;
    }
    */
}
