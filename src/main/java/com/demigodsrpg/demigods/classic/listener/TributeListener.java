package com.demigodsrpg.demigods.classic.listener;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.Setting;
import com.demigodsrpg.demigods.classic.deity.Deity;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import com.demigodsrpg.demigods.classic.model.ShrineModel;
import com.demigodsrpg.demigods.classic.util.ZoneUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TributeListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTributeInteract(PlayerInteractEvent event) {
        if (ZoneUtil.inNoDGCZone(event.getPlayer().getLocation())) return;

        // Define the location
        Location location = null;

        // Return from actions we don't care about
        if (!Action.RIGHT_CLICK_BLOCK.equals(event.getAction())) {
            if (Action.RIGHT_CLICK_AIR.equals(event.getAction())) {
                location = event.getPlayer().getTargetBlock(null, 10).getLocation();
            } else {
                return;
            }
        }

        // Define variables
        if (location == null) location = event.getClickedBlock().getLocation();
        PlayerModel model = DGClassic.PLAYER_R.fromPlayer(event.getPlayer());

        // Return if the player is mortal
        if (IDeity.Pantheon.MORTAL.equals(model.getMajorDeity().getPantheon())) return;

        // Define the shrine
        ShrineModel shrine = DGClassic.SHRINE_R.getShrine(location);
        if (shrine != null && shrine.getClickable().equals(location)) {
            // Cancel the interaction
            event.setCancelled(true);

            Deity deity = shrine.getDeity();
            if (shrine.getOwnerMojangId() != null && !model.getAllDeities().contains(deity)) {
                event.getPlayer().sendMessage(ChatColor.YELLOW + "You must be allied with " + deity.getColor() + deity.getDeityName() + ChatColor.YELLOW + " to tribute here.");
                return;
            }
            tribute(event.getPlayer(), shrine);
        }
    }

    @SuppressWarnings("RedundantCast")
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerTribute(InventoryCloseEvent event) {
        if (ZoneUtil.inNoDGCZone(event.getPlayer().getLocation())) return;

        // Define player and character
        Player player = (Player) event.getPlayer();
        PlayerModel model = DGClassic.PLAYER_R.fromPlayer(player);

        // Make sure they are immortal
        if (IDeity.Pantheon.MORTAL.equals(model.getMajorDeity().getPantheon())) return;

        // Get the shrine
        ShrineModel save = DGClassic.SHRINE_R.getShrine(player.getTargetBlock(null, 10).getLocation());

        // If it isn't a tribute chest then break the method
        if (!event.getInventory().getName().contains("Tribute to") || save == null)
            return;

        // Calculate the tribute value
        int tributeValue = 0, items = 0;
        for (ItemStack item : event.getInventory().getContents()) {
            if (item != null) {
                tributeValue += DGClassic.TRIBUTE_R.processTribute(item);
                items += item.getAmount();
            }
        }

        // Return if it's empty
        if (items == 0) return;

        // Handle the multiplier
        // tributeValue *= (double) Setting.EXP_MULTIPLIER.get();

        // Get the current favor for comparison
        double favorBefore = model.getFavor();
        double maxFavorBefore = model.getMaxFavor();
        double devotionBefore = model.getDevotion(save.getDeity());

        // Update the character's favor
        model.setFavor(favorBefore + tributeValue / 6);
        model.setMaxFavor(maxFavorBefore + tributeValue / 2);
        model.setDevotion(save.getDeity(), devotionBefore + tributeValue);

        DGClassic.PLAYER_R.register(model);

        // Define the shrine owner
        if (save.getOwnerMojangId() != null && DGClassic.PLAYER_R.fromId(save.getOwnerMojangId()) != null) {
            PlayerModel shrineOwner = DGClassic.PLAYER_R.fromId(save.getOwnerMojangId());
            OfflinePlayer shrineOwnerPlayer = shrineOwner.getOfflinePlayer();

            if (model.getMaxFavor() >= (int) Setting.FAVOR_CAP.get() && !player.getName().equals(shrineOwnerPlayer.getName())) {
                // Give them some of the blessings
                shrineOwner.setMaxFavor(shrineOwner.getMaxFavor() + tributeValue / 5);

                // Message them
                if (shrineOwnerPlayer.isOnline() && model.getMojangId().equals(shrineOwner.getMojangId())) {
                    ((Player) shrineOwnerPlayer).sendMessage(save.getDeity().getColor() + "Another " + save.getDeity().getNomen() + " has recently paid tribute at a shrine you own.");
                }
            } else if (model.getMaxFavor() > maxFavorBefore && !player.getName().equals(shrineOwnerPlayer.getName())) {
                // Define variables
                double ownerFavorBefore = shrineOwner.getMaxFavor();

                // Give them some of the blessings
                shrineOwner.setMaxFavor(shrineOwner.getMaxFavor() + tributeValue / 5);

                // Message them
                if (shrineOwnerPlayer.isOnline() && model.getMojangId().equals(shrineOwner.getMojangId())) {
                    ((Player) shrineOwnerPlayer).sendMessage(save.getDeity().getColor() + "Another " + save.getDeity().getNomen() + " has recently paid tribute at a shrine you own.");
                    if (shrineOwner.getMaxFavor() > ownerFavorBefore)
                        ((Player) shrineOwnerPlayer).sendMessage(ChatColor.YELLOW + "Your favor cap has increased to " + shrineOwner.getMaxFavor() + "!");
                }
            }

            DGClassic.PLAYER_R.register(shrineOwner);
        }

        // Handle messaging and Shrine owner updating
        if (items <= 0) {
            // They aren't good enough, let them know!
            player.sendMessage(ChatColor.RED + "Your tributes were insufficient for " + save.getDeity().getColor() + save.getDeity().getDeityName() + "'s" + ChatColor.RED + " blessings.");
        } else {
            player.sendMessage(save.getDeity().getColor() + save.getDeity().getDeityName() + " is pleased with your tribute.");
        }
        if (model.getMaxFavor() >= (int) Setting.FAVOR_CAP.get()) {
            // They have already met the max favor cap
            if (model.getFavor() > favorBefore)
                player.sendMessage(ChatColor.YELLOW + "You have been blessed with " + ChatColor.ITALIC + (model.getFavor() - favorBefore) + ChatColor.YELLOW + " instant favor.");
        } else {
            if (model.getMaxFavor() > maxFavorBefore) {
                // Message the tributer
                player.sendMessage(ChatColor.YELLOW + "Your favor cap has increased by " + ChatColor.ITALIC + (model.getFavor() - favorBefore) + ChatColor.YELLOW + "!");
            }
            if (model.getDevotion(save.getDeity()) > devotionBefore) {
                // Message the tributer
                player.sendMessage(save.getDeity().getColor() + "Your devotion to " + save.getDeity().getDeityName() + " has increased by " + ChatColor.ITALIC + (model.getDevotion(save.getDeity()) - devotionBefore) + "!");
            }
        }

        // Clear the tribute case
        event.getInventory().clear();
    }

    private static void tribute(Player player, ShrineModel save) {
        Deity shrineDeity = save.getDeity();

        // Open the tribute inventory
        Inventory ii = Bukkit.getServer().createInventory(player, 27, "Tribute to " + shrineDeity.getColor() + shrineDeity.getDeityName() + ChatColor.RESET + ".");
        player.openInventory(ii);
    }
}