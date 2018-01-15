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

        if (Setting.MONGODB_PERSISTENCE) {
            LIBRARIES.addMavenLibrary(LibraryHandler.MAVEN_CENTRAL, Depends.ORG_MONGO, Depends.MONGODB,
                    Depends.MONGODB_VER);
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
