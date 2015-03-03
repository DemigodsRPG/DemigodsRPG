package com.demigodsrpg.game.command;

import com.censoredsoftware.library.util.CommonSymbol;
import com.censoredsoftware.library.util.MapUtil2;
import com.censoredsoftware.library.util.StringUtil2;
import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.command.type.BaseCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.google.common.base.Optional;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;

import java.util.List;
import java.util.Map;

public class ValuesCommand extends BaseCommand {
    @Override
    public CommandResult onCommand(CommandSource sender, String command, List<String> args) {
        // Define variables
        Player player = (Player) sender;
        int count = 0;

        if (DGGame.TRIBUTE_R.getTributeValuesMap().isEmpty()) {
            sender.sendMessage(TextColors.RED + "There are currently no tributes on record.");
            return CommandResult.QUIET_ERROR;
        }

        // Send header
        sender.sendMessage(StringUtil2.chatTitle("Current High Value Tributes"));
        sender.sendMessage(" ");

        for (Map.Entry<ItemType, Integer> entry : MapUtil2.sortByValue(DGGame.TRIBUTE_R.getTributeValuesMap(), true).entrySet()) {
            // Handle count
            if (count >= 10) break;
            count++;

            // Display value
            sender.sendMessage(TextColors.GRAY + " " + CommonSymbol.RIGHTWARD_ARROW + " " + TextColors.YELLOW + StringUtil2.beautify(entry.getKey().getId()) + TextColors.GRAY + " (currently worth " + TextColors.GREEN + entry.getValue() + TextColors.GRAY + " per item)");
        }

        sender.sendMessage(" ");
        sender.sendMessage(TextColors.GRAY + "" + TextStyles.ITALIC + "Values are constantly changing based on how players");
        sender.sendMessage(TextColors.GRAY + "" + TextStyles.ITALIC + "tribute, so check back often!");

        if (player.getItemInHand().isPresent()) {
            sender.sendMessage(" ");
            sender.sendMessage(TextColors.GRAY + "The " + (player.getItemInHand().get().getQuantity() == 1 ? "item in your hand is" : "items in your hand are") + " worth " + TextColors.GREEN + DGGame.TRIBUTE_R.getValue(player.getItemInHand().get()) + TextColors.GRAY + " in total.");
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
