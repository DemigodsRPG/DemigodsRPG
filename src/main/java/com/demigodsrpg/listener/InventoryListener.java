/*
 * Copyright 2015 Demigods RPG
 * Copyright 2015 Alexander Chauncey
 * Copyright 2015 Alex Bennett
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
