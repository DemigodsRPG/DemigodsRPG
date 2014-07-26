package com.demigodsrpg.demigods.classic.registry;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.model.SpawnModel;
import com.demigodsrpg.demigods.classic.util.YamlFileUtil;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.Set;

public class SpawnRegistry extends AbstractRegistry<IDeity.Alliance, SpawnModel> {
    public static final String FILE_NAME = "spawns.dgc";

    public Location getSpawn(final IDeity.Alliance alliance) {
        SpawnModel point = Iterables.find(getRegistered(), new Predicate<SpawnModel>() {
            @Override
            public boolean apply(SpawnModel spawnPoint) {
                return spawnPoint.getAlliance().equals(alliance);
            }
        }, null);
        if (point == null) {
            return Bukkit.getWorlds().get(0).getSpawnLocation();
        }
        return point.getLocation();
    }

    public Set<SpawnModel> getFileData() {
        // Grab the current file.
        FileConfiguration data = YamlFileUtil.getConfiguration(DGClassic.SAVE_PATH, FILE_NAME);

        // Convert the raw file data into more usable data, in map form.
        Set<SpawnModel> set = new HashSet<>();
        for (String stringId : data.getKeys(false)) {
            try {
                set.add(new SpawnModel(IDeity.Alliance.valueOf(stringId), data.getConfigurationSection(stringId)));
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
        final Set<SpawnModel> currentFileData = getFileData();

        // Create/overwrite a configuration section if new data exists.
        for (SpawnModel model : Collections2.filter(getRegistered(), new Predicate<SpawnModel>() {
            @Override
            public boolean apply(SpawnModel key) {
                return !currentFileData.contains(key);
            }
        }))
            currentFile.createSection(model.getAlliance().name(), model.serialize());

        // Remove old unneeded data.
        for (SpawnModel model : Collections2.filter(currentFileData, new Predicate<SpawnModel>() {
            @Override
            public boolean apply(SpawnModel key) {
                return !getRegistered().contains(key);
            }
        }))
            currentFile.set(model.getAlliance().name(), null);

        // Save the file!
        return YamlFileUtil.saveFile(DGClassic.SAVE_PATH, FILE_NAME, currentFile);
    }
}
