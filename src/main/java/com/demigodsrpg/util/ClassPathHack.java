package com.demigodsrpg.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassPathHack {
    private static final Class[] parameters = new Class[]{URL.class};

    private ClassPathHack() {
    }

    public static void addFile(File f, URLClassLoader cL) throws IOException {
        addURL(f.toURI().toURL(), cL);
    }

    public static void addURL(URL u, URLClassLoader cL) throws IOException {
        Class urlClassLoader = URLClassLoader.class;
        try {
            Method method = urlClassLoader.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(cL, new Object[]{u});
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }
    }
}
