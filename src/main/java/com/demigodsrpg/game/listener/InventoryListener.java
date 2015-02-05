package com.demigodsrpg.game.listener;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.gui.ChooseDeityGUI;
import com.demigodsrpg.game.gui.ShrineGUI;
import com.demigodsrpg.game.gui.SlotFunction;
import com.demigodsrpg.game.model.ShrineModel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        // Shrine Select
        if (event.getInventory().getName().startsWith(ShrineGUI.INVENTORY_NAME)) {
            try {
                int count = Integer.parseInt(event.getInventory().getName().split(" ")[2]);
                ShrineGUI gui = new ShrineGUI(player);
                SlotFunction function = gui.getFunction(event.getSlot());
                if (!SlotFunction.NO_FUNCTION.equals(function) && event.getCurrentItem() != null && !event.getCurrentItem().getType().equals(Material.AIR)) {
                    event.setCancelled(true);
                    switch (function) {
                        case NEXT_PAGE:
                            player.openInventory(gui.getInventory(count + 1));
                            break;
                        case PREVIOUS_PAGE:
                            player.openInventory(gui.getInventory(count - 1));
                            break;
                        case WARP:
                            String shrineId = event.getCurrentItem().getItemMeta().getDisplayName();
                            ShrineModel model = DGGame.SHRINE_R.fromId(shrineId);
                            if (model != null) {
                                player.closeInventory();
                                player.teleport(model.getSafeTeleport());
                                player.sendMessage(ChatColor.YELLOW + "You have warped to " + shrineId + ".");
                            } else {
                                player.closeInventory();
                                player.sendMessage(ChatColor.RED + "Something is wrong with " + shrineId + "...");
                            }
                            break;
                    }
                }
            } catch (Exception oops) {
                oops.printStackTrace();
                player.sendMessage(ChatColor.RED + "Something went wrong...");
            }
        }

        // Deity Select
        if (event.getInventory().getName().startsWith(ChooseDeityGUI.INVENTORY_NAME)) {
            try {
                int count = Integer.parseInt(event.getInventory().getName().split(" ")[2]);
                ChooseDeityGUI gui = new ChooseDeityGUI(player);
                SlotFunction function = gui.getFunction(event.getSlot());
                if (!SlotFunction.NO_FUNCTION.equals(function) && event.getCurrentItem() != null && !event.getCurrentItem().getType().equals(Material.AIR)) {
                    event.setCancelled(true);
                    switch (function) {
                        case NEXT_PAGE:
                            player.openInventory(gui.getInventory(count + 1));
                            break;
                        case PREVIOUS_PAGE:
                            player.openInventory(gui.getInventory(count - 1));
                            break;
                        case RUN_COMMAND:
                            String deityName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                            Aspect aspect = Aspect.valueOf(deityName);
                            if (aspect != null) {
                                player.closeInventory();
                                player.performCommand("deity claim " + deityName);
                            } else {
                                player.closeInventory();
                                player.sendMessage(ChatColor.RED + "Something is wrong with " + deityName + "...");
                            }
                            break;
                    }
                }
            } catch (Exception oops) {
                oops.printStackTrace();
                player.sendMessage(ChatColor.RED + "Something went wrong...");
            }
        }
    }
}
