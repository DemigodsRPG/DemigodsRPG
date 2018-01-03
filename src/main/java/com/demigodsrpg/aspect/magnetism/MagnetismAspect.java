/*
 * Copyright 2015 Demigods RPG
 * Copyright 2015 Alexander Chauncey
 * Copyright 2015 Alex Bennett
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demigodsrpg.aspect.magnetism;

import com.demigodsrpg.aspect.Aspect;
import org.bukkit.*;
import org.bukkit.material.MaterialData;

public class MagnetismAspect implements Aspect.Group {
    @Override
    public String getName() {
        return "Magnetism Aspect";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.BLUE;
    }

    @Override
    public Sound getSound() {
        return Sound.UI_BUTTON_CLICK;
    }

    @Override
    public MaterialData getClaimMaterial() {
        return new MaterialData(Material.IRON_INGOT);
    }
}
