package com.demigodsrpg.game.area;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.util.JsonSection;
import com.demigodsrpg.game.util.LocationUtil;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FactionTerritory extends Area {
    private final String uuid;
    private final Faction faction;

    private FactionTerritory(Faction faction, AreaPriority priority, List<Location> corners) {
        super(priority, corners);
        this.uuid = UUID.randomUUID().toString();
        this.faction = faction;
    }

    public FactionTerritory(String id, final JsonSection conf) {
        super(AreaPriority.valueOf(conf.getString("priority")), new ArrayList<Location>() {{
            addAll(conf.getStringList("locations").stream().map(LocationUtil::locationFromString).collect(Collectors.toList()));
        }});
        this.uuid = id.split("\\$")[2];
        this.faction = DGGame.FACTION_R.factionFromName(id.split("\\$")[1]);
    }

    public Faction getFaction() {
        return faction;
    }

    @Override
    public String getPersistentId() {
        return "faction$" + getFaction().getName() + "$" + uuid;
    }
}
