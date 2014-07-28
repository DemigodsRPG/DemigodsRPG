package com.demigodsrpg.demigods.classic.registry;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.model.AbstractPersistentModel;
import com.demigodsrpg.demigods.classic.util.YamlFileUtil;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class AbstractDirectRegistry<K, T extends AbstractPersistentModel<K>> extends AbstractRegistry<K, T> {
    @Override
    public void register(T data) {
        FileConfiguration configuration = YamlFileUtil.getConfiguration(DGClassic.SAVE_PATH, getFileName());
        configuration.set(data.getPersistantId().toString(), data.serialize());
        YamlFileUtil.saveFile(DGClassic.SAVE_PATH, getFileName(), configuration);
    }

    @Override
    public void unregister(T data) {
        FileConfiguration configuration = YamlFileUtil.getConfiguration(DGClassic.SAVE_PATH, getFileName());
        configuration.set(data.getPersistantId().toString(), null);
        YamlFileUtil.saveFile(DGClassic.SAVE_PATH, getFileName(), configuration);
    }

    @Override
    public boolean saveToFile() {
        return true;
    }
}
