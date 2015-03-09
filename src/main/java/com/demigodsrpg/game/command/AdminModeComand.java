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

package com.demigodsrpg.game.command;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.command.type.BaseCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.model.PlayerModel;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminModeComand extends BaseCommand {
    @Override
    protected CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (sender instanceof Player && sender.hasPermission("demigods.admin")) {
            PlayerModel model = DGGame.PLAYER_R.fromPlayer((Player) sender);
            if (model.getAdminMode()) {
                sender.sendMessage(ChatColor.YELLOW + "Demigods admin mode disabled.");
            } else {
                sender.sendMessage(ChatColor.YELLOW + "Demigods admin mode enabled.");
            }
            model.setAdminMode(!model.getAdminMode());

            return CommandResult.SUCCESS;
        }

        return CommandResult.NO_PERMISSIONS;
    }
}
