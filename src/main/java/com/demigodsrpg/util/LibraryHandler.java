package com.demigodsrpg.util;

import org.bukkit.plugin.Plugin;

import java.io.*;
import java.net.*;
import java.util.*;

public class LibraryHandler {
    // -- IMPORTANT FIELDS -- //

    public static final String MAVEN_CENTRAL = "http://central.maven.org/maven2/";
    private static final int BYTE_SIZE = 1024;

    private final List<String> FILE_NAMES;
    private final Plugin PLUGIN;
    private final File LIB_DIRECTORY;

    // -- CONSTRUCTOR -- //

    public LibraryHandler(Plugin plugin) {
        this.PLUGIN = plugin;
        FILE_NAMES = new ArrayList<>();
        LIB_DIRECTORY = new File(PLUGIN.getDataFolder().getPath() + "/lib");
        checkDirectory();
    }

    // -- HELPER METHODS -- //

    public void addMavenLibrary(String repo, String groupId, String artifactId, String version) {
        try {
            String fileName = artifactId + "-" + version + ".jar";
            loadLibrary(fileName, new URI(repo + groupId.replace(".", "/") + "/" + artifactId + "/" + version + "/" +
                    fileName).toURL());
        } catch (Exception oops) {
            oops.printStackTrace();
        }
    }

    public void checkDirectory() {
        // If it exists and isn't a directory, throw an error
        if (LIB_DIRECTORY.exists() && !LIB_DIRECTORY.isDirectory()) {
            PLUGIN.getLogger().severe("The library directory isn't a directory!");
            return;
        }
        // Otherwise, make the directory
        else if (!LIB_DIRECTORY.exists()) {
            LIB_DIRECTORY.mkdirs();
        }

        // Check if all libraries exist

        File[] filesArray = LIB_DIRECTORY.listFiles();
        List<File> files = Arrays.asList(filesArray != null ? filesArray : new File[]{});

        for (File file : files) {
            if (file.getName().endsWith(".jar")) {
                FILE_NAMES.add(file.getName());
            }
        }
    }

    public void loadLibrary(String fileName, URL url) {
        // Check if the files are found or not
        File libraryFile = null;
        if (FILE_NAMES.contains(fileName)) {
            libraryFile = new File(LIB_DIRECTORY + "/" + fileName);
        }

        // If they aren't found, download them
        if (libraryFile == null) {
            PLUGIN.getLogger().warning("Downloading " + fileName + ".");
            libraryFile = downloadLibrary(fileName, url);
        }

        // Add the library to the classpath
        addToClasspath(libraryFile);
    }

    public void addToClasspath(File file) {
        try {
            ClassPathHack.addFile(file, (URLClassLoader) PLUGIN.getClass().getClassLoader());
        } catch (Exception oops) {
            PLUGIN.getLogger().severe("Couldn't load " + (file != null ? file.getName() : "a required library") + ", " +
                    "this may cause problems.");
            oops.printStackTrace();
        }
    }

    public File downloadLibrary(String libraryFileName, URL libraryUrl) {
        // Get the file
        File libraryFile = new File(LIB_DIRECTORY.getPath() + "/" + libraryFileName);

        // Create the streams
        BufferedInputStream in = null;
        FileOutputStream fout = null;

        try {
            // Setup the streams
            in = new BufferedInputStream(libraryUrl.openStream());
            fout = new FileOutputStream(libraryFile);

            // Create variables for loop
            final byte[] data = new byte[BYTE_SIZE];
            int count;

            // Write the data to the file
            while ((count = in.read(data, 0, BYTE_SIZE)) != -1) {
                fout.write(data, 0, count);
            }

            PLUGIN.getLogger().info("Download complete.");

            // Return the file
            return libraryFile;
        } catch (final Exception oops) {
            // Couldn't download the file
            PLUGIN.getLogger().severe("Download could not complete");
        } finally {
            // Close the streams
            try {
                if (in != null) {
                    in.close();
                }
                if (fout != null) {
                    fout.close();
                }
            } catch (final Exception ignored) {
            }
        }

        return null;
    }
}
