package com.demigodsrpg.game.command;

import com.censoredsoftware.library.util.StringUtil2;
import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.command.type.BaseCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.util.ColorUtil;
import com.google.common.base.Optional;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.source.ConsoleSource;

import java.util.List;

public class CheckCommand extends BaseCommand {
    @Override
    public CommandResult onCommand(CommandSource sender, String command, List<String> args) {
        if (sender instanceof ConsoleSource) {
            return CommandResult.PLAYER_ONLY;
        }
        Player player = (Player) sender;
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);
        player.sendMessage(StringUtil2.chatTitle("Player Stats"));
        player.sendMessage(TextColors.YELLOW + "You are the offspring of " + model.getGod().getName() + " and " + model.getHero().getName() + "."); // TODO Colors
        player.sendMessage(TextColors.YELLOW + "You are allied with the " + StringUtil2.beautify(model.getFaction().getName()) + " faction.");
        player.sendMessage(TextColors.YELLOW + "You have " + ColorUtil.getColor(player.getHealth(), player.getMaxHealth()) + TextStyles.ITALIC + player.getHealth() + " / " + player.getMaxHealth() + TextColors.YELLOW + " health.");
        if (!model.getAspects().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String deityName : model.getAspects()) {
                Aspect aspect = Aspects.valueOf(deityName);
                builder.append(aspect.getGroup().getColor()).append(aspect.getGroup().getName()).append(" ").append(aspect.getTier().name()).append(TextColors.RESET).append(", ");
            }
            String aspects = builder.toString();
            aspects = aspects.substring(0, aspects.length() - 4) + ".";
            player.sendMessage("Your aspects: " + aspects);
        }
        player.sendMessage("Favor: " + model.getFavor());
        player.sendMessage("Total Devotion: " + model.getTotalExperience());
        player.sendMessage("Number of ascensions: " + model.getLevel());
        player.sendMessage("Use " + TextStyles.ITALIC + "/binds" + TextColors.RESET + " for a list of all ability binds.");

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
