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

public enum CommonSymbol {
    // Misc
    BLACK_SQUARE("⬛"),
    FULL_BLOCK("█"),
    DASH("―"),
    CAUTION("⚠"),
    MAIL("✉"),
    DEATH("☠"),
    WATCH("⌚"),
    HOURGLASS("⌛"),
    POINT_OF_INTEREST("⌘"),

    // Misc cont.
    SUN("☀"),
    CHECKMARK_SLIM("✓"),
    CHECKMARK_THICK("✔"),
    X_SLIM("✗"),
    X_THICK("✘"),
    CHECKED_BOX("☑"),
    X_BOX("☒"),
    COFFEE("☕"),
    NO_ENTRY("⛔"),
    BLACK_FLAG("⚑"),
    BULLET("•"),

    // Math
    ONE_THIRD("⅓"),
    TWO_THIRDS("⅔"),
    ONE_FIFTH("⅕"),
    TWO_FIFTHS("⅖"),
    THREE_FIFTHS("⅗"),
    FOUR_FIFTHS("⅘"),

    // Roman Numerals
    RN_ONE("Ⅰ"),
    RN_FIVE("Ⅴ"),
    RN_TEN("Ⅹ"),
    RN_FIFTY("Ⅼ"),
    RN_HUNDRED("Ⅽ"),
    RN_FIVE_HUNDRED("Ⅾ"),
    RN_THOUSAND("Ⅿ"),

    // Greek
    L_ALPHA("α"),
    L_BETA("β"),
    U_GAMMA("Γ"),
    L_GAMMA("γ"),
    U_DELTA("Δ"),
    L_DELTA("δ"),
    L_ELIPSON("ε"),
    THETA("Θ"),
    U_PHI("Φ"),
    L_PHI("φ"),
    U_OMEGA("Ω"),
    L_OMEGA("ω"),

    // COPYRIGHT
    COPYRIGHT("©"),
    TRADEMARK("™"),
    SERVICEMARK("℠"),

    // Arrows
    RIGHTWARD_ARROW_SWOOP("➥"),
    RIGHTWARD_ARROW_THICK("➔"),
    RIGHTWARD_ARROW_INVERTED("➲"),
    RIGHTWARD_ARROW_HOLLOW("⇨"),
    RIGHTWARD_ARROW("➡"),
    COUNTERCLOCKWISE_SEMICIRLCE_ARROW("↶"),
    CLOCKWISE_SEMICIRCLE_ARROW("↷"),
    COUNTERCLOCKWISE_OPEN_CIRCLE_ARROW("↺"),
    CLOCKWISE_OPEN_CIRCLE_ARROW("↻"),

    // Numbers
    NUMBER_ONE_CIRCLE("❶"),
    NUMBER_TWO_CIRCLE("❷"),
    NUMBER_THREE_CIRCLE("❸"),
    NUMBER_FOUR_CIRCLE("❹"),
    NUMBER_FIVE_CIRCLE("❺"),
    NUMBER_SIX_CIRCLE("❻"),
    NUMBER_SEVEN_CIRCLE("❼"),
    NUMBER_EIGHT_CIRCLE("❽"),
    NUMBER_NINE_CIRCLE("❾"),
    NUMBER_TEN_CIRCLE("❿");

    private String symbol;

    private CommonSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
