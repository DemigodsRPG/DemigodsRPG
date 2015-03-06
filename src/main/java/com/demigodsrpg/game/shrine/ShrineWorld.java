package com.demigodsrpg.game.shrine;

import com.censoredsoftware.library.schematic.Point;
import com.censoredsoftware.library.schematic.PotentialMaterial;
import com.censoredsoftware.library.schematic.World;
import com.demigodsrpg.game.DGGame;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.extent.Extent;

public class ShrineWorld implements World {
    private Extent world;

    public ShrineWorld(Extent world) {
        this.world = world;
    }

    public ShrineWorld(String worldName) {
        if (DGGame.SERVER.getWorld(worldName).isPresent()) {
            this.world = DGGame.SERVER.getWorld(worldName).get();
        }
    }

    @Override
    public PotentialMaterial getMaterialAt(int x, int y, int z) {
        BlockLoc block = new Location(world, new Vector3d(x, y, z)).getBlock();
        return new PotentialMaterial(block.getType().getId(), block.getState().getDataValue());
    }

    @Override
    public void setPoint(Point point, PotentialMaterial material) {
        BlockLoc block = new Location(world, new Vector3d(point.getX(), point.getY(), point.getZ())).getBlock();
        BlockType type = DGGame.GAME.getRegistry().getBlock(material.getMaterial()).get();
        block.replaceWith(type);
        block.replaceWith(type.getStateFromDataValue(material.getData()));
    }

    @Override
    public String getName() {
        return getSpongeWorld().getName();
    }

    public org.spongepowered.api.world.World getSpongeWorld() {
        return (org.spongepowered.api.world.World) world;
    }
}
