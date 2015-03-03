package com.demigodsrpg.game.integration.chitchat;

public class FactionChatTag /* extends PlayerTag */ {
    /* @Override
    public String getName() {
        return "faction_chat";
    }

    @Override
    public String getFor(Player player) {
        if (isInChat(player)) {
            return ChatColor.DARK_GRAY + "[.]";
        }
        return ChatColor.DARK_RED + "[!]";
    }

    @Override
    public boolean cancelBungee(Player player) {
        return isInChat(player);
    }

    @Override
    public int getPriority() {
        return 0;
    }

    private boolean isInChat(Player player) {
        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);
        return DGGame.MISC_R.contains("faction_chat", player.getUniqueId().toString()) && !Faction.NEUTRAL.equals(model.getFaction()) && !Faction.EXCOMMUNICATED.equals(model.getFaction());
    } */
}
