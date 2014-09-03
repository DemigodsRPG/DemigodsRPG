package com.demigodsrpg.demigods.classic.registry;

import com.censoredsoftware.library.util.RandomUtil;
import com.demigodsrpg.demigods.classic.model.TributeModel;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TributeRegistry extends AbstractRegistry<TributeModel> {
    private final String FILE_NAME = "tributes.dgc";

    @Override
    public TributeModel valueFromData(String stringKey, ConfigurationSection data) {
        return new TributeModel(Material.getMaterial(stringKey), data);
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
        if (fromId(material.name()) != null) {
            unregister(fromId(material.name()));
        }
    }

    @Override
    public TributeModel fromId(String materialName) {
        TributeModel model = super.fromId(materialName);
        if (model == null) {
            model = new TributeModel(Material.valueOf(materialName), 1);
            register(model);
        }
        return model;
    }

    public Collection<TributeModel> find(final Category category) {
        return Collections2.filter(getRegistered(), new Predicate<TributeModel>() {
            @Override
            public boolean apply(TributeModel tributeModel) {
                return category.equals(tributeModel.getCategory());
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
        TributeModel data = fromId(material.name());
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
    public int getTributesForCategory(Category category) {
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
        TributeModel data = fromId(item.getType().name());

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
        int lastKnownValue = (int) fromId(material.name()).getLastKnownValue();
        return lastKnownValue > 0 ? lastKnownValue : 1;
    }

    /**
     * Returns the value for the <code>item</code> based on current tribute stats.
     *
     * @param item the item whose value to calculate.
     * @return the value of the item.
     */
    public int getValue(ItemStack item) {
        return getValue(item.getType()) * item.getAmount();
    }

    /**
     * Returns the category of the <code>material</code>.
     *
     * @param material the material whose category to check
     * @return the category
     */
    @Deprecated
    public Category getCategory(final Material material) {
        switch (material) {
            case BEDROCK:
                return Category.CHEATING;
            case DRAGON_EGG:
            case NETHER_STAR:
                return Category.BOSS_REWARD;
            case GHAST_TEAR:
            case DIAMOND_ORE:
            case LAPIS_ORE:
            case CHAINMAIL_BOOTS:
            case CHAINMAIL_CHESTPLATE:
            case CHAINMAIL_HELMET:
            case CHAINMAIL_LEGGINGS:
                return Category.MID_HIGH_RANGE;
            case GLOWSTONE_DUST:
            case GLOWSTONE:
            case REDSTONE:
            case REDSTONE_BLOCK:
            case BLAZE_ROD:
            case MOSSY_COBBLESTONE:
            case OBSIDIAN:
            case REDSTONE_ORE:
            case GRASS:
            case GOLDEN_CARROT:
            case GOLDEN_APPLE:
                return Category.MID_RANGE;
            case COOKED_BEEF:
            case COOKED_CHICKEN:
            case COOKED_FISH:
            case GRILLED_PORK:
            case BAKED_POTATO:
            case MUSHROOM_SOUP:
            case CAKE:
            case PUMPKIN_PIE:
                return Category.COOKED_FOOD;
            case APPLE:
            case ROTTEN_FLESH:
            case RAW_BEEF:
            case RAW_CHICKEN:
            case RAW_FISH:
            case LEATHER:
            case WOOL:
            case INK_SACK:
            case EGG:
            case RED_ROSE:
            case SPIDER_EYE:
            case STRING:
            case BONE:
            case PORK:
            case FEATHER:
            case POTATO:
            case POISONOUS_POTATO:
            case CARROT:
            case PUMPKIN:
            case PUMPKIN_SEEDS:
            case MELON:
            case MELON_SEEDS:
            case SEEDS:
            case WHEAT:
            case HAY_BLOCK:
            case SULPHUR:
                return Category.MOB_LOOT;
            case LOG:
            case DIRT:
            case SAND:
            case GRAVEL:
            case COBBLESTONE:
            case SANDSTONE:
            case STICK:
            case TORCH:
                return Category.WORTHLESS;
            default:
                if (material.name().contains("WOOD")) {
                    return Category.WOOD;
                } else if (material.name().contains("STONE")) {
                    return Category.STONE;
                } else if (material.name().contains("IRON")) {
                    return Category.IRON;
                } else if (material.name().contains("GOLD")) {
                    return Category.GOLD;
                } else if (material.name().contains("DIAMOND")) {
                    return Category.DIAMOND;
                } else if (material.name().contains("EMERALD")) {
                    return Category.EMERALD;
                } else if (material.name().contains("COAL")) {
                    return Category.COAL;
                } else if (material.name().contains("LEATHER")) {
                    return Category.LEATHER;
                }

                if (Iterators.any(Bukkit.recipeIterator(), new Predicate<Recipe>() {
                    @Override
                    public boolean apply(Recipe recipe) {
                        return recipe.getResult().getType().equals(material);
                    }
                })) {
                    return Category.MANUFACTURED;
                }

                // TODO More
                return Category.OTHER;
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

    public enum Category {
        CHEATING, COOKED_FOOD, MOB_LOOT, BOSS_REWARD, RAW_ORE, MID_HIGH_RANGE, MID_RANGE, WORTHLESS, WOOD, STONE, IRON, GOLD, DIAMOND, EMERALD, COAL, LEATHER, MANUFACTURED, OTHER
    }

    /**
     * Initialized the tribute map with some base data. This prevents fresh data from being out of whack.
     */
    @Deprecated
    public void initializeTributeTracking() {
        for (Material material : Material.values()) {
            // Don't use certain materials
            Material[] unused = {Material.AIR, Material.WATER, Material.STATIONARY_WATER, Material.LAVA, Material.STATIONARY_LAVA, Material.ENDER_PORTAL, Material.BEDROCK, Material.FIRE, Material.MOB_SPAWNER, Material.BURNING_FURNACE, Material.FLOWER_POT, Material.SKULL, Material.DOUBLE_STEP, Material.PORTAL, Material.CAKE_BLOCK, Material.BREWING_STAND, Material.CARROT, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.DOUBLE_PLANT, Material.EXP_BOTTLE, Material.GLOWING_REDSTONE_ORE, Material.LOG_2, Material.SIGN_POST, Material.SNOW, Material.WALL_SIGN};
            if (Arrays.asList(unused).contains(material)) continue;

            // Fill it with random data
            if (getTributes(material) == 0) save(material, RandomUtil.generateIntRange(1, 10));
        }
    }
}
