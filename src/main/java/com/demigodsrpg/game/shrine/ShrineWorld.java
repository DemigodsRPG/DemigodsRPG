package com.demigodsrpg.game.shrine;

import com.censoredsoftware.library.schematic.Point;
import com.censoredsoftware.library.schematic.PotentialMaterial;
import com.censoredsoftware.library.schematic.World;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class ShrineWorld implements World {
    private final org.bukkit.World world;

    public ShrineWorld(org.bukkit.World world) {
        this.world = world;
    }

    public ShrineWorld(String worldName) {
        this.world = Bukkit.getWorld(worldName);
    }

    @Override
    public PotentialMaterial getMaterialAt(int x, int y, int z) {
        Block block = world.getBlockAt(x, y, z);
        return new PotentialMaterial(block.getType().name(), block.getData());
    }

    @Override
    public void setPoint(Point point, PotentialMaterial material) {
        Block block = world.getBlockAt(point.getX(), point.getY(), point.getZ());
        block.setType(Material.valueOf(material.getMaterial()));
        block.setData(material.getData());
    }

    @Override
    public String getName() {
        return world.getName();
    }

    public org.bukkit.World getBukkitWorld() {
        return world;
    }
}
