package com.demigodsrpg.game.command;

public class BindsCommand /* extends BaseCommand */ {
    /*
    @Override
    public CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            return CommandResult.PLAYER_ONLY;
        }

        PlayerModel player = DGGame.PLAYER_R.fromPlayer((Player) sender);

        sender.sendMessage(StringUtil2.chatTitle("Binds"));
        if (!player.getBindsMap().isEmpty()) {
            for (Map.Entry<String, String> bind : player.getBindsMap().entrySet()) {
                AbilityMetaData ability = DGGame.ABILITY_R.fromCommand(bind.getKey());
                if (ability != null) {
                    String materialName = bind.getValue();
                    sender.sendMessage(" - " + ability.getAspect().getGroup().getColor() + ability.getName() +
                            TextColors.WHITE + ", bound to " + StringUtil2.beautify(materialName) + ".");
                }
            }
        } else {
            sender.sendMessage("You have no currently bound abilities.");
        }

        return CommandResult.SUCCESS;
    }
    */
}
