package com.demigodsrpg.game.shrine;

import com.censoredsoftware.library.schematic.Point;
import com.censoredsoftware.library.schematic.Schematic;
import com.censoredsoftware.library.schematic.Selection;
import com.demigodsrpg.game.DGGame;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.Location;

import java.util.List;
import java.util.stream.Collectors;

public class NetherShrine extends Schematic implements IShrine {
    public NetherShrine() {
        super("Nether Shrine", "HmmmQuestionMark", 6);

        // Create the main block
        add(new Selection(0, 1, 0, BlockTypes.GOLD_BLOCK.getId()));

        // Create the ender chest and the block below
        add(new Selection(0, 0, 0, BlockTypes.ENDER_CHEST.getId()));
        add(new Selection(0, -1, 0, BlockTypes.NETHER_BRICK.getId()));

        // Create the rest
        add(new Selection(-1, 0, 0, BlockTypes.NETHER_BRICK_STAIRS.getId()));
        add(new Selection(1, 0, 0, BlockTypes.NETHER_BRICK_STAIRS.getId(), (byte) 1));
        add(new Selection(0, 0, -1, BlockTypes.NETHER_BRICK_STAIRS.getId(), (byte) 2));
        add(new Selection(0, 0, 1, BlockTypes.NETHER_BRICK_STAIRS.getId(), (byte) 3));

        // Safe zone
        add(new Selection(1, -1, 1, BlockTypes.NETHER_BRICK.getId()));
        add(new Selection(1, 0, 1, BlockTypes.AIR.getId()));
        add(new Selection(1, 1, 1, BlockTypes.AIR.getId()));
    }

    @Override
    public void generate(Location reference) {
        generate(new Point(reference.getBlock().getX(), reference.getBlock().getY(), reference.getBlock().getZ(), new ShrineWorld(reference.getExtent())));
    }

    @Override
    public Location getClickable(Location reference) {
        return reference.add(0, 1, 0);
    }

    @Override
    public Location getSafeTeleport(Location reference) {
        return reference.add(1.5, 0, 1.5);
    }

    @Override
    public List<Location> getLocations(Location reference) {
        return getLocations(new Point(reference.getBlock().getX(), reference.getBlock().getY(), reference.getBlock().getZ(), new ShrineWorld(reference.getExtent()))).stream().
                map(point -> new Location(DGGame.SERVER.getWorld(point.getWorld().getName()).get(), new Vector3d(point.getX(), point.getY(), point.getZ()))).collect(Collectors.toList());
    }
}
