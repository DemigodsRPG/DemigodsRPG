package com.demigodsrpg.game.area;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.deity.Deity;
import com.demigodsrpg.game.util.JsonSection;
import com.demigodsrpg.game.util.LocationUtil;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ClaimRoom extends Area {
    private final String uuid;
    private final Deity deity;

    public ClaimRoom(Deity deity, AreaPriority priority, List<Location> corners) {
        super(priority, corners);
        this.uuid = UUID.randomUUID().toString();
        this.deity = deity;
    }

    public ClaimRoom(String id, JsonSection conf) {
        super(AreaPriority.valueOf(conf.getString("priority")), new ArrayList<Location>() {{
            addAll(conf.getStringList("locations").stream().map(LocationUtil::locationFromString).collect(Collectors.toList()));
        }});
        this.uuid = id.split("\\$")[2];
        this.deity = DGGame.DEITY_R.deityFromName(id.split("\\$")[1]);
    }

    @Override
    public String getPersistentId() {
        return "claimroom$" + deity.getName() + "$" + uuid;
    }

    public Deity getDeity() {
        return deity;
    }
}
