package com.demigodsrpg.demigods.classic.model;

import com.demigodsrpg.demigods.classic.DGClassic;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class TributeModel extends AbstractPersistentModel<Material> {
    private Material material;
    private String category;
    private Integer amount;

    public TributeModel(Material material, ConfigurationSection conf) {
        this.material = material;
        category = conf.getString("category");
        amount = conf.getInt("amount");
    }

    public TributeModel(String category, Material material, int amount) {
        this.category = category;
        this.material = material;
        this.amount = amount;
    }

    public String getCategory() {
        return this.category;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        DGClassic.TRIBUTE_R.register(this);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("category", category);
        map.put("amount", amount);
        return null;
    }

    @Override
    public Material getPersistantId() {
        return getMaterial();
    }
}
