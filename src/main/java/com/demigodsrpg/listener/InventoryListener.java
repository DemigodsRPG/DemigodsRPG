package com.demigodsrpg.listener;

import com.demigodsrpg.DGData;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Aspects;
import com.demigodsrpg.gui.AspectGUI;
import com.demigodsrpg.gui.ShrineGUI;
import com.demigodsrpg.model.PlayerModel;
import com.demigodsrpg.model.ShrineModel;
import com.demigodsrpg.util.SlotFunction;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
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
                String function = gui.getFunction(event.getSlot());
                if (!SlotFunction.NO_FUNCTION.equals(function) && event.getCurrentItem() != null && !event.getCurrentItem().getType().equals(Material.AIR)) {
                    event.setCancelled(true);
                    switch (function) {
                        case SlotFunction.NEXT_PAGE:
                            player.openInventory(gui.getInventory(count + 1));
                            break;
                        case SlotFunction.PREVIOUS_PAGE:
                            player.openInventory(gui.getInventory(count - 1));
                            break;
                        case SlotFunction.WARP:
                            String shrineId = event.getCurrentItem().getItemMeta().getDisplayName();
                            ShrineModel model = DGData.SHRINE_R.fromId(shrineId);
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
        } else if (event.getInventory().getName().startsWith(AspectGUI.INVENTORY_NAME)) {
            try {
                int count = Integer.parseInt(event.getInventory().getName().split(" ")[2]);
                AspectGUI gui = new AspectGUI(player);
                String function = gui.getFunction(event.getSlot());
                if (!SlotFunction.NO_FUNCTION.equals(function) && event.getCurrentItem() != null && !event.getCurrentItem().getType().equals(Material.AIR)) {
                    event.setCancelled(true);
                    switch (function) {
                        case SlotFunction.NEXT_PAGE:
                            player.openInventory(gui.getInventory(count + 1));
                            break;
                        case SlotFunction.PREVIOUS_PAGE:
                            player.openInventory(gui.getInventory(count - 1));
                            break;
                        case SlotFunction.CLAIM:
                            String aspectName = event.getCurrentItem().getItemMeta().getDisplayName();
                            Aspect aspect = Aspects.valueOf(aspectName);
                            if (aspect != null) {
                                player.closeInventory();
                                PlayerModel model = DGData.PLAYER_R.fromPlayer(player);
                                model.addAspect(aspect);
                                player.sendMessage(ChatColor.YELLOW + "You have claimed " + aspectName + ".");
                                player.getWorld().strikeLightningEffect(player.getLocation());
                            } else {
                                player.closeInventory();
                                player.sendMessage(ChatColor.RED + "Something is wrong with " + aspectName + "...");
                            }
                            break;
                        case SlotFunction.LOCKED: {
                            // Nope.avi
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        }
                    }
                }
            } catch (Exception oops) {
                oops.printStackTrace();
                player.sendMessage(ChatColor.RED + "Something went wrong...");
            }
        }
    }
}
