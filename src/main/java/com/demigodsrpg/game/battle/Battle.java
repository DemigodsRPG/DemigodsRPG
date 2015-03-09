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

package com.demigodsrpg.game.battle;

import com.demigodsrpg.game.deity.Faction;
import org.bukkit.Location;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class Battle {
    private ConcurrentMap<Participant, BattleMetaData> involved;
    private List<Micro> microBattles;

    private Location startLocation;

    private long startTimeMillis;
    private long endTimeMillis;

    private Faction won;
    private Faction lost;

    // -- CONSTRUCTORS -- //

    public Battle(Participant... participants) {
        if (participants.length < 1) {
            throw new IllegalArgumentException("A battle needs at least 1 participant to make sense.");
        }
        involved = new ConcurrentHashMap<>();
        for (Participant participant : participants) {
            involved.put(participant, new BattleMetaData());
        }
        startLocation = participants[0].getLocation();
        startTimeMillis = System.currentTimeMillis();
    }

    // -- MUTATORS -- //

    public void hit(Participant attacking, Participant hit) {
        putIfAbsent(attacking, hit);
        if (!attacking.getFaction().equals(hit.getFaction())) {
            involved.get(attacking).hits++;
        }
    }

    public Report end() {
        endTimeMillis = System.currentTimeMillis();
        return new Report(this);
    }

    // -- PRIVATE HELPER METHODS -- //

    private boolean okayToHit(Participant attacking, Participant defending) {
        return Faction.NEUTRAL.equals(attacking.getFaction()) ||
                Faction.NEUTRAL.equals(defending.getFaction()) ||
                Faction.EXCOMMUNICATED.equals(attacking.getFaction()) ||
                Faction.EXCOMMUNICATED.equals(defending.getFaction()) ||
                !attacking.getFaction().equals(defending.getFaction());
    }

    private void putIfAbsent(Participant... toPut) {
        for (Participant participant : toPut) {
            involved.putIfAbsent(participant, new BattleMetaData());
        }
    }
}
