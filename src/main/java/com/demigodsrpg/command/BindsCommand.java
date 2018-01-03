/*
 * Copyright 2015 Demigods RPG
 * Copyright 2015 Alexander Chauncey
 * Copyright 2015 Alex Bennett
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
                AbilityMetaData ability = AbilityRegistry.fromCommand(bind.getKey());
                if (ability != null) {
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
