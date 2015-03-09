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

package com.demigodsrpg.game.ability;

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
