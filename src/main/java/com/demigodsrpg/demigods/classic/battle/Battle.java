package com.demigodsrpg.demigods.classic.battle;

import com.demigodsrpg.demigods.classic.deity.IDeity;
import org.bukkit.Location;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Battle {
    private ConcurrentMap<Participant, Data> involved;
    private Set<Micro> microBattles;

    private Location startLocation;

    private Long startTimeMillis;
    private Long endTimeMillis;

    private IDeity.Alliance won;
    private IDeity.Alliance lost;

    // -- CONSTRUCTORS -- //

    public Battle(Participant... participants) {
        if (participants.length < 1) {
            throw new IllegalArgumentException("A battle needs at least 1 participant to make sense.");
        }
        involved = new ConcurrentHashMap<>();
        for (Participant participant : participants) {
            involved.put(participant, new Data());
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
            involved.putIfAbsent(participant, new Data());
        }
    }

    // -- SUB-CLASSES -- //

    public static class Data {
        private int hits = 0;
        private int assists = 0;
        private int denies = 0;
        private int kills = 0;
        private int teamKills = 0;
        private int deaths = 0;

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
}
