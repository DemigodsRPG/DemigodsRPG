package com.demigodsrpg.game.command.admin;

public class RemoveAspectCommand /* extends AdminPlayerCommand */ {
    /*
    @Override
    public CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args) {
        if (args.length == 2) {
            PlayerModel p;
            Aspect aspect;
            try {
                aspect = Aspects.valueOf(args[1].toUpperCase());
                p = DGGame.PLAYER_R.fromName(args[0]);
            } catch (Exception ignored) {
                sender.sendMessage(TextColors.RED + "Wrong player or deity! Please try a little harder.");
                return CommandResult.QUIET_ERROR;
            }

            p.removeAspect(aspect);
            sender.sendMessage(TextColors.YELLOW + "You removed " + aspect.getGroup().getName() + " " + aspect.getTier().name() + " from " + p.getLastKnownName() + ".");
            p.setExperience(aspect, 0.0);
            return CommandResult.SUCCESS;
        }
        return CommandResult.INVALID_SYNTAX;
    }
    */
}
