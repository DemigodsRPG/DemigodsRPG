package com.demigodsrpg.game.model;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.Setting;
import com.demigodsrpg.game.ability.AbilityMetaData;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.battle.BattleMetaData;
import com.demigodsrpg.game.battle.Participant;
import com.demigodsrpg.game.deity.Deity;
import com.demigodsrpg.game.deity.Faction;
import com.demigodsrpg.game.util.JsonSection;
import com.demigodsrpg.game.util.ZoneUtil;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.TIntDoubleHashMap;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PlayerModel extends AbstractPersistentModel<String> implements Participant {
    private final String mojangId;
    private String lastKnownName;

    // -- PARENTS -- //
    private Deity god;
    private Deity hero;

    private final List<String> aspects = new ArrayList<>(1);
    private Faction faction;
    private final BiMap<String, String> binds = HashBiMap.create();
    private final TIntDoubleHashMap experience;

    private long lastLoginTime;

    private double maxHealth;
    private double favor;
    private int level;

    private boolean canPvp;

    private int kills;
    private int deaths;
    private int teamKills;

    @SuppressWarnings("deprecation")
    public PlayerModel(Player player) {
        mojangId = player.getUniqueId().toString();
        lastKnownName = player.getName();
        lastLoginTime = System.currentTimeMillis();

        experience = new TIntDoubleHashMap(1);

        god = Deity.LOREM;
        hero = Deity.IPSUM;
        faction = Faction.NEUTRAL;

        maxHealth = 20.0;

        favor = 700.0;
        level = 0;

        canPvp = true;

        kills = 0;
        deaths = 0;
        teamKills = 0;
    }

    public PlayerModel(String mojangId, JsonSection conf) {
        this.mojangId = mojangId;
        lastKnownName = conf.getString("last_known_name");
        lastLoginTime = conf.getLong("last_login_time");
        aspects.addAll(conf.getStringList("aspects").stream().collect(Collectors.toList()));
        god = DGGame.DEITY_R.deityFromName(conf.getString("god"));
        hero = DGGame.DEITY_R.deityFromName(conf.getString("hero"));
        faction = DGGame.FACTION_R.factionFromName(conf.getString("faction"));
        binds.putAll((Map) conf.getSection("binds").getValues());
        maxHealth = conf.getDouble("max_health");
        favor = conf.getDouble("favor");
        experience = new TIntDoubleHashMap(1);
        for (Map.Entry<String, Object> entry : conf.getSection("experience").getValues().entrySet()) {
            try {
                experience.put(Aspects.valueOf(entry.getKey()).getId(), Double.valueOf(entry.getValue().toString()));
            } catch (Exception ignored) {
            }
        }
        level = conf.getInt("level");
        canPvp = conf.getBoolean("can_pvp", true);
        kills = conf.getInt("kills");
        deaths = conf.getInt("deaths");
        teamKills = conf.getInt("team_kills");
    }

    @Override
    public Type getType() {
        return Type.PERSISTENT;
    }

    @Override
    public String getPersistentId() {
        return mojangId;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("last_known_name", lastKnownName);
        map.put("last_login_time", lastLoginTime);
        map.put("aspects", Lists.newArrayList(aspects));
        map.put("god", god.getName());
        map.put("hero", hero.getName());
        map.put("faction", faction.getName());
        map.put("binds", binds);
        map.put("max_health", maxHealth);
        map.put("favor", favor);
        Map<Integer, Double> devotionMap = new HashMap<>();
        TIntIterator iterator = experience.keySet().iterator();
        while (iterator.hasNext()) {
            int key = iterator.next();
            try {
                devotionMap.put(key, experience.get(key));
            } catch (Exception ignored) {
            }
        }
        map.put("devotion", devotionMap);
        map.put("level", level);
        map.put("can_pvp", canPvp);
        map.put("kills", kills);
        map.put("deaths", deaths);
        map.put("team_kills", teamKills);
        return map;
    }

    public String getMojangId() {
        return mojangId;
    }

    public String getLastKnownName() {
        return lastKnownName;
    }

    public void setLastKnownName(String lastKnownName) {
        this.lastKnownName = lastKnownName;
        DGGame.PLAYER_R.register(this);
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        DGGame.PLAYER_R.register(this);
    }

    public List<String> getAspects() {
        return aspects;
    }

    public void addAspect(Aspect aspect) {
        aspects.add(getAspectName(aspect));
        DGGame.PLAYER_R.register(this);
    }

    public void removeAspect(Aspect aspect) {
        aspects.remove(getAspectName(aspect));
        DGGame.PLAYER_R.register(this);
    }

    @Override
    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
        DGGame.PLAYER_R.register(this);
    }

    public Deity getGod() {
        return god;
    }

    public Deity getHero() {
        return hero;
    }

    public boolean hasDeity(Deity deity) {
        return deity.equals(god) || deity.equals(hero);
    }

    double getMaxHealth() {
        return maxHealth;
    }

    void setMaxHealth(Double maxHealth) {
        this.maxHealth = maxHealth;
        DGGame.PLAYER_R.register(this);
    }

    public double getFavor() {
        return favor;
    }

    public void setFavor(Double favor) {
        this.favor = favor;
        DGGame.PLAYER_R.register(this);
    }

    public double getExperience(Aspect aspect) {
        if (!experience.containsKey(aspect.getId())) {
            return 0.0;
        }
        return experience.get(aspect.getId());
    }

    double getExperience(String aspectName) {
        int ordinal = Aspects.valueOf(aspectName).getId();
        if (!experience.containsKey(ordinal)) {
            return 0.0;
        }
        return experience.get(ordinal);
    }

    public Double getTotalExperience() {
        double total = 0.0;
        for (String aspect : aspects) {
            total += getExperience(aspect);
        }
        return total;
    }

    public void setExperience(Aspect aspect, double experience) {
        this.experience.put(aspect.getId(), experience);
        calculateAscensions();
        DGGame.PLAYER_R.register(this);
    }

    void setExperience(String aspectName, double experience) {
        int ordinal = Aspects.valueOf(aspectName).getId();
        this.experience.put(ordinal, experience);
        calculateAscensions();
        DGGame.PLAYER_R.register(this);
    }

    public int getLevel() {
        return level;
    }

    void setLevel(int level) {
        this.level = level;
        DGGame.PLAYER_R.register(this);
    }

    public Map<String, String> getBindsMap() {
        return binds;
    }

    public AbilityMetaData getBound(Material material) {
        if (binds.inverse().containsKey(material.name())) {
            return DGGame.ABILITY_R.fromCommand(binds.inverse().get(material.name()));
        }
        return null;
    }

    public Material getBound(AbilityMetaData ability) {
        return getBound(ability.getCommand());
    }

    Material getBound(String abilityCommand) {
        if (binds.containsKey(abilityCommand)) {
            return Material.valueOf(binds.get(abilityCommand));
        }
        return null;
    }

    public void bind(AbilityMetaData ability, Material material) {
        binds.put(ability.getCommand(), material.name());
        DGGame.PLAYER_R.register(this);
    }

    public void bind(String abilityCommand, Material material) {
        binds.put(abilityCommand, material.name());
        DGGame.PLAYER_R.register(this);
    }

    public void unbind(AbilityMetaData ability) {
        binds.remove(ability.getCommand());
        DGGame.PLAYER_R.register(this);
    }

    public void unbind(String abilityCommand) {
        binds.remove(abilityCommand);
        DGGame.PLAYER_R.register(this);
    }

    public void unbind(Material material) {
        binds.inverse().remove(material.name());
        DGGame.PLAYER_R.register(this);
    }

    public boolean getCanPvp() {
        return canPvp;
    }

    void setCanPvp(Boolean canPvp) {
        this.canPvp = canPvp;
        DGGame.PLAYER_R.register(this);
    }

    public int getKills() {
        return kills;
    }

    public void addKill() {
        kills++;
        DGGame.PLAYER_R.register(this);
    }

    public int getDeaths() {
        return deaths;
    }

    public void addDeath() {
        deaths++;
        DGGame.PLAYER_R.register(this);
    }

    public int getTeamKills() {
        return teamKills;
    }

    public void addTeamKill() {
        teamKills++;
        DGGame.PLAYER_R.register(this);
    }

    void resetTeamKills() {
        teamKills = 0;
        DGGame.PLAYER_R.register(this);
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(UUID.fromString(mojangId));
    }

    public boolean getOnline() {
        return getOfflinePlayer().isOnline();
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.PLAYER;
    }

    @Override
    public Location getLocation() {
        if (getOnline()) {
            return getOfflinePlayer().getPlayer().getLocation();
        }
        throw new UnsupportedOperationException("We don't support finding locations for players who aren't online.");
    }

    public boolean isDemigod() {
        return hero != null && god != null;
    }

    public boolean hasAspect(Aspect aspect) {
        return getAspects().contains(getAspectName(aspect));
    }

    @SuppressWarnings("RedundantCast")
    @Override
    public boolean reward(BattleMetaData data) {
        double experience = getTotalExperience();
        teamKills += data.getTeamKills();

        if (checkTeamKills()) {
            double score = data.getHits() + data.getAssists() / 2;
            score += data.getDenies();
            score += data.getKills() * 2;
            score -= data.getDeaths() * 1.5;
            score *= (double) Setting.EXP_MULTIPLIER.get();
            score /= aspects.size() + 1;
            for (String aspect : aspects) {
                setExperience(aspect, getExperience(aspect) + score);
            }
        }

        DGGame.PLAYER_R.register(this);

        return experience > getTotalExperience();
    }

    boolean checkTeamKills() {
        int maxTeamKills = Setting.MAX_TEAM_KILLS.get();
        if (maxTeamKills <= teamKills) {
            // Reset them to excommunicated
            setFaction(Faction.EXCOMMUNICATED);
            resetTeamKills();
            double former = getTotalExperience();
            if (getOnline()) {
                Player player = getOfflinePlayer().getPlayer();
                player.sendMessage(ChatColor.RED + "Your former faction has just excommunicated you.");
                player.sendMessage(ChatColor.RED + "You will no longer respawn at the faction spawn.");
                player.sendMessage(ChatColor.RED + "You have lost " +
                        ChatColor.GOLD + DecimalFormat.getCurrencyInstance().format(former - getTotalExperience()) +
                        ChatColor.RED + " experience.");
                player.sendMessage(ChatColor.YELLOW + "To join a faction, "); // TODO
            }
            return false;
        }
        return true;
    }

    public void giveFirstAspect(Deity hero, Aspect aspect) {
        giveAspect(aspect);
        setFaction(hero.getFaction());
        setMaxHealth(25.0);
        setLevel(1);
        setExperience(aspect, 20.0);
        calculateAscensions();
    }

    public void giveAspect(Aspect aspect) {
        aspects.add(getAspectName(aspect));
        setExperience(aspect, 20.0);
    }

    public boolean canClaim(Aspect aspect) {
        if (faction.equals(Faction.NEUTRAL)) {
            return !hasAspect(aspect);
        }
        if (Setting.NO_ALLIANCE_ASPECT_MODE.get()) {
            return costForNextDeity() <= level && !hasAspect(aspect);
        }

        // TODO Decide how to check if they can claim an aspect.
        return costForNextDeity() <= level && !hasAspect(aspect);
    }

    void calculateAscensions() {
        Player player = getOfflinePlayer().getPlayer();
        if (getLevel() >= (int) Setting.ASCENSION_CAP.get()) return;
        while (getTotalExperience() >= (int) Math.ceil(500 * Math.pow(getLevel() + 1, 2.02)) && getLevel() < (int) Setting.ASCENSION_CAP.get()) {
            setMaxHealth(getMaxHealth() + 10.0);
            player.setMaxHealth(getMaxHealth());
            player.setHealthScale(20.0);
            player.setHealthScaled(true);
            player.setHealth(getMaxHealth());

            setLevel(getLevel() + 1);

            player.sendMessage(ChatColor.AQUA + "Congratulations! Your Ascensions increased to " + getLevel() + ".");
            player.sendMessage(ChatColor.YELLOW + "Your maximum HP has increased to " + getMaxHealth() + ".");
        }
        DGGame.PLAYER_R.register(this);
    }

    public int costForNextDeity() {
        if (Setting.NO_COST_ASPECT_MODE.get()) return 0;
        switch (aspects.size() + 1) {
            case 1:
                return 2;
            case 2:
                return 5;
            case 3:
                return 9;
            case 4:
                return 14;
            case 5:
                return 19;
            case 6:
                return 25;
            case 7:
                return 30;
            case 8:
                return 35;
            case 9:
                return 40;
            case 10:
                return 50;
            case 11:
                return 60;
            case 12:
                return 70;
            case 13:
                return 80;
        }
        return 120;
    }

    @SuppressWarnings("deprecation")
    public void updateCanPvp() {
        if (Bukkit.getPlayer(mojangId) == null) return;

        // Define variables
        final Player player = Bukkit.getPlayer(mojangId);
        final boolean inNoPvpZone = ZoneUtil.inNoPvpZone(player.getLocation());

        if (DGGame.BATTLE_R.isInBattle(this)) return;

        if (!getCanPvp() && !inNoPvpZone) {
            setCanPvp(true);
            player.sendMessage(ChatColor.GRAY + "You can now enter in a battle.");
        } else if (!inNoPvpZone) {
            setCanPvp(true);
            DGGame.SERVER_R.remove(player.getName(), "pvp_cooldown");
        } else if (getCanPvp() && !DGGame.SERVER_R.contains(player.getName(), "pvp_cooldown")) {
            int delay = 10;
            DGGame.SERVER_R.put(player.getName(), "pvp_cooldown", true, delay, TimeUnit.SECONDS);
            final PlayerModel THIS = this;
            Bukkit.getScheduler().scheduleSyncDelayedTask(DGGame.getInst(), new BukkitRunnable() {
                @Override
                public void run() {
                    if (ZoneUtil.inNoPvpZone(player.getLocation())) {
                        if (DGGame.BATTLE_R.isInBattle(THIS)) return;
                        setCanPvp(false);
                        player.sendMessage(ChatColor.GRAY + "You are now safe from other players.");
                    }
                }
            }, (delay * 20));
        }
    }

    private String getAspectName(Aspect aspect) {
        return aspect.getGroup() + " " + aspect.getTier().name();
    }
}
