package com.demigodsrpg.game.listener;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.Setting;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.deity.Deity;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.model.ShrineModel;
import com.demigodsrpg.game.util.TargetingUtil;
import com.demigodsrpg.game.util.ZoneUtil;
import com.google.common.base.Optional;
import org.spongepowered.api.entity.EntityInteractionType;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.entity.living.player.PlayerInteractEvent;
import org.spongepowered.api.event.inventory.InventoryCloseEvent;
import org.spongepowered.api.item.inventory.Inventories;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.util.event.Order;
import org.spongepowered.api.util.event.Subscribe;
import org.spongepowered.api.world.Location;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class TributeListener {
    @Subscribe(order = Order.LAST)
    public void onTributeInteract(PlayerInteractEvent event) {
        if (ZoneUtil.inNoDGZone(event.getPlayer().getLocation())) return;

        // Define the location
        Location location;

        // Return from actions we don't care about
        if (!EntityInteractionType.RIGHT_CLICK.equals(event.getInteractionType())) {
            return;
        }

        // Define location
        if (!event.getClickedPosition().isPresent()) {
            location = TargetingUtil.directTarget(event.getPlayer());
        } else {
            location = new Location(event.getPlayer().getLocation().getExtent(), event.getClickedPosition().get().toDouble());
        }

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
                event.getPlayer().sendMessage(TextColors.YELLOW + "You must be allied with " + deity.getFaction().getColor() + deity.getName() + TextColors.YELLOW + " to tribute here.");
                return;
            }
            tribute(event.getPlayer(), shrine);
        }
    }

    @SuppressWarnings("RedundantCast")
    @Subscribe(order = Order.LAST)
    public void onPlayerTribute(InventoryCloseEvent event) {
        if (ZoneUtil.inNoDGZone(event.getViewer().getLocation())) return;

        // Define player and character
        Player player = (Player) event.getViewer();
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);

        // Make sure they are immortal
        if (!model.isDemigod()) return;

        // Get the shrine
        ShrineModel save = DGGame.SHRINE_R.getShrine(TargetingUtil.directTarget(player));

        // If it isn't a tribute chest then break the method
        if (!event.getContainer().getName().getTranslation().get().contains("Tribute to") || save == null)
            return;

        // Calculate the tribute value
        int tributeValue = 0, items = 0;
        Inventory inventory = event.getContainer();
        while (inventory.peek().isPresent()) {
            Optional<ItemStack> item = inventory.poll();
            if (item.isPresent()) {
                tributeValue += DGGame.TRIBUTE_R.processTribute(item.get());
                items += item.get().getQuantity();
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
                        player.sendMessage(TextColors.YELLOW + "You have been blessed with " + TextStyles.ITALIC + (model.getFavor() - favorBefore) + TextColors.YELLOW + " favor.");
                } else {
                    if (model.getExperience(aspect) > devotionBefore) {
                        // Message the tributer
                        player.sendMessage(save.getDeity().getFaction().getColor() + "Your devotion for " + aspect.getGroup() + " " + aspect.getTier().name() + " has increased by " + TextStyles.ITALIC + (model.getExperience(aspect) - devotionBefore) + "!");
                    }
                }
            }

            DGGame.PLAYER_R.register(model);

            // Define the shrine owner
            if (save.getOwnerMojangId() != null && DGGame.PLAYER_R.fromId(save.getOwnerMojangId()) != null) {
                PlayerModel shrineOwner = DGGame.PLAYER_R.fromId(save.getOwnerMojangId());
                Player shrineOwnerPlayer = shrineOwner.getPlayer();

                if (shrineOwner.getFavor() < (int) Setting.FAVOR_CAP.get() && !model.getMojangId().equals(shrineOwner.getMojangId())) {
                    // Give them some of the blessings
                    shrineOwner.setFavor(shrineOwner.getFavor() + tributeValue / 5);

                    // Message them
                    if (shrineOwnerPlayer.isOnline()) {
                        shrineOwnerPlayer.sendMessage(save.getDeity().getFaction().getColor() + "Someone has recently paid tribute at a shrine you own.");
                    }
                }

                DGGame.PLAYER_R.register(shrineOwner);
            }
        }

        // Handle messaging and Shrine owner updating
        if (tributeValue < 1) {
            // They aren't good enough, let them know!
            player.sendMessage(TextColors.RED + "Your tributes were insufficient for " + save.getDeity().getFaction().getColor() + save.getDeity().getName() + "'s" + TextColors.RED + " blessings.");
        } else {
            player.sendMessage(save.getDeity().getFaction().getColor() + save.getDeity().getName() + " is pleased with your tribute.");
        }

        // Clear the tribute case
        event.getContainer().clear();
    }

    private static void tribute(Player player, ShrineModel save) {
        Deity shrineDeity = save.getDeity();

        // Open the tribute inventory
        Inventory ii = Inventories.customInventoryBuilder().size(27)/*.name("Tribute to " + shrineDeity.getFaction().getColor() + shrineDeity.getName() + TextColors.RESET + "." */.build();

        player.openInventory(ii);
    }
}