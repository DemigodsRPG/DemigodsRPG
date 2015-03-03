package com.demigodsrpg.game.listener;

public class InventoryListener {

    /* FIXME This event isn't functional at all!
    @Subscribe(order = Order.LAST)
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getViewer();

        // Shrine Select
        String name = event.getContainer().getName().getTranslation().get();
        if (name.startsWith(ShrineGUI.INVENTORY_NAME)) {
            try {
                int count = Integer.parseInt(name.split(" ")[2]);
                ShrineGUI gui = new ShrineGUI(player);
                SlotFunction function = gui.getFunction(event.getContainer().);
                if (!SlotFunction.NO_FUNCTION.equals(function) && event.getContainer(). != null && !event.getCurrentItem().getType().equals(Material.AIR)) {
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
                                player.sendMessage(TextColors.YELLOW + "You have warped to " + shrineId + ".");
                            } else {
                                player.closeInventory();
                                player.sendMessage(TextColors.RED + "Something is wrong with " + shrineId + "...");
                            }
                            break;
                    }
                }
            } catch (Exception oops) {
                oops.printStackTrace();
                player.sendMessage(TextColors.RED + "Something went wrong...");
            }
        }
    }
    */
}
