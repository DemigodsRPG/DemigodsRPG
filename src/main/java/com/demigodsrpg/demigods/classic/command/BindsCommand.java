package com.demigodsrpg.demigods.classic.command;

import com.censoredsoftware.library.util.StringUtil2;
import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.ability.AbilityMetaData;
import com.demigodsrpg.demigods.classic.command.type.BaseCommand;
import com.demigodsrpg.demigods.classic.command.type.CommandResult;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class BindsCommand extends BaseCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            return CommandResult.PLAYER_ONLY;
        }

        PlayerModel player = DGClassic.PLAYER_R.fromPlayer((Player) sender);

        sender.sendMessage(StringUtil2.chatTitle("Binds"));
        if (!player.getBindsMap().isEmpty()) {
            for (Map.Entry<String, String> bind : player.getBindsMap().entrySet()) {
                AbilityMetaData ability = DGClassic.ABILITY_R.fromCommand(bind.getKey());
                if (ability != null) {
                    String materialName = bind.getValue();
                    sender.sendMessage(" - " + ability.getDeity().getColor() + ability.getName() +
                            ChatColor.WHITE + ", bound to " + StringUtil2.beautify(materialName) + ".");
                }
            }
        } else {
            sender.sendMessage("You have no currently bound abilities.");
        }

        return CommandResult.SUCCESS;
    }
}
