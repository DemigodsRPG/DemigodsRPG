package com.demigodsrpg.game.battle;

import com.demigodsrpg.game.deity.IDeity;
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

    private IDeity.Alliance won;
    private IDeity.Alliance lost;

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
        if (!attacking.getAlliance().equals(hit.getAlliance())) {
            involved.get(attacking).hits++;
        }
    }

    public Report end() {
        endTimeMillis = System.currentTimeMillis();
        return new Report(this);
    }

    // -- PRIVATE HELPER METHODS -- //

    private boolean okayToHit(Participant attacking, Participant defending) {
        return IDeity.Alliance.NEUTRAL.equals(attacking.getAlliance()) ||
                IDeity.Alliance.NEUTRAL.equals(defending.getAlliance()) ||
                IDeity.Alliance.EXCOMMUNICATED.equals(attacking.getAlliance()) ||
                IDeity.Alliance.EXCOMMUNICATED.equals(defending.getAlliance()) ||
                !attacking.getAlliance().equals(defending.getAlliance());
    }

    private void putIfAbsent(Participant... toPut) {
        for (Participant participant : toPut) {
            involved.putIfAbsent(participant, new BattleMetaData());
        }
    }
}
