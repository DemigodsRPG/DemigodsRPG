package com.demigodsrpg.command;

import com.demigodsrpg.DGData;
import com.demigodsrpg.ability.AbilityMetaData;
import com.demigodsrpg.ability.AbilityRegistry;
import com.demigodsrpg.command.type.BaseCommand;
import com.demigodsrpg.command.type.CommandResult;
import com.demigodsrpg.model.PlayerModel;
import com.demigodsrpg.util.misc.StringUtil2;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

public class BindsCommand extends BaseCommand {
    @Override
    public CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            return CommandResult.PLAYER_ONLY;
        }

        PlayerModel player = DGData.PLAYER_R.fromPlayer((Player) sender);

        sender.sendMessage(StringUtil2.chatTitle("Binds"));
        if (!player.getBindsMap().isEmpty()) {
            for (Map.Entry<String, String> bind : player.getBindsMap().entrySet()) {
                Optional<AbilityMetaData> abilityOptional = AbilityRegistry.fromCommand(bind.getKey());
                if (abilityOptional.isPresent()) {
                    AbilityMetaData ability = abilityOptional.get();
                    String materialName = bind.getValue();
                    sender.sendMessage(" - " + ability.getAspect().getGroup().getColor() + ability.getName() +
                            ChatColor.WHITE + ", bound to " + StringUtil2.beautify(materialName) + ".");
                }
            }
        } else {
            sender.sendMessage("You have no currently bound abilities.");
        }

        return CommandResult.SUCCESS;
    }
}
