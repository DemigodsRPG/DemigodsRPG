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

package com.demigodsrpg.model;

import com.demigodsrpg.DGData;
import com.demigodsrpg.Demo;
import com.demigodsrpg.Setting;
import com.demigodsrpg.ability.AbilityCaster;
import com.demigodsrpg.ability.AbilityMetaData;
import com.demigodsrpg.ability.AbilityRegistry;
import com.demigodsrpg.aspect.Aspect;
import com.demigodsrpg.aspect.Aspects;
import com.demigodsrpg.aspect.Groups;
import com.demigodsrpg.battle.BattleMetaData;
import com.demigodsrpg.deity.Deity;
import com.demigodsrpg.deity.DeityType;
import com.demigodsrpg.family.Family;
import com.demigodsrpg.util.ZoneUtil;
import com.demigodsrpg.util.datasection.AbstractPersistentModel;
import com.demigodsrpg.util.datasection.DataSection;
import com.demigodsrpg.util.misc.RandomUtil;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.TIntDoubleHashMap;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PlayerModel extends AbstractPersistentModel<String> implements AbilityCaster, Participant {
    private final String mojangId;
    private String lastKnownName;

    // -- PARENTS -- //
    private Optional<Deity> god;
    private Optional<Deity> hero;

    // -- CONTRACTS -- //
    private List<Deity> contracts = new ArrayList<>();

    private final List<String> aspects = new ArrayList<>(1);
    private final List<String> shrineWarps = new ArrayList<>();
    private Family family;
    private final BiMap<String, String> binds = HashBiMap.create();
    private final TIntDoubleHashMap experience;

    private long lastLoginTime;

    private double maxHealth;
    private double favor;
    private int level;

    private boolean canPvp;
    private boolean adminMode;

    private int kills;
    private int deaths;
    private int teamKills;

    @SuppressWarnings("deprecation")
    public PlayerModel(Player player) {
        mojangId = player.getUniqueId().toString();
        lastKnownName = player.getName();
        lastLoginTime = System.currentTimeMillis();

        experience = new TIntDoubleHashMap(1);

        // Debug data
        if (Setting.DEBUG_DATA) {
            handleDemo();
        } else {
            // Neutral faction
            family = Family.NEUTRAL;

            // Empty deities
            god = Optional.empty();
            hero = Optional.empty();
        }

        maxHealth = 20.0;

        favor = 700.0;
        level = 0;

        canPvp = true;
        adminMode = false;

        kills = 0;
        deaths = 0;
        teamKills = 0;
    }

    @SuppressWarnings("unchecked")
    public PlayerModel(String mojangId, DataSection conf) {
        this.mojangId = mojangId;
        lastKnownName = conf.getString("last_known_name");
        lastLoginTime = conf.getLong("last_login_time");
        aspects.addAll(conf.getStringList("aspects"));
        if (conf.getStringList("shrine_warps") != null) {
            shrineWarps.addAll(conf.getStringList("shrine_warps"));
        }
        god = Optional.ofNullable(DGData.DEITY_R.deityFromName(conf.getStringNullable("god")));
        hero = Optional.ofNullable(DGData.DEITY_R.deityFromName(conf.getStringNullable("hero")));
        for (Object contractName : conf.getList("contracts", new ArrayList<>())) {
            Deity contract = DGData.DEITY_R.deityFromName(contractName.toString());
            if (contract != null) {
                contracts.add(contract);
            }
        }
        family = DGData.FAMILY_R.familyFromName(conf.getStringNullable("faction"));
        if (family == null) {
            family = Family.NEUTRAL;
        }
        Map binds = conf.getSectionNullable("binds") != null ? (Map) conf.getSectionNullable("binds").getValues() : null;
        if (binds != null) {
            this.binds.putAll(binds);
        }
        maxHealth = conf.getDouble("max_health", 20.0);
        favor = conf.getDouble("favor", 20.0);
        experience = new TIntDoubleHashMap(1);
        boolean expError = false;
        for (Map.Entry<String, Object> entry : conf.getSectionNullable("devotion").getValues().entrySet()) {
            try {
                experience.put(Integer.valueOf(entry.getKey()), Double.valueOf(entry.getValue().toString()));
            } catch (Exception ignored) {
                expError = true;
            }
        }
        if (expError) {
            DGData.CONSOLE.warning("There was an error loading devotion data for " + lastKnownName + ".");
        }
        level = conf.getInt("level");
        canPvp = conf.getBoolean("can_pvp", true);
        adminMode = conf.getBoolean("admin_mode", false);
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
        map.put("shrine_warps", Lists.newArrayList(shrineWarps));
        if (god.isPresent()) {
            map.put("god", god.get().getName());
        }
        if (hero.isPresent()) {
            map.put("hero", hero.get().getName());
        }
        map.put("contracts", contracts.stream().map(Deity::getName).collect(Collectors.toList()));
        map.put("faction", family.getName());
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
        map.put("admin_mode", adminMode);
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
        DGData.PLAYER_R.register(this);
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        DGData.PLAYER_R.register(this);
    }

    public List<String> getAspects() {
        return aspects;
    }

    public void addAspect(Aspect aspect) {
        aspects.add(aspect.name());
        DGData.PLAYER_R.register(this);
    }

    public void removeAspect(Aspect aspect) {
        aspects.remove(aspect.name());
        DGData.PLAYER_R.register(this);
    }

    public List<String> getShrineWarps() {
        return shrineWarps;
    }

    public void addShrineWarp(ShrineModel model) {
        shrineWarps.add(model.getPersistentId());
        DGData.PLAYER_R.register(this);
    }

    public void removeShrineWarp(ShrineModel model) {
        shrineWarps.remove(model.getPersistentId());
        DGData.PLAYER_R.register(this);
    }

    public void removeShrineWarp(String id) {
        shrineWarps.remove(id);
        DGData.PLAYER_R.register(this);
    }

    @Override
    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
        DGData.PLAYER_R.register(this);
    }

    public void setGod(Deity god) {
        if (DeityType.GOD.equals(god.getDeityType())) {
            this.god = Optional.ofNullable(god);
            DGData.PLAYER_R.register(this);
        } else {
            throw new IllegalArgumentException("Cannot set a non-god deity as a god.");
        }
    }

    public void setHero(Deity hero) {
        if (DeityType.HERO.equals(hero.getDeityType())) {
            this.hero = Optional.ofNullable(hero);
            DGData.PLAYER_R.register(this);
        } else {
            throw new IllegalArgumentException("Cannot set a non-hero deity as a hero.");
        }
    }

    public Optional<Deity> getGod() {
        return god;
    }

    public Optional<Deity> getHero() {
        return hero;
    }

    public List<Deity> getContracts() {
        return contracts;
    }

    public void addContract(Deity deity) {
        contracts.add(deity);
    }

    public void removeContract(Deity deity) {
        contracts.remove(deity);
    }

    public boolean hasDeity(Deity deity) {
        return god.isPresent() && god.get().equals(deity) || hero.isPresent() && hero.get().equals(deity) || contracts.contains(deity);
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(Double maxHealth) {
        this.maxHealth = maxHealth;
        DGData.PLAYER_R.register(this);
    }

    public double getFavor() {
        return favor;
    }

    public void setFavor(double favor) {
        this.favor = favor;
        DGData.PLAYER_R.register(this);
    }

    public double getExperience(Aspect aspect) {
        if (!experience.containsKey(aspect.getId())) {
            return 0.0;
        }
        return experience.get(aspect.getId());
    }

    public double getExperience(String aspectName) {
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

    public void setExperience(Aspect aspect, double experience, boolean announce) {
        this.experience.put(aspect.getId(), experience);
        calculateAscensions(announce);
        DGData.PLAYER_R.register(this);
    }

    public void setExperience(String aspectName, double experience, boolean announce) {
        int ordinal = Aspects.valueOf(aspectName).getId();
        this.experience.put(ordinal, experience);
        calculateAscensions(announce);
        DGData.PLAYER_R.register(this);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        DGData.PLAYER_R.register(this);
    }

    public Map<String, String> getBindsMap() {
        return binds;
    }

    public AbilityMetaData getBound(Material material) {
        if (binds.inverse().containsKey(material.name())) {
            return AbilityRegistry.fromCommand(binds.inverse().get(material.name()));
        }
        return null;
    }

    public Material getBound(AbilityMetaData ability) {
        return getBound(ability.getCommand());
    }

    public Material getBound(String abilityCommand) {
        if (binds.containsKey(abilityCommand)) {
            return Material.valueOf(binds.get(abilityCommand));
        }
        return null;
    }

    public void bind(AbilityMetaData ability, Material material) {
        binds.put(ability.getCommand(), material.name());
        DGData.PLAYER_R.register(this);
    }

    public void bind(String abilityCommand, Material material) {
        binds.put(abilityCommand, material.name());
        DGData.PLAYER_R.register(this);
    }

    public void unbind(AbilityMetaData ability) {
        binds.remove(ability.getCommand());
        DGData.PLAYER_R.register(this);
    }

    public void unbind(String abilityCommand) {
        binds.remove(abilityCommand);
        DGData.PLAYER_R.register(this);
    }

    public void unbind(Material material) {
        binds.inverse().remove(material.name());
        DGData.PLAYER_R.register(this);
    }

    public boolean getCanPvp() {
        return canPvp;
    }

    public void setCanPvp(Boolean canPvp) {
        this.canPvp = canPvp;
        DGData.PLAYER_R.register(this);
    }

    public boolean getAdminMode() {
        return adminMode;
    }

    public void setAdminMode(Boolean adminMode) {
        this.adminMode = adminMode;
        DGData.PLAYER_R.register(this);
    }

    public int getKills() {
        return kills;
    }

    public void addKill() {
        kills++;
        DGData.PLAYER_R.register(this);
    }

    public int getDeaths() {
        return deaths;
    }

    public void addDeath() {
        deaths++;
        DGData.PLAYER_R.register(this);
    }

    public int getTeamKills() {
        return teamKills;
    }

    public void addTeamKill() {
        teamKills++;
        DGData.PLAYER_R.register(this);
    }

    public void resetTeamKills() {
        teamKills = 0;
        DGData.PLAYER_R.register(this);
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
        return getAspects().contains(aspect.name());
    }

    public boolean hasPrereqs(Aspect aspect) {
        int tier = aspect.getTier().ordinal();
        if (tier == 0) {
            return true;
        }
        Aspect.Group group = aspect.getGroup();
        for (String hasName : getAspects()) {
            Aspect has = Aspects.valueOf(hasName);
            if (has.getGroup().equals(group) && has.getTier().ordinal() + 1 == tier) {
                return true;
            }
        }
        return false;
    }

    public List<Aspect.Group> getPotentialGroups() {
        // Get the groups
        List<Aspect.Group> groups = new ArrayList<>();

        // Get the deities
        List<Deity> deities = new ArrayList<>(contracts);

        // Add hero and god tot he deity list
        if (hero.isPresent()) {
            deities.add(hero.get());
        }
        if (god.isPresent()) {
            deities.add(god.get());
        }

        // For each deity, find the groups
        for (Deity deity : deities) {
            // Is the group already in the cache?
            deity.getAspectGroups().stream().filter(group -> !groups.contains(group)).forEach(groups::add);
        }
        return groups;
    }

    public List<Aspect> getPotentialAspects(Aspect.Group group, boolean alwaysIncludeHero) {
        List<Aspect> aspects = new ArrayList<>();
        Optional<Aspect> heroAspect = Groups.heroAspectInGroup(group);
        if (hero.isPresent() && hero.get().getAspectGroups().contains(group) && heroAspect.isPresent()) {
            aspects.add(heroAspect.get());
        }
        List<Deity> gods = new ArrayList<>(contracts);
        if (god.isPresent()) {
            gods.add(god.get());
        }
        for (Deity deity : gods) {
            if (deity.getAspectGroups().contains(group)) {
                if (alwaysIncludeHero) {
                    List<Aspect> allAspects = Groups.aspectsInGroup(group);
                    if (heroAspect.isPresent()) {
                        allAspects.remove(heroAspect.get());
                    }
                    aspects.addAll(allAspects);
                } else {
                    aspects.addAll(Groups.godAspectsInGroup(group));
                }
                break;
            }
        }
        return aspects;
    }

    public List<Aspect> getPotentialAspects(boolean alwaysIncludeHero) {
        List<Aspect> aspects = new ArrayList<>();
        for (Aspect.Group group : getPotentialGroups()) {
            aspects.addAll(getPotentialAspects(group, alwaysIncludeHero));
        }
        return aspects;
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
            score *= (double) Setting.EXP_MULTIPLIER;
            score /= aspects.size() + 1;
            for (String aspect : aspects) {
                setExperience(aspect, getExperience(aspect) + score, true);
            }
        }

        DGData.PLAYER_R.register(this);

        return experience > getTotalExperience();
    }

    public boolean checkTeamKills() {
        if (!Family.EXCOMMUNICATED.equals(family)) {
            if (Setting.MAX_TEAM_KILLS <= teamKills) {
                // Reset them to excommunicated
                setFamily(Family.EXCOMMUNICATED);
                resetTeamKills();
                // double former = getTotalExperience();
                if (getOnline()) {
                    Player player = getOfflinePlayer().getPlayer();
                    player.sendMessage(ChatColor.RED + "Your former faction has just excommunicated you.");
                    player.sendMessage(ChatColor.RED + "You will no longer respawn at the faction spawn.");
                    // player.sendMessage(ChatColor.RED + "You have lost " +
                    //         ChatColor.GOLD + DecimalFormat.getCurrencyInstance().format(former - getTotalExperience()) +
                    //         ChatColor.RED + " experience.");
                    // player.sendMessage(ChatColor.YELLOW + "To join a faction, "); // TODO
                }
                return false;
            }
        }
        return true;
    }

    public void giveHeroAspect(Deity hero, Aspect aspect) {
        giveAspect(aspect);
        setFamily(hero.getFamilies().get(0));
        setMaxHealth(25.0);
        setLevel(1);
        setExperience(aspect, 20.0, true);
        calculateAscensions(true);
    }

    public void giveAspect(Aspect aspect) {
        aspects.add(aspect.name());
        setExperience(aspect, 20.0, true);
    }

    public boolean canClaim(Aspect aspect) {
        return costForNextAspect() <= level && !hasAspect(aspect) && hasPrereqs(aspect);
    }

    public boolean canContract(Deity deity) {
        return Setting.NO_FACTION_CONTRACT_MODE || deity.getFamilies().contains(family);
    }

    public void calculateAscensions(boolean announce) {
        Player player = getOfflinePlayer().getPlayer();
        if (getLevel() >= Setting.ASCENSION_CAP) return;
        boolean did = false;
        while (getTotalExperience() >= (int) Math.ceil(500 * Math.pow(getLevel() + 1, 2.02)) && getLevel() < Setting.ASCENSION_CAP) {
            did = true;
            setMaxHealth(getMaxHealth() + 10.0);
            player.setMaxHealth(getMaxHealth());
            player.setHealthScale(20.0);
            player.setHealthScaled(true);
            player.setHealth(getMaxHealth());

            setLevel(getLevel() + 1);
        }
        if (did && announce) {
            player.sendMessage(ChatColor.AQUA + "Congratulations! Your Ascensions have increased to " + getLevel() + ".");
            player.sendMessage(ChatColor.YELLOW + "Your maximum HP has increased to " + getMaxHealth() + ".");
        }
        DGData.PLAYER_R.register(this);
    }

    public int costForNextAspect() {
        if (Setting.NO_COST_ASPECT_MODE) return 0;
        switch (aspects.size()) {
            case 1:
                return 0;
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
        if (Bukkit.getPlayer(UUID.fromString(mojangId)) == null) return;

        // Define variables
        final Player player = Bukkit.getPlayer(UUID.fromString(mojangId));
        final boolean inNoPvpZone = ZoneUtil.inNoPvpZone(player.getLocation());

        if (DGData.BATTLE_R.isInBattle(this)) return;

        if (!getCanPvp() && !inNoPvpZone) {
            setCanPvp(true);
            player.sendMessage(ChatColor.GRAY + "You can now enter in a battle.");
        } else if (!inNoPvpZone) {
            setCanPvp(true);
            DGData.SERVER_R.remove(player.getName(), "pvp_cooldown");
        } else if (getCanPvp() && !DGData.SERVER_R.contains(player.getName(), "pvp_cooldown")) {
            int delay = 10;
            DGData.SERVER_R.put(player.getName(), "pvp_cooldown", true, delay, TimeUnit.SECONDS);
            final PlayerModel THIS = this;
            Bukkit.getScheduler().scheduleSyncDelayedTask(DGData.PLUGIN, new BukkitRunnable() {
                @Override
                public void run() {
                    if (ZoneUtil.inNoPvpZone(player.getLocation())) {
                        if (DGData.BATTLE_R.isInBattle(THIS)) return;
                        setCanPvp(false);
                        player.sendMessage(ChatColor.GRAY + "You are now safe from other players.");
                    }
                }
            }, (delay * 20));
        }
    }

    @Deprecated
    public void cleanse() {
        // Neutral faction
        family = Family.NEUTRAL;

        // Empty deities
        god = Optional.empty();
        hero = Optional.empty();

        // Reset stats
        maxHealth = 20.0;
        favor = 700.0;
        level = 0;
        experience.clear();

        // Clear binds and aspects
        binds.clear();
        aspects.clear();

        // Check for demo
        if (Setting.DEBUG_DATA) {
            handleDemo();
        }

        // Save
        DGData.PLAYER_R.register(this);
    }

    private void handleDemo() {
        // Debug deities
        god = Optional.of(Demo.D.LOREM);

        // Debug aspects
        int roll = RandomUtil.generateIntRange(0, 2);
        if (roll == 0) {
            hero = Optional.of(Demo.D.IPSUM);
            family = Demo.D.IPSUM.getFamilies().get(0);
            addAspect(Aspects.BLOODLUST_ASPECT_HERO);
            setExperience(Aspects.BLOODLUST_ASPECT_HERO, 1000, false);
        } else if (roll == 1) {
            hero = Optional.of(Demo.D.DOLOR);
            family = Demo.D.DOLOR.getFamilies().get(0);
            addAspect(Aspects.MAGNETISM_ASPECT_HERO);
            setExperience(Aspects.MAGNETISM_ASPECT_HERO, 5000, false);
        } else {
            god = Optional.of(Demo.D.SIT);
            hero = Optional.of(Demo.D.AMET);
            family = Demo.D.AMET.getFamilies().get(0);
            addAspect(Aspects.WATER_ASPECT_HERO);
            setExperience(Aspects.WATER_ASPECT_HERO, 1000, false);
        }
    }
}
