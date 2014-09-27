package com.demigodsrpg.demigods.classic.battle;

public class BattleMetaData {
    // -- PACKAGE PRIVATE FIELDS -- //

    int hits = 0;
    private final int assists = 0;
    private final int denies = 0;
    private final int kills = 0;
    private final int teamKills = 0;
    private final int deaths = 0;

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
