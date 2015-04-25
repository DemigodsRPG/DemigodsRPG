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

package com.demigodsrpg.game.command.type;

import com.demigodsrpg.data.DGData;
import com.demigodsrpg.data.model.PlayerModel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public abstract class AdminPlayerCommand extends BaseCommand implements TabCompleter {
    @SuppressWarnings("deprecation")
    @Override
    public CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (args.length > 0) {
            PlayerModel model = DGData.PLAYER_R.fromName(args[0]);
            if (model != null) {
                return onCommand(sender, model, args);
            }
        }
        return CommandResult.INVALID_SYNTAX;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return DGData.PLAYER_R.getNameStartsWith(args[0]);
        }
        return new ArrayList<>();
    }

    protected abstract CommandResult onCommand(CommandSender sender, PlayerModel model, String[] args);
}
