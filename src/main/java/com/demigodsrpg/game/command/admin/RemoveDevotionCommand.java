package com.demigodsrpg.game.command.admin;

public class RemoveDevotionCommand /* extends AdminPlayerCommand */ {
    /*
    @Override
    public CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args) {
        if (args.length == 3) {

            PlayerModel m;
            try {
                Player p = DGGame.PLAYER_R.fromName(args[0]).getPlayer().getPlayer();
                double amount = Double.parseDouble(args[2]);
                m = DGGame.PLAYER_R.fromPlayer(p);
                Aspect aspect = Aspects.valueOf(args[1].toUpperCase());
                if (!m.getAspects().contains(aspect.getGroup().getName() + " " + aspect.getTier().name())) {
                    sender.sendMessage(TextColors.RED + "The player you are accessing does not have that deity.");
                    return CommandResult.QUIET_ERROR;
                }
                double newAmount = m.getExperience(aspect) - amount;
                if (newAmount < 0) newAmount = 0;

                m.setExperience(aspect, newAmount);

                sender.sendMessage(TextColors.YELLOW + "You removed " + amount + " devotion from " + p.getName() + " in the aspect group " + aspect.getGroup().getName() + ".");
            } catch (Exception ignored) {
                sender.sendMessage(TextColors.RED + "Invalid syntax! /RemoveDevotion [Name, Deity, Amount]");
                return CommandResult.QUIET_ERROR;
            }
            return CommandResult.SUCCESS;
        }
        return CommandResult.INVALID_SYNTAX;
    }
    */
}