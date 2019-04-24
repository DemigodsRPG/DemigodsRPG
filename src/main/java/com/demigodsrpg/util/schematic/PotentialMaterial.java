/*
 * Copyright 2014 Alex Bennett & Alexander Chauncey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demigodsrpg.util.schematic;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

public class PotentialMaterial {
    private Material data;
    private BlockFace face;
    private int odds;
    private boolean physics;

    /**
     * Constructor for StoaMaterialData with only Material and byte data given.
     *
     * @param material Material of the block.
     */
    public PotentialMaterial(Material material) {
        this.data = material;
        this.odds = 100;
        this.physics = false;
    }

    /**
     * Constructor for StoaMaterialData with only Material and byte data given.
     *
     * @param material Material of the block.
     */
    public PotentialMaterial(Material material, BlockFace face) {
        this.data = material;
        this.face = face;
        this.odds = 100;
        this.physics = false;
    }

    /**
     * Constructor for StoaMaterialData with only Material and byte data given.
     *
     * @param material Material of the block.
     * @param physics  If physics is on.
     */
    public PotentialMaterial(Material material, boolean physics) {
        this.data = material;
        this.odds = 100;
        this.physics = physics;
    }

    /**
     * Constructor for StoaMaterialData with only Material and byte data given.
     *
     * @param material Material of the block.
     * @param physics  If physics is on.
     */
    public PotentialMaterial(Material material, BlockFace face, boolean physics) {
        this.data = material;
        this.face = face;
        this.odds = 100;
        this.physics = physics;
    }

    /**
     * Constructor for StoaMaterialData with Material, byte data, and odds given.
     *
     * @param material Material of the block.
     * @param odds     The odds of this object being generated.
     */
    public PotentialMaterial(Material material, int odds) {
        if (odds == 0 || odds > 100) throw new PotentialMaterialException();
        this.data = material;
        this.face = face;
        this.odds = odds;
        this.physics = false;
    }

    /**
     * Constructor for StoaMaterialData with Material, byte data, and odds given.
     *
     * @param material Material of the block.
     * @param odds     The odds of this object being generated.
     */
    public PotentialMaterial(Material material, BlockFace face, int odds) {
        if (odds == 0 || odds > 100) throw new PotentialMaterialException();
        this.data = material;
        this.face = face;
        this.odds = odds;
        this.physics = false;
    }

    /**
     * Constructor for StoaMaterialData with Material, byte data, and odds given.
     *
     * @param material Material of the block.
     * @param odds     The odds of this object being generated.
     * @param physics  If physics is on.
     */
    public PotentialMaterial(Material material, int odds, boolean physics) {
        if (odds == 0 || odds > 100) throw new PotentialMaterialException();
        this.data = material;
        this.odds = odds;
        this.physics = physics;
    }

    /**
     * Constructor for StoaMaterialData with Material, byte data, and odds given.
     *
     * @param material Material of the block.
     * @param odds     The odds of this object being generated.
     * @param physics  If physics is on.
     */
    public PotentialMaterial(Material material, BlockFace face, int odds, boolean physics) {
        if (odds == 0 || odds > 100) throw new PotentialMaterialException();
        this.data = material;
        this.face = face;
        this.odds = odds;
        this.physics = physics;
    }

    /**
     * Get the Material of this object.
     *
     * @return A Material.
     */
    public Material getMaterial() {
        return this.data;
    }

    /**
     * Get the block face of this object.
     *
     * @return Block face.
     */
    public BlockFace getFace() {
        return this.face;
    }

    /**
     * Get the odds of this object generating.
     *
     * @return Odds (as an integer, out of 5).
     */
    public int getOdds() {
        return this.odds;
    }

    /**
     * Get the physics boolean.
     *
     * @return If physics should apply on generation.
     */
    public boolean getPhysics() {
        return this.physics;
    }

    public enum Preset {
        STONE_BRICK(new ArrayList<PotentialMaterial>(3) {
            {
                add(new PotentialMaterial(Material.STONE_BRICKS, 80));
                add(new PotentialMaterial(Material.CRACKED_STONE_BRICKS, 10));
                add(new PotentialMaterial(Material.CHISELED_STONE_BRICKS, 10));
            }
        }), SANDY_GRASS(new ArrayList<PotentialMaterial>(2) {
            {
                add(new PotentialMaterial(Material.SAND, 65));
                add(new PotentialMaterial(Material.GRASS_BLOCK, 35));
            }
        }), PRETTY_FLOWERS_AND_GRASS(new ArrayList<PotentialMaterial>(4) {
            {
                add(new PotentialMaterial(Material.AIR, 50));
                add(new PotentialMaterial(Material.TALL_GRASS, 35, true));
                add(new PotentialMaterial(Material.DANDELION, 9, true));
                add(new PotentialMaterial(Material.ROSE_BUSH, 6, true));
            }
        }), VINE(new ArrayList<PotentialMaterial>(2) {
            {
                add(new PotentialMaterial(Material.VINE, 40));
                add(new PotentialMaterial(Material.AIR, 60));
            }
        });

        private List<PotentialMaterial> data;

        Preset(List<PotentialMaterial> data) {
            this.data = data;
        }

        public List<PotentialMaterial> getData() {
            return data;
        }
    }
}
