package com.demigodsrpg.demigods.classic.listener;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.deity.Deity;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Random;
import java.util.Set;

public class PlayerListener implements Listener {
    private final Random random = new Random();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        PlayerModel model = DGClassic.PLAYER_R.fromPlayer(event.getPlayer());

        // TODO REMOVE THIS DEBUG TESTING ONLY
        event.getPlayer().sendMessage("Welcome!");
        if (model.getMajorDeity().equals(Deity.HUMAN)) {
            event.getPlayer().sendMessage("Here, have a free deity (for science)!");

            model = new PlayerModel(event.getPlayer());
            DGClassic.PLAYER_R.register(model);

            if (random.nextBoolean()) {
                model.giveMajorDeity(Deity.ZEUS, true);
                model.giveDeity(Deity.HEPHAESTUS);
            } else if (random.nextBoolean()) {
                model.giveMajorDeity(Deity.CRONUS, true);
            } else {
                model.giveMajorDeity(Deity.POSEIDON, true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        PlayerModel model = DGClassic.PLAYER_R.fromPlayer(event.getPlayer());
        String format = event.getFormat();
        char symbol = !IDeity.Alliance.EXCOMMUNICATED.equals(model.getAlliance()) ? model.getAlliance().name().charAt(0) : 'X';
        if (DGClassic.SERV_R.contains("alliance_chat", event.getPlayer().getUniqueId().toString()) && !IDeity.Alliance.NEUTRAL.equals(model.getAlliance()) && !IDeity.Alliance.EXCOMMUNICATED.equals(model.getAlliance())) {
            event.getRecipients().clear();
            Set<PlayerModel> playerModelSet = DGClassic.PLAYER_R.getOnlineInAlliance(model.getAlliance());
            for (PlayerModel playerModel : playerModelSet) {
                event.getRecipients().add(playerModel.getOfflinePlayer().getPlayer());
            }
            event.setFormat(ChatColor.DARK_GRAY + "[.]" + model.getMajorDeity().getColor() + "[" + model.getMajorDeity().getColor() + symbol + "]" + format);
        } else {
            event.setFormat(ChatColor.DARK_RED + "[!]" + model.getMajorDeity().getColor() + "[" + model.getMajorDeity().getColor() + symbol + "]" + format);
        }
    }
}
