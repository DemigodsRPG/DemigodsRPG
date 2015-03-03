package com.demigodsrpg.game.shrine;

import org.spongepowered.api.world.Location;

import java.util.List;

public enum Shrine implements IShrine {
    OVERWORLD(new OverworldShrine()), NETHER(new NetherShrine());

    private final IShrine parent;

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
    public List<Location> getLocations(Location reference) {
        return parent.getLocations(reference);
    }

    public enum Status {
        HOLY, SCORN, CORRUPT
    }
}
