package com.demigodsrpg.demigods.classic.ability;

/**
 * The result state of an ability after being invoked.
 */
public enum AbilityResult {
    /**
     * The ability cast was a success.
     */
    SUCCESS,
    /**
     * There was no target found.
     */
    NO_TARGET_FOUND,
    /**
     * The ability failed for some other reason.
     */
    OTHER_FAILURE
}
