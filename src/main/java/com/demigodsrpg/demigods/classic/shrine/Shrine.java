package com.demigodsrpg.demigods.classic.shrine;

import com.censoredsoftware.library.schematic.Point;
import org.bukkit.Location;

import java.util.Collection;

public enum Shrine implements IShrine {
    OVERWORLD(new OverworldShrine()), NETHER(new NetherShrine());

    private final IShrine parent;

    private Shrine(IShrine parent) {
        this.parent = parent;
    }

    public void generate(Point reference) {
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
    public Collection<Point> getLocations(Point reference) {
        return parent.getLocations(reference);
    }

    public enum Status {
        HOLY, SCORN, CORRUPT
    }
}
