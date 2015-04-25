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

package com.demigodsrpg.game;

import com.demigodsrpg.util.LibraryHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class DGBukkitPlugin extends JavaPlugin {

    // -- LIBRARY HANDLER -- //

    private static LibraryHandler LIBRARIES;

    // -- BUKKIT ENABLE/DISABLE METHODS -- //

    @Override
    public void onEnable() {
        // Get and load the libraries
        LIBRARIES = new LibraryHandler(this);

        // Censored Libs
        LIBRARIES.addMavenLibrary(LibraryHandler.DG_MG, Depends.COM_CS, Depends.CS_SCHEMATIC, Depends.CS_VER);
        LIBRARIES.addMavenLibrary(LibraryHandler.DG_MG, Depends.COM_CS, Depends.CS_UTIL, Depends.CS_VER);
        LIBRARIES.addMavenLibrary(LibraryHandler.DG_MG, Depends.COM_CS, Depends.CS_BUKKIT_UTIL, Depends.CS_VER);

        // Demigods RPG Libs
        LIBRARIES.addMavenLibrary(LibraryHandler.DG_MG, Depends.COM_DG, Depends.DG_UTIL, Depends.DG_UTIL_VER);
        LIBRARIES.addMavenLibrary(LibraryHandler.DG_MG, Depends.COM_DG, Depends.DG_DATA, Depends.DG_DATA_VER);

        // Enable
        new DGGame(this);
    }

    @Override
    public void onDisable() {
        // Disable
        DGGame.getInst().onDisable(this);
    }
}
