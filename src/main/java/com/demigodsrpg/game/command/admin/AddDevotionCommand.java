package com.demigodsrpg.game.command.admin;

public class AddDevotionCommand /* extends AdminPlayerCommand */ {
    /*
    @Override
    public CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args) {
        if (args.length == 3) {
            try {
                Player p = DGGame.PLAYER_R.fromName(args[0]).getPlayer().getPlayer();
                double amount = Double.parseDouble(args[2]);
                Aspect aspect = Aspects.valueOf(args[1].toUpperCase());
                if (!DGGame.PLAYER_R.fromPlayer(p).getAspects().contains(aspect.getGroup().getName() + " " + aspect.getTier().name())) {
                    sender.sendMessage(TextColors.RED + "The player you are accessing does not have that aspect.");
                    return CommandResult.QUIET_ERROR;
                }

                DGGame.PLAYER_R.fromPlayer(p).setExperience(aspect, DGGame.PLAYER_R.fromPlayer(p).getExperience(aspect) + amount);

                sender.sendMessage(TextColors.YELLOW + "You added " + amount + " devotion to " + p.getName() + " to the " + aspect.getGroup().getName() + " " + aspect.getTier().name() + " aspect.");
            } catch (Exception ignored) {
                sender.sendMessage(TextColors.RED + "Invalid syntax! /AddDevotion [Name, Aspect, Amount]");
                return CommandResult.QUIET_ERROR;
            }
            return CommandResult.SUCCESS;
        }
        return CommandResult.INVALID_SYNTAX;
    }
    */
}
