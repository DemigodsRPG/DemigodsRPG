package com.demigodsrpg.demigods.classic.shrine;

import com.censoredsoftware.library.schematic.Schematic;
import com.censoredsoftware.library.schematic.Selection;
import org.bukkit.Location;
import org.bukkit.Material;

public class OverworldShrine extends Schematic implements IShrine {
    private Location location;

    public OverworldShrine() {
        super("Overworld Shrine", "HmmmQuestionMark", 6);

        // Create the main block
        add(new Selection(0, 1, 0, Material.GOLD_BLOCK));

        // Create the ender chest and the block below
        add(new Selection(0, 0, 0, Material.ENDER_CHEST));
        add(new Selection(0, -1, 0, Material.SMOOTH_BRICK));

        // Create the rest
        add(new Selection(-1, 0, 0, Material.SMOOTH_STAIRS));
        add(new Selection(1, 0, 0, Material.SMOOTH_STAIRS, (byte) 1));
        add(new Selection(0, 0, -1, Material.SMOOTH_STAIRS, (byte) 2));
        add(new Selection(0, 0, 1, Material.SMOOTH_STAIRS, (byte) 3));

        // Safe zone
        add(new Selection(1, -1, 1, Material.SMOOTH_BRICK));
        add(new Selection(1, 0, 1, Material.AIR));
        add(new Selection(1, 1, 1, Material.AIR));
    }

    @Override
    public Location getClickable(Location reference) {
        return reference.clone().add(0, 1, 0);
    }

    @Override
    public Location getSafeTeleport(Location reference) {
        return reference.clone().add(1.5, 0, 1.5);
    }
}
