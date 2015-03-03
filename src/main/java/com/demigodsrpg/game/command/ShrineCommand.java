package com.demigodsrpg.game.command;

import com.demigodsrpg.game.command.type.BaseCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.gui.ShrineGUI;
import com.google.common.base.Optional;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.source.ConsoleSource;

import java.util.List;

public class ShrineCommand extends BaseCommand {
    @Override
    public CommandResult onCommand(CommandSource sender, String command, List<String> args) {
        if (sender instanceof ConsoleSource) {
            return CommandResult.PLAYER_ONLY;
        }

        try {
            Player player = (Player) sender;
            Inventory inventory = new ShrineGUI(player).getInventory();
            if (inventory == null) {
                player.sendMessage(TextColors.YELLOW + "You don't have any shrines yet!");
                return CommandResult.QUIET_ERROR;
            }
            player.openInventory(inventory);
        } catch (Exception oops) {
            oops.printStackTrace();
            return CommandResult.ERROR;
        }

        return CommandResult.SUCCESS;
    }

    @Override
    public boolean testPermission(CommandSource commandSource) {
        return false;
    }

    @Override
    public Optional<String> getShortDescription() {
        return null;
    }

    @Override
    public Optional<String> getHelp() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public List<String> getSuggestions(CommandSource commandSource, String s) throws CommandException {
        return null;
    }
}
