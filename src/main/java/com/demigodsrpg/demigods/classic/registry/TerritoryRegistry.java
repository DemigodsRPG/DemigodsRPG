package com.demigodsrpg.demigods.classic.registry;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.territory.Territory;
import com.demigodsrpg.demigods.classic.util.YamlFileUtil;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.Set;

public class TerritoryRegistry extends AbstractRegistry<IDeity.Alliance, Territory> {
    private final World WORLD;
    private final String FILE_NAME;

    public TerritoryRegistry(World world) {
        WORLD = world;
        FILE_NAME = world.getName() + ".territories.dgc";
    }

    public World getWorld() {
        return WORLD;
    }

    public Set<Territory> getFileData() {
        // Grab the current file.
        FileConfiguration data = YamlFileUtil.getConfiguration(DGClassic.SAVE_PATH, FILE_NAME);

        // Convert the raw file data into more usable data, in map form.
        Set<Territory> set = new HashSet<>();
        for (String stringId : data.getKeys(false)) {
            try {
                set.add(new Territory(IDeity.Alliance.valueOf(stringId), data.getConfigurationSection(stringId)));
            } catch (Exception oops) {
                oops.printStackTrace();
            }
        }
        return set;
    }

    public void registerFromFile() {
        register(getFileData());
    }

    public boolean saveToFile() {
        // Grab the current file, and its data as a usable map.
        FileConfiguration currentFile = YamlFileUtil.getConfiguration(DGClassic.SAVE_PATH, FILE_NAME);
        final Set<Territory> currentFileData = getFileData();

        // Create/overwrite a configuration section if new data exists.
        for (Territory model : Collections2.filter(getRegistered(), new Predicate<Territory>() {
            @Override
            public boolean apply(Territory key) {
                return !currentFileData.contains(key);
            }
        }))
            currentFile.createSection(model.getAlliance().name(), model.serialize());

        // Remove old unneeded data.
        for (Territory model : Collections2.filter(currentFileData, new Predicate<Territory>() {
            @Override
            public boolean apply(Territory key) {
                return !getRegistered().contains(key);
            }
        }))
            currentFile.set(model.getAlliance().name(), null);

        // Save the file!
        return YamlFileUtil.saveFile(DGClassic.SAVE_PATH, FILE_NAME, currentFile);
    }
}

