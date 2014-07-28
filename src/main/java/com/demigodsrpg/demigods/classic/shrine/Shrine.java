package com.demigodsrpg.demigods.classic.shrine;

import org.bukkit.Location;

import java.util.Collection;

public enum Shrine implements IShrine {
    OVERWORLD(new OverworldShrine()), NETHER(new NetherShrine());

    private IShrine parent;

    private Shrine(IShrine parent) {
        this.parent = parent;
    }

    public void generate(Location reference) {
        parent.generate(reference);
    }

    public Location getClickable(Location reference) {
        return parent.getClickable(reference);
    }

    public Location getSafeTeleport(Location reference) {
        return parent.getSafeTeleport(reference);
    }

    public int getGroundRadius() {
        return parent.getGroundRadius();
    }

    @Override
    public Collection<Location> getLocations(Location reference) {
        return parent.getLocations(reference);
    }

    public enum Status {
        HOLY, SCORN, CORRUPT
    }
}
