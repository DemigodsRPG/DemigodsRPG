package com.demigodsrpg.demigods.classic.model;

import com.censoredsoftware.library.util.MapUtil2;
import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.registry.TributeRegistry;
import com.demigodsrpg.demigods.classic.util.JsonSection;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class TributeModel extends AbstractPersistentModel<String> {
    private static final Double VALUE_K = 14.286;
    private static Double OFFSET = 1.0;

    private Material material;
    private List<Double> tributeTimes;
    private Integer fitness;
    private TributeRegistry.Category category;
    private Double lastKnownValue;

    public TributeModel(Material material, JsonSection conf) {
        this.material = material;
        tributeTimes = conf.getDoubleList("tributeTimes");
        fitness = conf.getInt("fitness");
        category = DGClassic.TRIBUTE_R.getCategory(material);
        lastKnownValue = conf.getDouble("lastKnownValue");
    }

    public TributeModel(Material material, int fitness) {
        this.material = material;
        tributeTimes = new ArrayList<>();
        this.fitness = fitness;
        category = DGClassic.TRIBUTE_R.getCategory(material);
        lastKnownValue = 1.0;
    }

    public Material getMaterial() {
        return material;
    }

    public List<Double> getTributeTimes() {
        long twoWeeksAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(14);
        for (Double time : tributeTimes) {
            if (time < twoWeeksAgo) {
                tributeTimes.remove(time);
            }
        }
        return tributeTimes;
    }

    public void addTributeTime() {
        tributeTimes.add((double) System.currentTimeMillis());
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int amount) {
        this.fitness = amount;
        tributeTimes.add((double) System.currentTimeMillis());
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

    private void updateValue() {
        if (getCategory().equals(TributeRegistry.Category.WORTHLESS)) {
            lastKnownValue = 0.0;
        } else if (getCategory().equals(TributeRegistry.Category.CHEATING)) {
            lastKnownValue = -3000.0;
        } else {
            lastKnownValue = (getValuePercentage() / OFFSET) * VALUE_K * DGClassic.TRIBUTE_R.getRegistered().size();
        }
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
    public String getPersistantId() {
        return getMaterial().name();
    }

    public static class ValueTask extends BukkitRunnable {
        @Override
        public void run() {
            OFFSET = 1.0;
            for (TributeModel model : DGClassic.TRIBUTE_R.getRegistered()) {
                OFFSET += model.getValuePercentage();
            }

            for (TributeModel model : DGClassic.TRIBUTE_R.getRegistered()) {
                // Trim the tribute times
                if (model.tributeTimes.size() > 30) {
                    model.tributeTimes = model.tributeTimes.subList(model.tributeTimes.size() - 31, model.tributeTimes.size() - 1);
                }

                // Update the value
                model.updateValue();
            }
        }
    }
}
