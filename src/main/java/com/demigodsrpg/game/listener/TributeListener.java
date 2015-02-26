package com.demigodsrpg.game.listener;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.Setting;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.deity.Deity;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.model.ShrineModel;
import com.demigodsrpg.game.util.ZoneUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class TributeListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTributeInteract(PlayerInteractEvent event) {
        if (ZoneUtil.inNoDGZone(event.getPlayer().getLocation())) return;

        // Define the location
        Location location = null;

        // Return from actions we don't care about
        if (!Action.RIGHT_CLICK_BLOCK.equals(event.getAction())) {
            if (Action.RIGHT_CLICK_AIR.equals(event.getAction())) {
                location = event.getPlayer().getTargetBlock((Set<Material>) null, 10).getLocation();
            } else {
                return;
            }
        }

        // Define variables
        if (location == null) location = event.getClickedBlock().getLocation();
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(event.getPlayer());

        // Return if the player is mortal
        if (!model.isDemigod()) return;

        // Define the shrine
        ShrineModel shrine = DGGame.SHRINE_R.getShrine(location);
        if (shrine != null && shrine.getClickable().equals(location)) {
            // Cancel the interaction
            event.setCancelled(true);

            Deity deity = shrine.getDeity();
            if (shrine.getOwnerMojangId() != null && !model.hasDeity(deity)) {
                event.getPlayer().sendMessage(ChatColor.YELLOW + "You must be allied with " + deity.getFaction().getColor() + deity.getName() + ChatColor.YELLOW + " to tribute here.");
                return;
            }
            tribute(event.getPlayer(), shrine);
        }
    }

    @SuppressWarnings("RedundantCast")
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerTribute(InventoryCloseEvent event) {
        if (ZoneUtil.inNoDGZone(event.getPlayer().getLocation())) return;

        // Define player and character
        Player player = (Player) event.getPlayer();
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);

        // Make sure they are immortal
        if (!model.isDemigod()) return;

        // Get the shrine
        ShrineModel save = DGGame.SHRINE_R.getShrine(player.getTargetBlock((Set<Material>) null, 10).getLocation());

        // If it isn't a tribute chest then break the method
        if (!event.getInventory().getName().contains("Tribute to") || save == null)
            return;

        // Calculate the tribute value
        int tributeValue = 0, items = 0;
        for (ItemStack item : event.getInventory().getContents()) {
            if (item != null) {
                tributeValue += DGGame.TRIBUTE_R.processTribute(item);
                items += item.getAmount();
            }
        }

        // Return if it's empty
        if (items == 0) return;

        // Handle the multiplier
        // tributeValue *= (double) Setting.EXP_MULTIPLIER.get();

        // Get the aspects to level
        List<Aspect> aspectsToLevel = model.getAspects().stream().map(Aspects::valueOf).
                filter(aspect -> save.getDeity().getAspectGroups().contains(aspect.getGroup())).
                collect(Collectors.toList());

        // Make sure there are aspects to level
        if (!aspectsToLevel.isEmpty()) {
            // Divide up the value
            tributeValue /= aspectsToLevel.size();

            for (Aspect aspect : aspectsToLevel) {
                // Get the current favor for comparison
                double favorBefore = model.getFavor();
                double devotionBefore = model.getExperience(aspect);

                // Update the character's favor
                model.setFavor(favorBefore + tributeValue);
                model.setExperience(aspect, devotionBefore + tributeValue);

                if (model.getFavor() < (int) Setting.FAVOR_CAP.get()) {
                    if (model.getFavor() > favorBefore)
                        player.sendMessage(ChatColor.YELLOW + "You have been blessed with " + ChatColor.ITALIC + (model.getFavor() - favorBefore) + ChatColor.YELLOW + " favor.");
                } else {
                    if (model.getExperience(aspect) > devotionBefore) {
                        // Message the tributer
                        player.sendMessage(save.getDeity().getFaction().getColor() + "Your devotion for " + aspect.getGroup() + " " + aspect.getTier().name() + " has increased by " + ChatColor.ITALIC + (model.getExperience(aspect) - devotionBefore) + "!");
                    }
                }
            }

            DGGame.PLAYER_R.register(model);

            // Define the shrine owner
            if (save.getOwnerMojangId() != null && DGGame.PLAYER_R.fromId(save.getOwnerMojangId()) != null) {
                PlayerModel shrineOwner = DGGame.PLAYER_R.fromId(save.getOwnerMojangId());
                OfflinePlayer shrineOwnerPlayer = shrineOwner.getOfflinePlayer();

                if (shrineOwner.getFavor() < (int) Setting.FAVOR_CAP.get() && !model.getMojangId().equals(shrineOwner.getMojangId())) {
                    // Give them some of the blessings
                    shrineOwner.setFavor(shrineOwner.getFavor() + tributeValue / 5);

                    // Message them
                    if (shrineOwnerPlayer.isOnline()) {
                        ((Player) shrineOwnerPlayer).sendMessage(save.getDeity().getFaction().getColor() + "Someone has recently paid tribute at a shrine you own.");
                    }
                }

                DGGame.PLAYER_R.register(shrineOwner);
            }
        }

        // Handle messaging and Shrine owner updating
        if (tributeValue < 1) {
            // They aren't good enough, let them know!
            player.sendMessage(ChatColor.RED + "Your tributes were insufficient for " + save.getDeity().getFaction().getColor() + save.getDeity().getName() + "'s" + ChatColor.RED + " blessings.");
        } else {
            player.sendMessage(save.getDeity().getFaction().getColor() + save.getDeity().getName() + " is pleased with your tribute.");
        }

        // Clear the tribute case
        event.getInventory().clear();
    }

    private static void tribute(Player player, ShrineModel save) {
        Deity shrineDeity = save.getDeity();

        // Open the tribute inventory
        Inventory ii = Bukkit.getServer().createInventory(player, 27, "Tribute to " + shrineDeity.getFaction().getColor() + shrineDeity.getName() + ChatColor.RESET + ".");
        player.openInventory(ii);
    }
}