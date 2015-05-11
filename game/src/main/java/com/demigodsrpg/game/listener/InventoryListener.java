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

package com.demigodsrpg.game.listener;

import com.demigodsrpg.data.DGData;
import com.demigodsrpg.data.model.ShrineModel;
import com.demigodsrpg.game.gui.ShrineGUI;
import com.demigodsrpg.util.SlotFunction;
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
        }
    }
}
