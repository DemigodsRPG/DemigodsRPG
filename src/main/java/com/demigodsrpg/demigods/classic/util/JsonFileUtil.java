package com.demigodsrpg.demigods.classic.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class JsonFileUtil {
    private JsonFileUtil() {
    }

    public static JsonSection getSection(String path, String fileName) {
        File dataFile = new File(path + fileName);
        if (!(dataFile.exists())) createFile(dataFile);
        return loadSection(dataFile);
    }

    @SuppressWarnings("unchecked")
    public static JsonSection loadSection(File dataFile) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        try {
            FileInputStream inputStream = new FileInputStream(dataFile);
            InputStreamReader reader = new InputStreamReader(inputStream);
            Map<String, Object> sectionData = gson.fromJson(reader, Map.class);
            reader.close();
            return new JsonSection(sectionData);
        } catch (Exception oops) {
            oops.printStackTrace();
        }
        return null;
    }

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

    public static boolean saveFile(String path, String fileName, JsonSection section) {
        File dataFile = new File(path + fileName);
        if (!(dataFile.exists())) createFile(dataFile);
        return section.save(dataFile);
    }
}
