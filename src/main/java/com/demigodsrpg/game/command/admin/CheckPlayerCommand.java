package com.demigodsrpg.game.command.admin;

import com.censoredsoftware.library.util.StringUtil2;
import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.command.type.AdminPlayerCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.util.ColorUtil;
import com.google.common.base.Optional;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.util.command.CommandSource;

import java.util.List;

public class CheckPlayerCommand extends AdminPlayerCommand {
    @Override
    public CommandResult onCommand(CommandSource sender, PlayerModel model, List<String> args) {
        if (args.size() == 1) {
            Player p = null;
            try {
                p = DGGame.PLAYER_R.fromName(args.get(0)).getPlayer();
            } catch (Exception ignored) {
                sender.sendMessage(TextColors.RED + "Player is not real, but we appreciate the attempt!");
                return CommandResult.QUIET_ERROR;
            }
            if (p.isOnline()) {
                sendInfo(p, sender);
            }
            return CommandResult.SUCCESS;
        }
        return CommandResult.INVALID_SYNTAX;
    }

    private void sendInfo(Player p, CommandSource s) {
        PlayerModel model = DGGame.PLAYER_R.fromId(p.getUniqueId().toString());
        s.sendMessage(StringUtil2.chatTitle("Player Stats"));
        s.sendMessage(p.getName() + " is an offspring of " + model.getGod() + " and " + model.getHero() + "."); // TODO Colors
        s.sendMessage(p.getName() + " is a member of the " + StringUtil2.beautify(model.getFaction().getName()) + " faction.");
        if (p.isOnline())
            s.sendMessage(p.getName() + " has " + ColorUtil.getColor(p.getHealth(), p.getMaxHealth()) + TextStyles.ITALIC + p.getHealth() + " / " + p.getMaxHealth() + TextColors.YELLOW + " health.");
        if (!model.getAspects().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String deityName : model.getAspects()) {
                Aspect aspect = Aspects.valueOf(deityName);
                builder.append(aspect.getGroup().getColor()).append(aspect.getGroup()).append(" ").append(aspect.getTier().name()).append(TextColors.RESET).append(", ");
            }
            String minorDeities = builder.toString();
            minorDeities = minorDeities.substring(0, minorDeities.length() - 4);
            s.sendMessage(p.getName() + " is also allied with: " + minorDeities);
        }
        s.sendMessage("Favor: " + model.getFavor());
        s.sendMessage("Total Devotion: " + model.getTotalExperience());
        s.sendMessage("Number of ascensions: " + model.getLevel());
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
}
