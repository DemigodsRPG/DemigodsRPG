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

package com.demigodsrpg.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iciql.Db;
import com.iciql.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;

/**
 * Json file related utility methods.
 */
public class DataSectionUtil {
    // -- CONSTRUCTOR -- //

    /**
     * Private constructor to prevent instance calls.
     */
    private DataSectionUtil() {
    }

    // -- UTILITY METHODS -- //

    /**
     * Load a JsonSection from a json file.
     *
     * @param file The json file path.
     * @return The JsonSection for the entire file.
     */
    @SuppressWarnings("unchecked")
    public static Optional<DataSection> loadSectionFromFile(String file) {
        File dataFile = new File(file);
        if (!(dataFile.exists())) createFile(dataFile);
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        try {
            FileInputStream inputStream = new FileInputStream(dataFile);
            InputStreamReader reader = new InputStreamReader(inputStream);
            Map<String, Object> sectionData = gson.fromJson(reader, Map.class);
            reader.close();
            return Optional.of(new FJsonSection(sectionData));
        } catch (Exception oops) {
            oops.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Load a JsonSection from a json file.
     *
     * @param connection The database connection.
     * @return The JsonSection.
     */
    @SuppressWarnings("unchecked")
    public static Optional<DataSection> loadSectionFromPSQL(String name, String connection) {
        final ThreadLocal<PJsonSection.Table> safeTable = Utils.newThreadLocal(PJsonSection.Table.class);
        Db db = Db.open(connection);
        try {
            Optional<PJsonSection.Table> found = db.from(safeTable.get()).where(safeTable.get().id).is(name).select().stream().findFirst();
            if (found.isPresent()) {
                return Optional.ofNullable(found.get().section);
            }
        } catch (Exception oops) {
            oops.printStackTrace();
        }
        db.close();
        return Optional.empty();
    }

    /**
     * Create a file and its directory path.
     *
     * @param dataFile The file object.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void createFile(File dataFile) {
        try {
            // Create the directories.
            (dataFile.getParentFile()).mkdirs();

            // Create the new file.
            dataFile.createNewFile();
        } catch (Exception errored) {
            throw new RuntimeException("Demigods RPG couldn't create a data file!", errored);
        }
    }

    /**
     * Save a JsonSection as a json file.
     *
     * @param file     The path to the json file.
     * @param section  The JsonSection to be saved.
     * @return Save success or failure.
     */
    public static boolean saveFile(String file, DataSection section) {
        File dataFile = new File(file);
        if (!(dataFile.exists())) createFile(dataFile);
        return section.toFJsonSection().save(dataFile);
    }

    /**
     * Save a JsonSection as a json file in a pretty format.
     *
     * @param file     The path to the json file.
     * @param section  The JsonSection to be saved.
     * @return Save success or failure.
     */
    public static boolean saveFilePretty(String file, DataSection section) {
        File dataFile = new File(file);
        if (!(dataFile.exists())) createFile(dataFile);
        return section.toFJsonSection().savePretty(dataFile);
    }

    /**
     * Save this section to a json db.
     *
     * @param name       The section name.
     * @param connection The db to hold the section data.
     * @return Save success or failure.
     */
    public static boolean savePSQL(String name, String connection, DataSection section) {
        return section.toPJsonSection().save(name, connection);
    }
}
