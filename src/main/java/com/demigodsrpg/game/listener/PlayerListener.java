package com.demigodsrpg.game.listener;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.model.PlayerModel;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.entity.living.player.PlayerChatEvent;
import org.spongepowered.api.event.net.PlayerConnectionEvent;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.event.Order;
import org.spongepowered.api.util.event.Subscribe;
import org.spongepowered.api.util.event.callback.AbstractEventCallback;

import java.util.Set;

public class PlayerListener {
    @Subscribe(order = Order.EARLY) // FIXME This is any connection, I'm not sure this works how we want
    public void onJoin(PlayerConnectionEvent event) {
        Player player = event.getConnection().getPlayer();
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);

        if (player.isOnline()) {
            player.sendMessage("Welcome!");
            if (model.getAspects().isEmpty()) {
                player.sendMessage("TEMP: " + TextColors.GRAY + "Use " + TextColors.YELLOW + "/aspect claim" + TextColors.GRAY + " to claim an aspect.");
            }
        }
    }

    @Subscribe(order = Order.FIRST)
    public void onPlayerChat(PlayerChatEvent event) {
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(event.getPlayer());
        if (DGGame.MISC_R.contains("faction_chat", event.getPlayer().getUniqueId().toString()) && !Faction.NEUTRAL.equals(model.getFaction()) && !Faction.EXCOMMUNICATED.equals(model.getFaction())) {
            event.getCallbacks().clear();
            Set<PlayerModel> playerModelSet = DGGame.PLAYER_R.getOnlineInAlliance(model.getFaction());
            for (PlayerModel playerModel : playerModelSet) {
                event.getCallbacks().add(new AbstractEventCallback() {
                    @Override
                    public void run() {
                        // TODO This is super temp, I'm not even sure it works
                        // FIXME Sponge needs to get their shit together so I actually know what I'm supposed to do to set recipients.
                        playerModel.getPlayer().sendMessage("<" + event.getPlayer().getDisplayName() + "> " + event.getMessage());
                    }
                });
            }
        }
    }
}
