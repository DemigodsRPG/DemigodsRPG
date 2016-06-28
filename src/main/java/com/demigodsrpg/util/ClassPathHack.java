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
