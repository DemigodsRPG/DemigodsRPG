package com.demigodsrpg.demigods.classic.command;

import com.censoredsoftware.library.util.StringUtil2;
import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.ability.Ability;
import com.demigodsrpg.demigods.classic.ability.AbilityMetaData;
import com.demigodsrpg.demigods.classic.command.type.BaseCommand;
import com.demigodsrpg.demigods.classic.command.type.CommandResult;
import com.demigodsrpg.demigods.classic.deity.Deity;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.gui.ChooseDeityGUI;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class DeityCommand extends BaseCommand {
    @Override
    protected CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        // Player only
        if (sender instanceof ConsoleCommandSender) {
            return CommandResult.PLAYER_ONLY;
        }

        // Get the player
        final Player player = (Player) sender;
        final PlayerModel model = DGClassic.PLAYER_R.fromPlayer(player);

        // Deity list
        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + StringUtil2.chatTitle("Deity List"));
            for (Deity deity : Deity.values()) {
                player.sendMessage(" - " + deity.getColor() + deity.getDeityName() + ": " + deity.getInfo() + " (" + StringUtil2.beautify(deity.getDefaultAlliance().name()) + ")");
            }
            return CommandResult.SUCCESS;
        }

        if (args.length == 1) {
            // Claim
            switch (args[0].toLowerCase()) {
                case "claim": {
                    try {
                        Inventory inventory = new ChooseDeityGUI(player).getInventory();
                        if (inventory == null) {
                            if (model.getAlliance().equals(IDeity.Alliance.EXCOMMUNICATED)) {
                                player.sendMessage(ChatColor.YELLOW + "Your current alliance prevents you from claiming new deities.");
                            } else {
                                player.sendMessage(ChatColor.RED + "There are *currently* no deities you can choose from.");
                                player.sendMessage(ChatColor.YELLOW + "You need " + model.costForNextDeity() + " ascensions to claim again."); // TODO Fix this for there being no more deities left
                            }
                            return CommandResult.QUIET_ERROR;
                        }
                        player.openInventory(inventory);
                        return CommandResult.SUCCESS;
                    } catch (Exception oops) {
                        oops.printStackTrace();
                        return CommandResult.ERROR;
                    }
                }
            }

            // Deity info
            try {
                Deity deity = Deity.valueOf(args[0].toUpperCase());
                player.sendMessage(deity.getColor() + StringUtil2.chatTitle(deity.getDeityName() + " Info"));
                player.sendMessage(" - Info: " + deity.getInfo());
                player.sendMessage(" - Alliance: " + StringUtil2.beautify(deity.getDefaultAlliance().name()));

                for (AbilityMetaData ability : DGClassic.ABILITY_R.getAbilities(deity)) {
                    player.sendMessage(" " + ability.getName() + ":");
                    player.sendMessage(ability.getInfo());

                    player.sendMessage(" - Type: " + StringUtil2.beautify(ability.getType().name()));
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
                    final Deity deity = Deity.valueOf(deityName.toUpperCase());

                    if (deity != null && model.canClaim(deity)) {
                        // Check if the importance is none
                        if (IDeity.Importance.NONE.equals(deity.getImportance())) {
                            // TODO CANNOT CLAIM DEITIES THAT AREN'T IMPORTANT
                            player.sendMessage(ChatColor.RED + "You cannot claim this deity.");
                            return CommandResult.QUIET_ERROR;

                            // Give the deity
                            // model.giveMajorDeity(deity, true);

                            // Message them
                            // player.sendMessage(deity.getColor() + "The " + StringUtil2.beautify(deity.getDefaultAlliance().name()) + " alliance welcomes you, " + deity.getNomen() + ".");

                            // Fancy particles
                            // for (int i = 0; i < 20; i++)
                            //    player.getWorld().spawn(player.getLocation(), ExperienceOrb.class);
                        }
                        // Check if the importance is major
                        else if (IDeity.Importance.MAJOR.equals(deity.getImportance()) && !model.getMajorDeity().getImportance().equals(IDeity.Importance.MAJOR)) {
                            // Pondering message
                            player.sendMessage(deity.getColor() + deity.getDeityName() + ChatColor.GRAY + " is pondering your choice...");

                            // Play scary sound
                            player.playSound(player.getLocation(), Sound.AMBIENCE_CAVE, 0.6F, 1F);

                            // Delay for dramatic effect
                            Bukkit.getScheduler().scheduleSyncDelayedTask(DGClassic.getInst(), new BukkitRunnable() {
                                @Override
                                public void run() {
                                    // Give the deity
                                    model.giveMajorDeity(deity, true);

                                    // Play acceptance sound
                                    player.playSound(player.getLocation(), Sound.ENDERDRAGON_DEATH, 1F, 1F);

                                    // Message them and do cool things
                                    player.sendMessage(deity.getColor() + "The " + StringUtil2.beautify(deity.getDefaultAlliance().name()) + " alliance welcomes you, " + deity.getNomen() + ".");
                                    player.getWorld().strikeLightningEffect(player.getLocation());

                                    // Fancy particles
                                    for (int i = 0; i < 20; i++)
                                        player.getWorld().spawn(player.getLocation(), ExperienceOrb.class);
                                }
                            }, 60);
                        } else {
                            // Pondering message
                            player.sendMessage(deity.getColor() + deity.getDeityName() + ChatColor.GRAY + " is pondering your choice...");

                            // Play scary sound
                            player.playSound(player.getLocation(), Sound.AMBIENCE_CAVE, 0.6F, 1F);

                            // Delay for dramatic effect
                            Bukkit.getScheduler().scheduleSyncDelayedTask(DGClassic.getInst(), new BukkitRunnable() {
                                @Override
                                public void run() {
                                    // Give the deity
                                    model.giveDeity(deity);

                                    // Play acceptance sound
                                    player.playSound(player.getLocation(), Sound.ENDERDRAGON_DEATH, 1F, 1F);

                                    // Message them and do cool things
                                    player.sendMessage(deity.getColor() + "You have been accepted as an " + deity.getNomen() + ".");
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
