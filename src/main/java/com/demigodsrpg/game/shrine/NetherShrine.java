package com.demigodsrpg.game.shrine;

import com.censoredsoftware.library.schematic.Point;
import com.censoredsoftware.library.schematic.Schematic;
import com.censoredsoftware.library.schematic.Selection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Collection;
import java.util.stream.Collectors;

public class NetherShrine extends Schematic implements IShrine {
    public NetherShrine() {
        super("Nether Shrine", "HmmmQuestionMark", 6);

        // Create the main block
        add(new Selection(0, 1, 0, Material.GOLD_BLOCK.name()));

        // Create the ender chest and the block below
        add(new Selection(0, 0, 0, Material.ENDER_CHEST.name()));
        add(new Selection(0, -1, 0, Material.NETHER_BRICK.name()));

        // Create the rest
        add(new Selection(-1, 0, 0, Material.NETHER_BRICK_STAIRS.name()));
        add(new Selection(1, 0, 0, Material.NETHER_BRICK_STAIRS.name(), (byte) 1));
        add(new Selection(0, 0, -1, Material.NETHER_BRICK_STAIRS.name(), (byte) 2));
        add(new Selection(0, 0, 1, Material.NETHER_BRICK_STAIRS.name(), (byte) 3));

        // Safe zone
        add(new Selection(1, -1, 1, Material.NETHER_BRICK.name()));
        add(new Selection(1, 0, 1, Material.AIR.name()));
        add(new Selection(1, 1, 1, Material.AIR.name()));
    }

    @Override
    public void generate(Location reference) {
        generate(new Point(reference.getBlockX(), reference.getBlockY(), reference.getBlockZ(), new ShrineWorld(reference.getWorld())));
    }

    @Override
    public Location getClickable(Location reference) {
        return reference.clone().add(0, 1, 0);
    }

    @Override
    public Location getSafeTeleport(Location reference) {
        return reference.clone().add(1.5, 0, 1.5);
    }

    @Override
    public Collection<Location> getLocations(Location reference) {
        return getLocations(new Point(reference.getBlockX(), reference.getBlockY(), reference.getBlockZ(), new ShrineWorld(reference.getWorld()))).stream().
                map(point -> new Location(Bukkit.getWorld(point.getWorld().getName()), point.getX(), point.getY(), point.getZ())).collect(Collectors.toList());
    }
}
