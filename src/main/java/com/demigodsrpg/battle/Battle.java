package com.demigodsrpg.battle;

import com.demigodsrpg.DGData;
import com.demigodsrpg.family.Family;
import com.demigodsrpg.model.Participant;
import com.demigodsrpg.model.PlayerModel;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Battle {
    private final String id;

    private ConcurrentMap<String, BattleMetaData> involved;

    private Location startLocation;

    private long startTimeMillis;
    private long lastInteract;
    private long endTimeMillis;

    // -- CONSTRUCTORS -- //

    public Battle() {
        id = UUID.randomUUID().toString();
        involved = new ConcurrentHashMap<>();
    }

    public Battle(Participant... participants) {
        id = UUID.randomUUID().toString();
        if (participants.length < 1) {
            throw new IllegalArgumentException("A battle needs at least 1 participant to make sense.");
        }
        involved = new ConcurrentHashMap<>();
        for (Participant participant : participants) {
            involved.put(participant.getKey(), new BattleMetaData());
        }
        startLocation = participants[0].getLocation();
        startTimeMillis = System.currentTimeMillis();
        lastInteract = System.currentTimeMillis();
        DGData.BATTLE_R.register(this);
    }

    // -- GETTERS -- //

    public String getId() {
        return id;
    }

    public ConcurrentMap<String, BattleMetaData> getInvolved() {
        return involved;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public long getLastInteract() {
        return lastInteract;
    }

    public long getEndTimeMillis() {
        return endTimeMillis;
    }

    public boolean isInvolved(Participant participant) {
        return involved.keySet().contains(participant.getKey());
    }

    public boolean isInvolved(Player player) {
        PlayerModel model = DGData.PLAYER_R.fromPlayer(player);
        return isInvolved(model);
    }

    // -- MUTATORS -- //

    public void setStartLocation(Location location) {
        this.startLocation = location;
    }

    public void setStartTimeMillis(long startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
        this.lastInteract = startTimeMillis;
    }

    public void setEndTimeMillis(long endTimeMillis) {
        this.endTimeMillis = endTimeMillis;
    }

    public void hit(Participant attacking, Participant hit) {
        putIfAbsent(attacking, hit);
        if (okayToHit(attacking, hit)) {
            involved.get(attacking.getKey()).hits++;
        }
        lastInteract = System.currentTimeMillis();
        DGData.BATTLE_R.register(this);
    }

    public void deny(Participant attacking, Participant target, Participant denier) {
        putIfAbsent(attacking, target, denier);
        if (!attacking.getFamily().equals(denier.getFamily()) && okayToHit(attacking, target)) {
            involved.get(denier.getKey()).denies++;
        }
        lastInteract = System.currentTimeMillis();
        DGData.BATTLE_R.register(this);
    }

    public void assist(Participant attacking, Participant hit, Participant assistant) {
        putIfAbsent(attacking, hit, assistant);
        if (!attacking.getFamily().equals(assistant.getFamily()) && okayToHit(attacking, hit)) {
            involved.get(assistant.getKey()).assists++;
        }
        lastInteract = System.currentTimeMillis();
        DGData.BATTLE_R.register(this);
    }

    public void kill(Participant attacking, Participant killed) {
        putIfAbsent(attacking, killed);
        if (attacking.getFamily().equals(killed.getFamily())) {
            involved.get(attacking.getKey()).teamKills++;
            attacking.addTeamKill();
        } else {
            involved.get(attacking.getKey()).kills++;
        }
        lastInteract = System.currentTimeMillis();
        die(killed);
    }

    public void die(Participant dead) {
        putIfAbsent(dead);
        involved.get(dead.getKey()).deaths++;
        DGData.BATTLE_R.register(this);
    }

    public Report end() {
        endTimeMillis = System.currentTimeMillis();
        for (Map.Entry<String, BattleMetaData> entry : involved.entrySet()) {
            Participant participant = DGData.PLAYER_R.fromId(entry.getKey()); // FIXME This restricts to players
            participant.reward(entry.getValue());
        }
        DGData.BATTLE_R.unregister(this);
        return new Report(this);
    }

    // -- PRIVATE HELPER METHODS -- //

    private boolean okayToHit(Participant attacking, Participant defending) {
        return Family.NEUTRAL.equals(attacking.getFamily()) ||
                Family.NEUTRAL.equals(defending.getFamily()) ||
                Family.EXCOMMUNICATED.equals(attacking.getFamily()) ||
                Family.EXCOMMUNICATED.equals(defending.getFamily()) ||
                !attacking.getFamily().equals(defending.getFamily());
    }

    private void putIfAbsent(Participant... toPut) {
        for (Participant participant : toPut) {
            involved.putIfAbsent(participant.getKey(), new BattleMetaData());
        }
    }
}
