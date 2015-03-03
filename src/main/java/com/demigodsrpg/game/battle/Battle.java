package com.demigodsrpg.game.battle;

import com.demigodsrpg.game.deity.Faction;
import org.spongepowered.api.world.Location;

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
