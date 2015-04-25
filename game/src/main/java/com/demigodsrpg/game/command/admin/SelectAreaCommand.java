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

package com.demigodsrpg.game.command.admin;

import com.demigodsrpg.data.DGData;
import com.demigodsrpg.data.area.AreaSelection;
import com.demigodsrpg.data.model.PlayerModel;
import com.demigodsrpg.game.command.type.BaseCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SelectAreaCommand extends BaseCommand {

    @Override
    protected CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (sender instanceof Player) {
            PlayerModel model = DGData.PLAYER_R.fromPlayer((Player) sender);
            if (model.getAdminMode()) {
                AreaSelection selection = new AreaSelection((Player) sender);
                selection.register();
                AreaSelection.AREA_SELECTION_CACHE.put(model.getMojangId(), selection);

                sender.sendMessage(ChatColor.YELLOW + "You may now make a selection.");

                // TODO Disabling a selection

            } else {
                return CommandResult.NO_PERMISSIONS;
            }
        } else {
            return CommandResult.PLAYER_ONLY;
        }

        return CommandResult.SUCCESS;
    }
}
