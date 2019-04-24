package com.demigodsrpg.shrine;

import com.demigodsrpg.util.schematic.Schematic;
import com.demigodsrpg.util.schematic.Selection;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;


public class OverworldShrine extends Schematic implements IShrine {
    public OverworldShrine() {
        super("Overworld Shrine", "HmmmQuestionMark", 6);

        // Create the main block
        add(new Selection(0, 1, 0, Material.GOLD_BLOCK));

        // Create the ender chest and the block below
        add(new Selection(0, 0, 0, Material.ENDER_CHEST));
        add(new Selection(0, -1, 0, Material.STONE_BRICKS));

        // Create the rest
        add(new Selection(-1, 0, 0, Material.STONE_BRICK_STAIRS, BlockFace.WEST));
        add(new Selection(1, 0, 0, Material.STONE_BRICK_STAIRS, BlockFace.EAST));
        add(new Selection(0, 0, -1, Material.STONE_BRICK_STAIRS, BlockFace.SOUTH));
        add(new Selection(0, 0, 1, Material.STONE_BRICK_STAIRS, BlockFace.NORTH));

        // Safe zone
        add(new Selection(1, -1, 1, Material.STONE_BRICKS));
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
