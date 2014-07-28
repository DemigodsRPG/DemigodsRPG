package com.demigodsrpg.demigods.classic.model;

import com.censoredsoftware.library.util.MapUtil2;
import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.registry.TributeRegistry;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class TributeModel extends AbstractPersistentModel<Material> {
    private static final Double VALUE_K = 71.43;

    private Material material;
    private List<Long> tributeTimes;
    private Integer fitness;
    private TributeRegistry.Category category;
    private Double lastKnownValue;

    public TributeModel(Material material, ConfigurationSection conf) {
        this.material = material;
        tributeTimes = conf.getLongList("tributeTimes");
        fitness = conf.getInt("fitness");
        category = DGClassic.TRIBUTE_R.getCategory(material);
        lastKnownValue = conf.getDouble("lastKnownValue");
    }

    public TributeModel(Material material, int fitness) {
        this.material = material;
        tributeTimes = new ArrayList<>();
        this.fitness = fitness;
        category = DGClassic.TRIBUTE_R.getCategory(material);
        lastKnownValue = getValuePercentage();
    }

    public Material getMaterial() {
        return material;
    }

    public List<Long> getTributeTimes() {
        long twoWeeksAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(14);
        for (Long time : tributeTimes) {
            if (time < twoWeeksAgo) {
                tributeTimes.remove(time);
            }
        }
        return tributeTimes;
    }

    public void addTributeTime() {
        tributeTimes.add(System.currentTimeMillis());
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int amount) {
        this.fitness = amount;
        tributeTimes.add(System.currentTimeMillis());
        DGClassic.TRIBUTE_R.register(this);
    }

    public TributeRegistry.Category getCategory() {
        return category;
    }

    public double getFrequency() {
        return getTributeTimes().size() / 336;
    }

    public double getLastKnownValue() {
        return lastKnownValue;
    }

    private void updateValue(double percentOffset) {
        if (getCategory().equals("worthless")) {
            lastKnownValue = 0.0;
        }
        lastKnownValue = (getValuePercentage() / percentOffset) * VALUE_K * DGClassic.TRIBUTE_R.getRegistered().size();
        DGClassic.TRIBUTE_R.register(this);
    }

    private double getValuePercentage() {
        Collection<TributeModel> allInCat = DGClassic.TRIBUTE_R.find(getCategory());
        int size = allInCat.size();
        if (size < 2) {
            size = 2;
        }
        Map<TributeModel, Double> map = new HashMap<>();
        for (TributeModel model : allInCat) {
            map.put(model, model.getFrequency());
        }
        int count = 1;
        double rankInCategory = 1.0;
        for (TributeModel model : MapUtil2.sortByValue(map, false).keySet()) {
            if (model.equals(this)) {
                rankInCategory = (double) count;
                break;
            }
        }
        double fractionOfCategory = rankInCategory / size;
        double categoryFitness = DGClassic.TRIBUTE_R.getTributesForCategory(getCategory());
        double fractionOfTotal = 1 - (categoryFitness / DGClassic.TRIBUTE_R.getTotalTributes());
        return fractionOfTotal * fractionOfCategory;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("tributeTimes", tributeTimes);
        map.put("fitness", fitness);
        map.put("lastKnownValue", lastKnownValue);
        return map;
    }

    @Override
    public Material getPersistantId() {
        return getMaterial();
    }

    public static class ValueTask extends BukkitRunnable {
        @Override
        public void run() {
            double percentOffset = 0;
            for (TributeModel model : DGClassic.TRIBUTE_R.getRegistered()) {
                percentOffset += model.getValuePercentage();
            }
            for (TributeModel model : DGClassic.TRIBUTE_R.getRegistered()) {
                model.updateValue(percentOffset);
            }
        }
    }
}
