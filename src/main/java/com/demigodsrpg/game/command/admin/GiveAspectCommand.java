package com.demigodsrpg.game.command.admin;

public class GiveAspectCommand /* extends AdminPlayerCommand */ {
    /*
    @Override
    public CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args) {
        if (args.length == 2) {
            PlayerModel p = null;
            Aspect aspect = null;
            try {
                aspect = Aspects.valueOf(args[1].toUpperCase());
                p = DGGame.PLAYER_R.fromName(args[0]);
            } catch (Exception ignored) {
            }
            if (p == null) {
                sender.sendMessage(TextColors.RED + "No such player.");
                return CommandResult.QUIET_ERROR;
            }
            if (aspect == null) {
                sender.sendMessage(TextColors.RED + "No such deity.");
                return CommandResult.QUIET_ERROR;
            }

            p.giveAspect(aspect);
            sender.sendMessage(TextColors.YELLOW + "You added " + aspect.getGroup().getName() + " " + aspect.getTier().name() + " to " + p.getLastKnownName() + ".");
            return CommandResult.SUCCESS;
        }
        return CommandResult.INVALID_SYNTAX;
    }
    */
}
