/*
 * Copyright 2014 Alexander Chauncey
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

package com.demigodsrpg.demigods.classic.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Package private utility for common file related methods.
 */
public class YamlFileUtil {
    /**
     * Private constructor.
     */
    private YamlFileUtil() {
    }

    /**
     * Get the FileConfiguration at a given location.
     * If no file exists, create one.
     *
     * @param path     The file directory path.
     * @param fileName The file name.
     * @return The configuration.
     */
    public static FileConfiguration getConfiguration(String path, String fileName) {
        File dataFile = new File(path + fileName);
        if (!(dataFile.exists())) createFile(dataFile);
        return YamlConfiguration.loadConfiguration(dataFile);
    }

    /**
     * Create a new file.
     *
     * @param dataFile The file object.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void createFile(File dataFile) {
        try {
            // Create the directories.
            (dataFile.getParentFile()).mkdirs();

            // Create the new file.
            dataFile.createNewFile();
        } catch (Exception errored) {
            throw new RuntimeException("DGClassic couldn't create a data file!", errored);
        }
    }

    /**
     * Save the file.
     *
     * @param path     The file directory path.
     * @param fileName The file name.
     * @param conf     The bukkit handled file configuration.
     * @return Saved successfully.
     */
    public static boolean saveFile(String path, String fileName, FileConfiguration conf) {
        try {
            conf.save(path + fileName);
            return true;
        } catch (Exception oops) {
            oops.printStackTrace();
        }
        return false;
    }
}
