package com.demigodsrpg.game.command;

import com.censoredsoftware.library.util.StringUtil2;
import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityMetaData;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.command.type.BaseCommand;
import com.demigodsrpg.game.command.type.CommandResult;
import com.demigodsrpg.game.model.PlayerModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AspectCommand extends BaseCommand {
    @Override
    protected CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        // Player only
        if (sender instanceof ConsoleCommandSender) {
            return CommandResult.PLAYER_ONLY;
        }

        // Get the player
        final Player player = (Player) sender;
        final PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);

        // Deity list
        if (args.length == 0) {
            // FIXME Display the deities not aspects
            player.sendMessage(ChatColor.YELLOW + StringUtil2.chatTitle("Aspect List"));
            for (Aspect aspect : Aspects.values()) {
                player.sendMessage(" - " + aspect.getGroup().getColor() + aspect.getGroup().getName() + " " + aspect.getTier().name() + ": " + aspect.getInfo() /* FIXME + " (" + StringUtil2.beautify(aspect.getDefaultAlliance().name()) + ")" */);
            }
            return CommandResult.SUCCESS;
        }

        if (args.length == 1) {
            // Aspect info
            try {
                Aspect aspect = Aspects.valueOf(args[0].toUpperCase());
                player.sendMessage(aspect.getGroup().getColor() + StringUtil2.chatTitle(aspect.getGroup() + " Info"));
                player.sendMessage(" - Info: " + aspect.getInfo());
                // FIXME player.sendMessage(" - Deity: " + StringUtil2.beautify(aspect.getDefaultAlliance().name()));

                for (AbilityMetaData ability : DGGame.ABILITY_R.getAbilities(aspect)) {
                    player.sendMessage(" " + ability.getName() + ":");
                    player.sendMessage(ability.getInfo());

                    player.sendMessage(" - Type: " + StringUtil2.beautify(ability.getType().name()));
                    if (!ability.getCommand().equals("")) {
                        player.sendMessage(" - Command: " + "/" + ability.getCommand());
                    }
                    if (!ability.getType().equals(Ability.Type.PASSIVE)) {
                        player.sendMessage(" - Cost: " + ability.getCost());
                    }
                    if (ability.getCooldown() > 0) {
                        player.sendMessage(" - Cooldown (ms): " + ability.getCooldown());
                    }
                    player.sendMessage(" ");
                }

                return CommandResult.SUCCESS;
            } catch (NullPointerException ignored) {
            }
        }

        if (args.length == 2) {
            // Get the subcommand
            switch (args[0].toLowerCase()) {
                case "claim": {
                    String deityName = args[1];
                    final Aspect aspect = Aspects.valueOf(deityName.toUpperCase());

                    if (aspect != null && model.canClaim(aspect)) {
                        // Check if the aspect is a hero tier
                        if (Aspect.Tier.HERO.equals(aspect.getTier())) {
                            player.sendMessage(ChatColor.RED + "You cannot claim this aspect.");
                            return CommandResult.QUIET_ERROR;
                        }
                        // Check if the tier is I
                        else if (Aspect.Tier.I.equals(aspect.getTier())) {
                            // Pondering message
                            player.sendMessage(aspect.getGroup().getColor() + aspect.getGroup().getName() + ChatColor.GRAY + " is pondering your choice...");

                            // Play scary sound
                            player.playSound(player.getLocation(), Sound.AMBIENCE_CAVE, 0.6F, 1F);

                            // Delay for dramatic effect
                            Bukkit.getScheduler().scheduleSyncDelayedTask(DGGame.getInst(), new BukkitRunnable() {
                                @Override
                                public void run() {
                                    // Give the aspect
                                    // FIXME model.giveFirstAspect(hero, aspect);

                                    // Play acceptance sound
                                    player.playSound(player.getLocation(), Sound.ENDERDRAGON_DEATH, 1F, 1F);

                                    // Message them and do cool things
                                    // FIXME player.sendMessage(aspect.getColor() + "The " + StringUtil2.beautify(aspect.getDefaultAlliance().name()) + " alliance welcomes you, " + aspect.getNomen() + ".");
                                    player.getWorld().strikeLightningEffect(player.getLocation());

                                    // Fancy particles
                                    for (int i = 0; i < 20; i++)
                                        player.getWorld().spawn(player.getLocation(), ExperienceOrb.class);
                                }
                            }, 60);
                        } else {
                            // Pondering message
                            // FIXME Display a more fitting message
                            player.sendMessage(aspect.getGroup().getColor() + aspect.getGroup().getName() + ChatColor.GRAY + " is pondering your choice...");

                            // Play scary sound
                            player.playSound(player.getLocation(), Sound.AMBIENCE_CAVE, 0.6F, 1F);

                            // Delay for dramatic effect
                            Bukkit.getScheduler().scheduleSyncDelayedTask(DGGame.getInst(), new BukkitRunnable() {
                                @Override
                                public void run() {
                                    // Give the deity
                                    model.giveAspect(aspect);

                                    // Play acceptance sound
                                    player.playSound(player.getLocation(), Sound.ENDERDRAGON_DEATH, 1F, 1F);

                                    // Message them and do cool things
                                    // FIXME player.sendMessage(aspect.getColor() + "You have been accepted as an " + aspect.getNomen() + ".");
                                    player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, 4F);

                                    // Fancy particles
                                    for (int i = 0; i < 20; i++)
                                        player.getWorld().spawn(player.getLocation(), ExperienceOrb.class);
                                }
                            }, 60);
                        }

                        return CommandResult.SUCCESS;
                    }
                }
            }
        }

        return CommandResult.QUIET_ERROR;
    }
}
