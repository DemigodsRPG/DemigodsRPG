/*
 * Copyright 2014 Alex Bennett & Alexander Chauncey
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

package com.demigodsrpg.util.misc;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.apache.commons.lang.StringUtils;
import org.atteo.evo.inflector.English;

import java.util.Collection;

public class StringUtil2 {
    /**
     * Returns true if the <code>string</code> starts with a vowel.
     *
     * @param string the string to check.
     */
    public static boolean beginsWithVowel(String string) {
        String[] vowels = {"a", "e", "i", "o", "u"};
        return StringUtils.startsWithAny(string.toLowerCase(), vowels);
    }

    /**
     * Returns true if the <code>string</code> starts with a consonant.
     *
     * @param string the string to check.
     */
    public static boolean beginsWithConsonant(String string) {
        return !beginsWithVowel(string);
    }

    /**
     * Returns true if the <code>string</code> contains any of the strings held in the <code>collection</code>.
     *
     * @param check      the string to check.
     * @param collection the collection given.
     */
    public static boolean containsAnyInCollection(final String check, Collection<String> collection) {
        return Iterables.any(collection, new Predicate<String>() {
            @Override
            public boolean apply(String string) {
                return StringUtils.containsIgnoreCase(check, string);
            }
        });
    }

    /**
     * Checks the <code>string</code> for <code>max</code> capital letters.
     *
     * @param string the string to check.
     * @param max    the maximum allowed capital letters.
     */
    public static boolean hasCapitalLetters(String string, int max) {
        // Define variables
        String allCaps = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int count = 0;
        char[] characters = string.toCharArray();
        for (char character : characters) {
            if (allCaps.contains("" + character)) count++;
            if (count > max) return true;
        }
        return false;
    }

    public static String plural(String word, int count) {
        return English.plural(word, count);
    }

    /**
     * Automatically removes underscores and returns a capitalized version of the given <code>string</code>.
     *
     * @param string the string the beautify.
     */
    public static String beautify(String string) {
        return StringUtils.capitalize(string.toLowerCase().replace("_", " "));
    }

    /**
     * Returns a formatted title ready for the chat.
     *
     * @param title the title to format
     * @return the formatted title
     */
    public static String chatTitle(String title) {
        int total = 86;
        String chatTitle = " " + CommonSymbol.RIGHTWARD_ARROW + " " + title + " ";
        for (int i = 0; i < (total - chatTitle.length()); i++) { chatTitle += "-"; }
        return chatTitle;
    }
}
