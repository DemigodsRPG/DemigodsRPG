package com.demigodsrpg.demigods.classic.model;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.Setting;
import com.demigodsrpg.demigods.classic.battle.Battle;
import com.demigodsrpg.demigods.classic.battle.Participant;
import com.demigodsrpg.demigods.classic.deity.Deity;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.registry.AbilityRegistry;
import com.demigodsrpg.demigods.classic.util.ZoneUtil;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PlayerModel extends AbstractPersistentModel<UUID> implements Participant {
    private UUID mojangId;
    private String lastKnownName;
    private Long lastLoginTime;

    private Deity majorDeity;
    private Set<Deity> contractedDeities = new HashSet<>();
    private Map<String, Double> devotion;
    private IDeity.Alliance acceptedAlliance;

    private BiMap<String, String> binds = HashBiMap.create();

    private Double maxHealth;
    private Double maxFavor;
    private Double favor;
    private Integer ascensions;

    private Boolean canPvp;

    private Integer kills;
    private Integer deaths;
    private Integer teamKills;

    @SuppressWarnings("deprecation")
    public PlayerModel(Player player) {
        mojangId = player.getUniqueId();
        lastKnownName = player.getName();
        lastLoginTime = System.currentTimeMillis();

        majorDeity = Deity.HUMAN;
        devotion = new HashMap<>();

        acceptedAlliance = IDeity.Alliance.NEUTRAL;

        maxHealth = 20.0;
        maxFavor = 0.0;

        favor = 0.0;
        ascensions = 0;

        canPvp = true;

        kills = 0;
        deaths = 0;
        teamKills = 0;
    }

    public PlayerModel(UUID mojangId, ConfigurationSection conf) {
        this.mojangId = mojangId;
        lastKnownName = conf.getString("lastKnownName");
        lastLoginTime = conf.getLong("lastLoginTime");
        majorDeity = Deity.valueOf(conf.getString("majorDeity"));
        for (String name : conf.getStringList("contractedDeities")) {
            contractedDeities.add(Deity.valueOf(name));
        }
        acceptedAlliance = IDeity.Alliance.valueOf(conf.getString("alliance"));
        binds.putAll((Map) conf.getConfigurationSection("binds").getValues(false));
        maxHealth = conf.getDouble("maxHealth");
        maxFavor = conf.getDouble("maxFavor");
        favor = conf.getDouble("favor");
        devotion = Maps.newHashMap(Maps.transformEntries(conf.getConfigurationSection("devotion").getValues(false), new Maps.EntryTransformer<String, Object, Double>() {
            @Override
            public Double transformEntry(String s, Object o) {
                return Double.parseDouble(o.toString());
            }
        }));
        ascensions = conf.getInt("ascensions");
        canPvp = conf.getBoolean("canPvp", true);
        kills = conf.getInt("kills");
        deaths = conf.getInt("deaths");
        teamKills = conf.getInt("teamKills");
    }

    @Override
    public Type getType() {
        return Type.PERSISTENT;
    }

    @Override
    public UUID getPersistantId() {
        return getMojangId();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("lastKnownName", lastKnownName);
        map.put("lastLoginTime", lastLoginTime);
        map.put("majorDeity", majorDeity.name());
        List<String> contractedDeities = new ArrayList<>();
        for (Deity deity : this.contractedDeities) {
            contractedDeities.add(deity.name());
        }
        map.put("contractedDeities", contractedDeities);
        map.put("alliance", acceptedAlliance.name());
        map.put("binds", Maps.newHashMap(binds));
        map.put("maxHealth", maxHealth);
        map.put("maxFavor", maxFavor);
        map.put("favor", favor);
        map.put("devotion", devotion);
        map.put("ascensions", ascensions);
        map.put("canPvp", canPvp);
        map.put("kills", kills);
        map.put("deaths", deaths);
        map.put("teamKills", teamKills);
        return map;
    }

    public UUID getMojangId() {
        return mojangId;
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

    public IDeity getMajorDeity() {
        return majorDeity;
    }

    public void setMajorDeity(Deity majorDeity) {
        this.majorDeity = majorDeity;
        DGClassic.PLAYER_R.register(this);
    }

    public Set<Deity> getContractedDeities() {
        return contractedDeities;
    }

    public void addContractedDeity(Deity deity) {
        contractedDeities.add(deity);
        DGClassic.PLAYER_R.register(this);
    }

    public void removeContractedDeity(Deity deity) {
        contractedDeities.remove(deity);
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

    public Double getMaxFavor() {
        return maxFavor;
    }

    public void setMaxFavor(Double maxFavor) {
        this.maxFavor = maxFavor;
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
        if (!devotion.containsKey(deity.toString())) {
            return 0.0;
        }
        return devotion.get(deity.name());
    }

    public Double getTotalDevotion() {
        double total = getDevotion(majorDeity);
        for (Deity deity : contractedDeities) {
            total += getDevotion(deity);
        }
        return total;
    }

    public void setDevotion(Deity deity, Double devotion) {
        this.devotion.put(deity.name(), devotion);
        DGClassic.PLAYER_R.register(this);
    }

    public Integer getAscensions() {
        return ascensions;
    }

    public void setAscensions(Integer ascensions) {
        this.ascensions = ascensions;
        DGClassic.PLAYER_R.register(this);
    }

    public AbilityRegistry.Data getBound(Material material) {
        if(binds.inverse().containsKey(material.name())) {
           return DGClassic.ABILITY_R.fromCommand(binds.inverse().get(material.name()));
        }
        return null;
    }

    public Material getBound(AbilityRegistry.Data ability) {
        return getBound(ability.getCommand());
    }

    public Material getBound(String abilityCommand) {
        if(binds.containsKey(abilityCommand)) {
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
        return Bukkit.getOfflinePlayer(mojangId);
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
            for (Deity deity : contractedDeities) {
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

    public void giveMajorDeity(Deity deity) {
        setMajorDeity(deity);
        setAlliance(deity.getDefaultAlliance());
        setMaxHealth(25.0);
        setMaxFavor(700.0);
        setFavor(getMaxFavor());
        setDevotion(deity, 20.0);
        setAscensions(1);
        calculateAscensions();
    }

    public void giveDeity(Deity deity) {
        contractedDeities.add(deity);
        setDevotion(deity, 20.0);
    }

    public void calculateAscensions() {
        Player player = getOfflinePlayer().getPlayer();
        if (getAscensions() >= (int) Setting.ASCENSION_CAP.get()) return;
        while (getTotalDevotion() >= (int) Math.ceil(500 * Math.pow(getAscensions() + 1, 2.02)) && getAscensions() < (int) Setting.ASCENSION_CAP.get()) {
            setMaxHealth(getMaxHealth() + 10.0);
            player.setHealth(getMaxHealth());
            player.setMaxHealth(getMaxHealth());
            player.setHealthScale(getMaxHealth());

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
        } else if (getCanPvp() && !DGClassic.SERV_R.exists(player.getName(), "pvp_cooldown")) {
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

    public void updateFavor() {
        if(getFavor() < getMaxFavor()) {   
            setFavor(getFavor() + 4);
        }
    }
}
