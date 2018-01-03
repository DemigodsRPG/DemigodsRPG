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
