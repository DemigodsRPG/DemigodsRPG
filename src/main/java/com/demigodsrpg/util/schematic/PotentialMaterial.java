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

import java.util.ArrayList;
import java.util.List;

public class PotentialMaterial {
    private String material;
    private byte data;
    private int odds;
    private boolean physics;

    /**
     * Constructor for StoaMaterialData with only Material given.
     *
     * @param material Material of the block.
     */
    public PotentialMaterial(String material) {
        this.material = material;
        this.data = 0;
        this.odds = 100;
        this.physics = false;
    }

    /**
     * Constructor for StoaMaterialData with only Material given.
     *
     * @param material Material of the block.
     * @param physics  If physics is on.
     */
    public PotentialMaterial(String material, boolean physics) {
        this.material = material;
        this.data = 0;
        this.odds = 100;
        this.physics = physics;
    }

    /**
     * Constructor for StoaMaterialData with only Material given and odds given.
     *
     * @param material Material of the block.
     * @param odds     The odds of this object being generated.
     */
    public PotentialMaterial(String material, int odds) {
        if (odds == 0 || odds > 100) throw new PotentialMaterialException();
        this.material = material;
        this.data = 100;
        this.odds = odds;
        this.physics = false;
    }

    /**
     * Constructor for StoaMaterialData with only Material given and odds given.
     *
     * @param material Material of the block.
     * @param odds     The odds of this object being generated.
     * @param physics  If physics is on.
     */
    public PotentialMaterial(String material, int odds, boolean physics) {
        if (odds == 0 || odds > 100) throw new PotentialMaterialException();
        this.material = material;
        this.data = 100;
        this.odds = odds;
        this.physics = physics;
    }

    /**
     * Constructor for StoaMaterialData with only Material and byte data given.
     *
     * @param material Material of the block.
     * @param data     Byte data of the block.
     */
    public PotentialMaterial(String material, byte data) {
        this.material = material;
        this.data = data;
        this.odds = 100;
        this.physics = false;
    }

    /**
     * Constructor for StoaMaterialData with only Material and byte data given.
     *
     * @param material Material of the block.
     * @param data     Byte data of the block.
     * @param physics  If physics is on.
     */
    public PotentialMaterial(String material, byte data, boolean physics) {
        this.material = material;
        this.data = data;
        this.odds = 100;
        this.physics = physics;
    }

    /**
     * Constructor for StoaMaterialData with Material, byte data, and odds given.
     *
     * @param material Material of the block.
     * @param data     Byte data of the block.
     * @param odds     The odds of this object being generated.
     */
    public PotentialMaterial(String material, byte data, int odds) {
        if (odds == 0 || odds > 100) throw new PotentialMaterialException();
        this.material = material;
        this.data = data;
        this.odds = odds;
        this.physics = false;
    }

    /**
     * Constructor for StoaMaterialData with Material, byte data, and odds given.
     *
     * @param material Material of the block.
     * @param data     Byte data of the block.
     * @param odds     The odds of this object being generated.
     * @param physics  If physics is on.
     */
    public PotentialMaterial(String material, byte data, int odds, boolean physics) {
        if (odds == 0 || odds > 100) throw new PotentialMaterialException();
        this.material = material;
        this.data = data;
        this.odds = odds;
        this.physics = physics;
    }

    /**
     * Get the Material of this object.
     *
     * @return A Material.
     */
    public String getMaterial() {
        return this.material;
    }

    /**
     * Get the byte data of this object.
     *
     * @return Byte data.
     */
    public byte getData() {
        return this.data;
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
                add(new PotentialMaterial("SMOOTH_BRICK", 80));
                add(new PotentialMaterial("SMOOTH_BRICK", (byte) 1, 10));
                add(new PotentialMaterial("SMOOTH_BRICK", (byte) 2, 10));
            }
        }), SANDY_GRASS(new ArrayList<PotentialMaterial>(2) {
            {
                add(new PotentialMaterial("SAND", 65));
                add(new PotentialMaterial("GRASS", 35));
            }
        }), PRETTY_FLOWERS_AND_GRASS(new ArrayList<PotentialMaterial>(4) {
            {
                add(new PotentialMaterial("AIR", 50));
                add(new PotentialMaterial("LONG_GRASS", (byte) 1, 35, true));
                add(new PotentialMaterial("YELLOW_FLOWER", 9, true));
                add(new PotentialMaterial("RED_ROSE", 6, true));
            }
        }), VINE_1(new ArrayList<PotentialMaterial>(2) {
            {
                add(new PotentialMaterial("VINE", (byte) 1, 40));
                add(new PotentialMaterial("AIR", 60));
            }
        }), VINE_4(new ArrayList<PotentialMaterial>(2) {
            {
                add(new PotentialMaterial("VINE", (byte) 4, 40));
                add(new PotentialMaterial("AIR", 60));
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
