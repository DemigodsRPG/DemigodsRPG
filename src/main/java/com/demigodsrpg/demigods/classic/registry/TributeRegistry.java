package com.demigodsrpg.demigods.classic.registry;

import com.demigodsrpg.demigods.classic.model.TributeModel;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TributeRegistry extends AbstractRegistry<Material, TributeModel> {
    private final String FILE_NAME = "tributes.dgc";

    @Override
    public Material keyFromString(String stringKey) {
        return Material.valueOf(stringKey);
    }

    @Override
    public TributeModel valueFromData(String stringKey, ConfigurationSection data) {
        return new TributeModel(keyFromString(stringKey), data);
    }

    @Override
    public String getFileName() {
        return FILE_NAME;
    }

    public void save(Material material, int amount) {
        // Remove the data if it exists already
        remove(material);

        // Make the new one
        register(new TributeModel(material, amount));
    }

    public void remove(Material material) {
        if (fromId(material) != null) {
            unregister(fromId(material));
        }
    }

    @Override
    public TributeModel fromId(Material material) {
        TributeModel model = super.fromId(material);
        if (model == null) {
            model = new TributeModel(material, 1);
            register(model);
        }
        return model;
    }

    public Collection<TributeModel> find(final String category) {
        return Collections2.filter(getRegistered(), new Predicate<TributeModel>() {
            @Override
            public boolean apply(TributeModel tributeModel) {
                return category.equalsIgnoreCase(tributeModel.getCategory());
            }
        });
    }

    /**
     * Returns all saved tribute data with attached current value.
     *
     * @return a Map of all tribute data.
     */
    public Map<Material, Integer> getTributeValuesMap() {
        Map<Material, Integer> map = new HashMap<>();
        for (TributeModel data : getRegistered()) {
            map.put(data.getMaterial(), getValue(data.getMaterial()));
        }
        return map;
    }

    /**
     * Returns the total number of tributes for the entire server.
     *
     * @return the total server tributes.
     */
    public int getTotalTributes() {
        int total = 1;
        for (TributeModel data : getRegistered()) {
            total += data.getFitness();
        }
        return total;
    }

    /**
     * Returns the number of tributes for the <code>material</code>.
     *
     * @param material the material to check.
     * @return the total number of tributes.
     */
    public int getTributes(Material material) {
        TributeModel data = fromId(material);
        if (data != null) return data.getFitness();
        else return 1;
    }

    /**
     * Returns the number of tributes for the <code>category</code>.
     *
     * @param category the category to check.
     * @return the total number of tributes.
     */
    @Deprecated
    public int getTributesForCategory(String category) {
        int total = 1;
        for (TributeModel data : find(category))
            total += data.getFitness();
        return total;
    }

    /**
     * Saves the <code>item</code> amount into the server tribute stats.
     *
     * @param item the item whose amount to save.
     */
    public void saveTribute(ItemStack item) {
        TributeModel data = fromId(item.getType());

        if (data != null) {
            data.setFitness(data.getFitness() + item.getAmount());
        } else {
            save(item.getType(), item.getAmount());
        }
    }

    /**
     * Returns the value for a <code>material</code>.
     */
    public int getValue(Material material) {
        int lastKnownValue = (int) fromId(material).getLastKnownValue();
        return lastKnownValue > 0 ? lastKnownValue : 1;
    }

    /**
     * Returns the value for the <code>item</code> based on current tribute stats.
     *
     * @param item the item whose value to calculate.
     * @return the value of the item.
     */
    public int getValue(ItemStack item) {
        return getValue(item.getType());
    }

    /**
     * Returns the category of the <code>material</code>.
     *
     * @param material the material whose category to check
     * @return the category
     */
    @Deprecated
    public String getCategory(Material material) {
        switch (material) {
            case DRAGON_EGG:
            case NETHER_STAR:
                return "boss_reward";
            case DIAMOND_ORE:
            case IRON_ORE:
            case GOLD_ORE:
            case LAPIS_ORE:
            case COAL_ORE:
                return "raw_ore";
            case GLOWSTONE_DUST:
            case GLOWSTONE:
            case REDSTONE:
            case REDSTONE_BLOCK:
            case BLAZE_ROD:
            case GHAST_TEAR:
            case MOSSY_COBBLESTONE:
            case OBSIDIAN:
            case GRASS:
            case STONE:
            case LOG:
                return "mid_range";
            case DIRT:
            case SAND:
            case GRAVEL:
            case COBBLESTONE:
            case SANDSTONE:
                return "worthless";
            default:
                if (material.name().contains("WOOD")) {
                    return "wood";
                } else if (material.name().contains("STONE")) {
                    return "stone";
                } else if (material.name().contains("IRON")) {
                    return "iron";
                } else if (material.name().contains("GOLD")) {
                    return "gold";
                } else if (material.name().contains("DIAMOND")) {
                    return "diamond";
                } else if (material.name().contains("EMERALD")) {
                    return "emerald";
                } else if (material.name().contains("COAL")) {
                    return "coal";
                } else if (material.name().contains("LEATHER")) {
                    return "leather";
                }
                // TODO More
                return "default";
        }
    }

    /**
     * Called when actually tributing the <code>item</code>.
     *
     * @param item the item to process.
     * @return the value of the item.
     */
    public int processTribute(ItemStack item) {
        // Grab the value before
        int value = getValue(item);

        // Save the tribute to be used in calculations later
        saveTribute(item);

        // Return the value
        return value;
    }

    /**
     * Returns the base value of the <code>material</code>.
     *
     * @param material the material whose value to return.
     * @return the base value of the item.
     */
    public double getBaseTributeValue(Material material) {
        // TODO: BALANCE THIS SHIT.

        double value;
        switch (material) {
            case ENDER_PORTAL_FRAME:
                value = 23;
                break;
            case CAULDRON_ITEM:
                value = 84;
                break;
            case LAVA_BUCKET:
                value = 36.5;
                break;
            case MILK_BUCKET:
                value = 36.5;
                break;
            case WATER_BUCKET:
                value = 36.5;
                break;
            case NETHER_WARTS:
                value = 13.2;
                break;
            case NETHER_STAR:
                value = 150;
                break;
            case BEACON:
                value = 150;
                break;
            case SADDLE:
                value = 5.3;
                break;
            case EYE_OF_ENDER:
                value = 18;
                break;
            case STONE:
                value = 1.5;
                break;
            case COBBLESTONE:
                value = 1.3;
                break;
            case LOG:
                value = 1;
                break;
            case WOOD:
                value = 1.23;
                break;
            case STICK:
                value = 1.11;
                break;
            case GLASS:
                value = 1.5;
                break;
            case LAPIS_BLOCK:
                value = 85;
                break;
            case SANDSTONE:
                value = 1.9;
                break;
            case GOLD_BLOCK:
                value = 100;
                break;
            case IRON_BLOCK:
                value = 120;
                break;
            case BRICK:
                value = 10;
                break;
            case TNT:
                value = 10;
                break;
            case MOSSY_COBBLESTONE:
                value = 50;
                break;
            case OBSIDIAN:
                value = 10;
                break;
            case DIAMOND_BLOCK:
                value = 180;
                break;
            case CACTUS:
                value = 1.7;
                break;
            case YELLOW_FLOWER:
                value = 1.1;
                break;
            case SEEDS:
                value = 1.3;
                break;
            case PUMPKIN:
                value = 3;
                break;
            case CAKE:
                value = 15;
                break;
            case APPLE:
                value = 3;
                break;
            case CARROT:
            case POTATO:
                value = 1.7;
                break;
            case COAL:
                value = 2.5;
                break;
            case DIAMOND:
                value = 20;
                break;
            case IRON_ORE:
                value = 7;
                break;
            case GOLD_ORE:
                value = 13;
                break;
            case IRON_INGOT:
                value = 12;
                break;
            case GOLD_INGOT:
                value = 16;
                break;
            case GOLD_NUGGET:
                value = 2;
                break;
            case STRING:
                value = 2.4;
                break;
            case WHEAT:
                value = 1.6;
                break;
            case BREAD:
                value = 3;
                break;
            case RAW_FISH:
            case PORK:
                value = 2.5;
                break;
            case COOKED_FISH:
            case GRILLED_PORK:
                value = 4;
                break;
            case GOLDEN_APPLE:
                value = 80;
                break;
            case GOLDEN_CARROT:
                value = 17;
                break;
            case GOLD_RECORD:
                value = 60;
                break;
            case GREEN_RECORD:
                value = 60;
                break;
            case GLOWSTONE:
                value = 1.7;
                break;
            case REDSTONE:
                value = 3.3;
                break;
            case REDSTONE_BLOCK:
                value = 27.7;
                break;
            case EGG:
                value = 1.3;
                break;
            case SUGAR:
                value = 1.2;
                break;
            case BONE:
                value = 3;
                break;
            case ENDER_PEARL:
                value = 1.7;
                break;
            case SULPHUR:
                value = 1.2;
                break;
            case COCOA:
                value = 1.6;
                break;
            case ROTTEN_FLESH:
                value = 3;
                break;
            case RAW_CHICKEN:
                value = 2;
                break;
            case COOKED_CHICKEN:
                value = 2.6;
                break;
            case RAW_BEEF:
                value = 2;
                break;
            case COOKED_BEEF:
                value = 2.7;
                break;
            case MELON:
                value = 1.8;
                break;
            case COOKIE:
                value = 1.45;
                break;
            case VINE:
                value = 1.2;
                break;
            case EMERALD:
                value = 17;
                break;
            case EMERALD_BLOCK:
                value = 153;
                break;
            case DRAGON_EGG:
                value = 200;
                break;
            default:
                value = 1.0;
                break;
        }

        // Return
        return value;
    }
}
