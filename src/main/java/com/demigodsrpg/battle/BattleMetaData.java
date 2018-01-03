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
