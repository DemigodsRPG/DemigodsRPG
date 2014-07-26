package com.demigodsrpg.demigods.classic.listener;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.deity.Deity;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PlayerModel model = DGClassic.PLAYER_R.fromPlayer(event.getPlayer());
        ;
        if (model == null) {
            model = new PlayerModel(event.getPlayer());
            DGClassic.PLAYER_R.register(model);

            model.giveMajorDeity(Deity.ZEUS);
            model.giveDeity(Deity.HEPHAESTUS);
        }
    }
}
