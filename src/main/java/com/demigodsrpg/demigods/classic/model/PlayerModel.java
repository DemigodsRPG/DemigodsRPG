package com.demigodsrpg.demigods.classic.model;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.Setting;
import com.demigodsrpg.demigods.classic.battle.Battle;
import com.demigodsrpg.demigods.classic.battle.Participant;
import com.demigodsrpg.demigods.classic.deity.Deity;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.registry.AbilityRegistry;
import com.demigodsrpg.demigods.classic.util.JsonSection;
import com.demigodsrpg.demigods.classic.util.ZoneUtil;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PlayerModel extends AbstractPersistentModel<String> implements Participant {
    private String mojangId;
    private String lastKnownName;

    private String majorDeity;
    private Set<String> contractedDeities = new HashSet<>();
    private IDeity.Alliance acceptedAlliance;
    private BiMap<String, String> binds = HashBiMap.create();

    private Map<String, Double> devotion;

    private long lastLoginTime;

    private double maxHealth;
    private double favor;
    private int ascensions;

    private boolean canPvp;

    private int kills;
    private int deaths;
    private int teamKills;

    @SuppressWarnings("deprecation")
    public PlayerModel(Player player) {
        mojangId = player.getUniqueId().toString();
        lastKnownName = player.getName();
        lastLoginTime = System.currentTimeMillis();

        majorDeity = Deity.HUMAN.name();
        devotion = new HashMap<>();

        acceptedAlliance = IDeity.Alliance.NEUTRAL;

        maxHealth = 20.0;

        favor = 700.0;
        ascensions = 0;

        canPvp = true;

        kills = 0;
        deaths = 0;
        teamKills = 0;
    }

    public PlayerModel(String mojangId, JsonSection conf) {
        this.mojangId = mojangId;
        lastKnownName = conf.getString("last_known_name");
        lastLoginTime = conf.getLong("last_login_time");
        majorDeity = conf.getString("major_deity");
        for (String name : conf.getStringList("contracted_deities")) {
            contractedDeities.add(name);
        }
        acceptedAlliance = IDeity.Alliance.valueOf(conf.getString("alliance"));
        binds.putAll((Map) conf.getSection("binds").getValues());
        maxHealth = conf.getDouble("max_health");
        favor = conf.getDouble("favor");
        devotion = Maps.newHashMap(Maps.transformEntries(conf.getSection("devotion").getValues(), new Maps.EntryTransformer<String, Object, Double>() {
            @Override
            public Double transformEntry(String s, Object o) {
                return Double.parseDouble(o.toString());
            }
        }));
        ascensions = conf.getInt("ascensions");
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
    public String getPersistantId() {
        return mojangId;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("last_known_name", lastKnownName);
        map.put("last_login_time", lastLoginTime);
        map.put("major_deity", majorDeity);
        map.put("contracted_deities", Lists.newArrayList(contractedDeities));
        map.put("alliance", acceptedAlliance.name());
        map.put("binds", binds);
        map.put("max_health", maxHealth);
        map.put("favor", favor);
        map.put("devotion", devotion);
        map.put("ascensions", ascensions);
        map.put("can_pvp", canPvp);
        map.put("kills", kills);
        map.put("deaths", deaths);
        map.put("team_kills", teamKills);
        return map;
    }

    public UUID getMojangId() {
        return UUID.fromString(mojangId);
    }

    public String getLastKnownName() {
        return lastKnownName;
    }

    public void setLastKnownName(String lastKnownName) {
        this.lastKnownName = lastKnownName;
        DGClassic.PLAYER_R.register(this);
    }

    public Long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        DGClassic.PLAYER_R.register(this);
    }

    public Deity getMajorDeity() {
        return Deity.valueOf(majorDeity);
    }

    public void setMajorDeity(Deity majorDeity) {
        this.majorDeity = majorDeity.name();
        DGClassic.PLAYER_R.register(this);
    }

    public Set<String> getAllDeities() {
        Set<String> deities = new HashSet<>();
        deities.addAll(getContractedDeities());
        deities.add(majorDeity);
        return deities;
    }

    public Set<String> getContractedDeities() {
        return contractedDeities;
    }

    public void addContractedDeity(Deity deity) {
        contractedDeities.add(deity.name());
        DGClassic.PLAYER_R.register(this);
    }

    public void removeContractedDeity(Deity deity) {
        contractedDeities.remove(deity.name());
        DGClassic.PLAYER_R.register(this);
    }

    @Override
    public IDeity.Alliance getAlliance() {
        return acceptedAlliance;
    }

    public void setAlliance(IDeity.Alliance acceptedAlliance) {
        this.acceptedAlliance = acceptedAlliance;
        DGClassic.PLAYER_R.register(this);
    }

    public Double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(Double maxHealth) {
        this.maxHealth = maxHealth;
        DGClassic.PLAYER_R.register(this);
    }

    public Double getFavor() {
        return favor;
    }

    public void setFavor(Double favor) {
        this.favor = favor;
        DGClassic.PLAYER_R.register(this);
    }

    public Double getDevotion(Deity deity) {
        if (!devotion.containsKey(deity.name())) {
            return 0.0;
        }
        return devotion.get(deity.name());
    }

    public Double getDevotion(String deityName) {
        if (!devotion.containsKey(deityName)) {
            return 0.0;
        }
        return devotion.get(deityName);
    }

    public Double getTotalDevotion() {
        double total = getDevotion(majorDeity);
        for (String deity : contractedDeities) {
            total += getDevotion(deity);
        }
        return total;
    }

    public void setDevotion(Deity deity, Double devotion) {
        this.devotion.put(deity.name(), devotion);
        calculateAscensions();
        DGClassic.PLAYER_R.register(this);
    }

    public void setDevotion(String deityName, Double devotion) {
        this.devotion.put(deityName, devotion);
        calculateAscensions();
        DGClassic.PLAYER_R.register(this);
    }

    public Integer getAscensions() {
        return ascensions;
    }

    public void setAscensions(Integer ascensions) {
        this.ascensions = ascensions;
        DGClassic.PLAYER_R.register(this);
    }

    public Map<String, String> getBindsMap() {
        return binds;
    }

    public AbilityRegistry.Data getBound(Material material) {
        if (binds.inverse().containsKey(material.name())) {
            return DGClassic.ABILITY_R.fromCommand(binds.inverse().get(material.name()));
        }
        return null;
    }

    public Material getBound(AbilityRegistry.Data ability) {
        return getBound(ability.getCommand());
    }

    public Material getBound(String abilityCommand) {
        if (binds.containsKey(abilityCommand)) {
            return Material.valueOf(binds.get(abilityCommand));
        }
        return null;
    }

    public void bind(AbilityRegistry.Data ability, Material material) {
        binds.put(ability.getCommand(), material.name());
        DGClassic.PLAYER_R.register(this);
    }

    public void bind(String abilityCommand, Material material) {
        binds.put(abilityCommand, material.name());
        DGClassic.PLAYER_R.register(this);
    }

    public void unbind(AbilityRegistry.Data ability) {
        binds.remove(ability.getCommand());
        DGClassic.PLAYER_R.register(this);
    }

    public void unbind(String abilityCommand) {
        binds.remove(abilityCommand);
        DGClassic.PLAYER_R.register(this);
    }

    public void unbind(Material material) {
        binds.inverse().remove(material.name());
        DGClassic.PLAYER_R.register(this);
    }

    public boolean getCanPvp() {
        return canPvp;
    }

    public void setCanPvp(Boolean canPvp) {
        this.canPvp = canPvp;
        DGClassic.PLAYER_R.register(this);
    }

    public Integer getKills() {
        return kills;
    }

    public void addKill() {
        kills++;
        DGClassic.PLAYER_R.register(this);
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void addDeath() {
        deaths++;
        DGClassic.PLAYER_R.register(this);
    }

    public Integer getTeamKills() {
        return teamKills;
    }

    public void addTeamKill() {
        teamKills++;
        DGClassic.PLAYER_R.register(this);
    }

    public void resetTeamKills() {
        teamKills = 0;
        DGClassic.PLAYER_R.register(this);
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

    @SuppressWarnings("RedundantCast")
    @Override
    public boolean reward(Battle.Data data) {
        double devotion = getTotalDevotion();
        teamKills += data.getTeamKills();

        if (checkTeamKills()) {
            double score = data.getHits() + data.getAssists() / 2;
            score += data.getDenies();
            score += data.getKills() * 2;
            score -= data.getDeaths() * 1.5;
            score *= (double) Setting.EXP_MULTIPLIER.get();
            score /= contractedDeities.size() + 1;
            setDevotion(majorDeity, getDevotion(majorDeity) + score);
            for (String deity : contractedDeities) {
                setDevotion(deity, getDevotion(deity) + score);
            }
        }

        DGClassic.PLAYER_R.register(this);

        return devotion > getTotalDevotion();
    }

    public boolean checkTeamKills() {
        int maxTeamKills = Setting.MAX_TEAM_KILLS.get();
        if (maxTeamKills <= teamKills) {
            // Reset them to excommunicated
            setAlliance(IDeity.Alliance.EXCOMMUNICATED);
            resetTeamKills();
            double former = getTotalDevotion();
            setDevotion(majorDeity, 0.0);
            if (getOnline()) {
                Player player = getOfflinePlayer().getPlayer();
                player.sendMessage(ChatColor.RED + "Your former alliance has just excommunicated you.");
                player.sendMessage(ChatColor.RED + "You will no longer respawn at the alliance spawn.");
                player.sendMessage(ChatColor.RED + "You have lost " +
                        ChatColor.GOLD + DecimalFormat.getCurrencyInstance().format(former - getTotalDevotion()) +
                        ChatColor.RED + " devotion.");
                player.sendMessage(ChatColor.YELLOW + "To join an alliance, "); // TODO
            }
            return false;
        }
        return true;
    }

    public void giveMajorDeity(Deity deity, boolean firstTime) {
        setMajorDeity(deity);
        if (firstTime) {
            setAlliance(deity.getDefaultAlliance());
            setMaxHealth(25.0);
            setAscensions(1);
        }
        setDevotion(deity, 20.0);
        calculateAscensions();
    }

    public void giveDeity(Deity deity) {
        contractedDeities.add(deity.name());
        setDevotion(deity, 20.0);
    }

    public void calculateAscensions() {
        Player player = getOfflinePlayer().getPlayer();
        if (getAscensions() >= (int) Setting.ASCENSION_CAP.get()) return;
        while (getTotalDevotion() >= (int) Math.ceil(500 * Math.pow(getAscensions() + 1, 2.02)) && getAscensions() < (int) Setting.ASCENSION_CAP.get()) {
            setMaxHealth(getMaxHealth() + 10.0);
            player.setMaxHealth(getMaxHealth());
            player.setHealthScale(20.0);
            player.setHealthScaled(true);
            player.setHealth(getMaxHealth());

            setAscensions(getAscensions() + 1);

            player.sendMessage(ChatColor.AQUA + "Congratulations! Your Ascensions increased to " + getAscensions() + ".");
            player.sendMessage(ChatColor.YELLOW + "Your maximum HP has increased to " + getMaxHealth() + ".");
        }
        DGClassic.PLAYER_R.register(this);
    }

    public int costForNextDeity() {
        switch (contractedDeities.size() + 1) {
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

        if (DGClassic.BATTLE_R.isInBattle(this)) return;

        if (!getCanPvp() && !inNoPvpZone) {
            setCanPvp(true);
            player.sendMessage(ChatColor.GRAY + "You can now enter in a battle.");
        } else if (!inNoPvpZone) {
            setCanPvp(true);
            DGClassic.SERV_R.remove(player.getName(), "pvp_cooldown");
        } else if (getCanPvp() && !DGClassic.SERV_R.contains(player.getName(), "pvp_cooldown")) {
            int delay = 10;
            DGClassic.SERV_R.put(player.getName(), "pvp_cooldown", true, delay, TimeUnit.SECONDS);
            final PlayerModel THIS = this;
            Bukkit.getScheduler().scheduleSyncDelayedTask(DGClassic.getInst(), new BukkitRunnable() {
                @Override
                public void run() {
                    if (ZoneUtil.inNoPvpZone(player.getLocation())) {
                        if (DGClassic.BATTLE_R.isInBattle(THIS)) return;
                        setCanPvp(false);
                        player.sendMessage(ChatColor.GRAY + "You are now safe from other players.");
                    }
                }
            }, (delay * 20));
        }
    }
}
