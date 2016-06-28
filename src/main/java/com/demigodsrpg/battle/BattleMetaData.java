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

package com.demigodsrpg.battle;

public class BattleMetaData {
    // -- PACKAGE PRIVATE FIELDS -- //

    int hits = 0;
    int assists = 0;
    int denies = 0;
    int kills = 0;
    int teamKills = 0;
    int deaths = 0;

    // -- GETTERS -- //

    public int getHits() {
        return hits;
    }

    public int getAssists() {
        return assists;
    }

    public int getDenies() {
        return denies;
    }

    public int getKills() {
        return kills;
    }

    public int getTeamKills() {
        return teamKills;
    }

    public int getDeaths() {
        return deaths;
    }
}
