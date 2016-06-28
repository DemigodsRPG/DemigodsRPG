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

package com.demigodsrpg;

import com.demigodsrpg.util.LibraryHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class DGBukkitPlugin extends JavaPlugin {

    // -- LIBRARY HANDLER -- //

    private static LibraryHandler LIBRARIES;

    // -- BUKKIT ENABLE/DISABLE METHODS -- //

    @Override
    public void onEnable() {
        // Config
        getConfig().options().copyDefaults(true);
        saveConfig();

        // Get and load the libraries
        LIBRARIES = new LibraryHandler(this);

        // Evo-inflector
        LIBRARIES.addMavenLibrary(LibraryHandler.MAVEN_CENTRAL, Depends.ORG_ATTEO, Depends.EVO, Depends.EVO_VER);

        // PostgreSQL & Iciql Libs
        if (getConfig().getBoolean("psql.use", false)) {
            LIBRARIES.addMavenLibrary(LibraryHandler.MAVEN_CENTRAL, Depends.COM_ICIQL, Depends.ICIQL, Depends.ICIQL_VER);
            LIBRARIES.addMavenLibrary(LibraryHandler.MAVEN_CENTRAL, Depends.ORG_PSQL, Depends.PSQL, Depends.PSQL_VER);
        }

        // Enable
        new DGGame(this);
    }

    @Override
    public void onDisable() {
        // Disable
        DGGame.getInst().onDisable(this);
    }
}
