package com.demigodsrpg.demigods.classic.registry;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.model.ShrineModel;
import com.demigodsrpg.demigods.classic.util.YamlFileUtil;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ShrineRegistry extends AbstractRegistry<UUID, ShrineModel> {
    public static final String FILE_NAME = "shrines.dgc";

    public ShrineModel getShrine(final Location location, final int range) {
        return Iterables.find(getRegistered(), new Predicate<ShrineModel>() {
            @Override
            public boolean apply(ShrineModel shrineModel) {
                return shrineModel.getLocation().distance(location) <= range;
            }
        }, null);
    }

    public Set<ShrineModel> getFileData() {
        // Grab the current file.
        FileConfiguration data = YamlFileUtil.getConfiguration(DGClassic.SAVE_PATH, FILE_NAME);

        // Convert the raw file data into more usable data, in map form.
        Set<ShrineModel> set = new HashSet<>();
        for (String stringId : data.getKeys(false)) {
            try {
                set.add(new ShrineModel(UUID.fromString(stringId), data.getConfigurationSection(stringId)));
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
        final Set<ShrineModel> currentFileData = getFileData();

        // Create/overwrite a configuration section if new data exists.
        for (ShrineModel model : Collections2.filter(getRegistered(), new Predicate<ShrineModel>() {
            @Override
            public boolean apply(ShrineModel key) {
                return !currentFileData.contains(key);
            }
        }))
            currentFile.createSection(model.getAlliance().name(), model.serialize());

        // Remove old unneeded data.
        for (ShrineModel model : Collections2.filter(currentFileData, new Predicate<ShrineModel>() {
            @Override
            public boolean apply(ShrineModel key) {
                return !getRegistered().contains(key);
            }
        }))
            currentFile.set(model.getAlliance().name(), null);

        // Save the file!
        return YamlFileUtil.saveFile(DGClassic.SAVE_PATH, FILE_NAME, currentFile);
    }
}
