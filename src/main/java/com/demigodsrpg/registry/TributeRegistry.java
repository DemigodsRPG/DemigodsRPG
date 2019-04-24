package com.demigodsrpg.registry;

import com.demigodsrpg.model.TributeModel;
import com.demigodsrpg.util.datasection.DataSection;
import com.demigodsrpg.util.datasection.Registry;
import com.demigodsrpg.util.misc.RandomUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.*;
import java.util.stream.Collectors;

public interface TributeRegistry extends Registry<TributeModel> {
    String NAME = "tributes";

    @Override
    default TributeModel fromDataSection(String stringKey, DataSection data) {
        return new TributeModel(Material.getMaterial(stringKey), data);
    }

    default void save(Material material, int amount) {
        // Remove the data if it exists already
        remove(material);

        // Make the new one
        register(new TributeModel(material, amount));
    }

    default void remove(Material material) {
        remove(material.name());
    }

    default TributeModel fromKeyOrNew(String materialName) {
        if (!getRegisteredData().containsKey(materialName)) {
            loadFromDb(materialName);
        }
        TributeModel model = getRegisteredData().getOrDefault(materialName, null);
        if (model == null) {
            model = new TributeModel(Material.valueOf(materialName), 1);
            register(model);
        }
        return model;
    }

    default Collection<TributeModel> find(final Category category) {
        return getRegisteredData().values().stream().filter(model -> category.equals(model.getCategory()))
                .collect(Collectors.toList());
    }

    /**
     * Returns all saved tribute data with attached current value.
     *
     * @return a Map of all tribute data.
     */
    default Map<Material, Integer> getTributeValuesMap() {
        Map<Material, Integer> map = new HashMap<>();
        for (TributeModel data : getRegisteredData().values()) {
            map.put(data.getMaterial(), (int) getValue(data.getMaterial()));
        }
        return map;
    }

    /**
     * Returns the total number of tributes for the entire server.
     *
     * @return the total server tributes.
     */
    default int getTotalTributes() {
        int total = 1;
        for (TributeModel data : getRegisteredData().values()) {
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
    default double getTributes(Material material) {
        TributeModel data = fromKeyOrNew(material.name());
        if (data != null) { return data.getFitness(); } else return 1;
    }

    /**
     * Returns the number of tributes for the <code>category</code>.
     *
     * @param category the category to check.
     * @return the total number of tributes.
     */
    @Deprecated
    default int getTributesForCategory(Category category) {
        int total = 1;
        for (TributeModel data : find(category)) { total += data.getFitness(); }
        return total;
    }

    /**
     * Saves the <code>item</code> amount into the server tribute stats.
     *
     * @param item the item whose amount to save.
     */
    default void saveTribute(ItemStack item) {
        TributeModel data = fromKeyOrNew(item.getType().name());

        if (data != null) {
            data.setFitness(data.getFitness() + item.getAmount());
        } else {
            save(item.getType(), item.getAmount());
        }
    }

    /**
     * Returns the value for a <code>material</code>.
     */
    default double getValue(Material material) {
        int lastKnownValue = (int) fromKeyOrNew(material.name()).getLastKnownValue();
        return lastKnownValue >= 0.0 ? lastKnownValue : 1.0;
    }

    /**
     * Returns the value for the <code>item</code> based on current tribute stats.
     *
     * @param item the item whose value to calculate.
     * @return the value of the item.
     */
    default int getValue(ItemStack item) {
        return (int) getValue(item.getType()) * item.getAmount();
    }

    /**
     * Returns the category of the <code>material</code>.
     *
     * @param material the material whose category to check
     * @return the category
     */
    @Deprecated
    default Category getCategory(final Material material) {
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
            case COOKED_COD:
            case COOKED_PORKCHOP:
            case BAKED_POTATO:
            case MUSHROOM_STEW:
            case CAKE:
            case PUMPKIN_PIE:
                return Category.COOKED_FOOD;
            case APPLE:
            case ROTTEN_FLESH:
            case BEEF:
            case CHICKEN:
            case COD:
            case LEATHER:
                //case LEGACY_WOOL:
            case INK_SAC:
            case EGG:
            case ROSE_BUSH:
            case SPIDER_EYE:
            case STRING:
            case BONE:
            case PORKCHOP:
            case FEATHER:
            case POTATO:
            case POISONOUS_POTATO:
            case CARROT:
            case PUMPKIN:
            case PUMPKIN_SEEDS:
            case MELON:
            case MELON_SEEDS:
                //case LEGACY_SEEDS:
            case WHEAT:
            case HAY_BLOCK:
                //case LEGACY_SULPHUR:
                return Category.MOB_LOOT;
            //case LEGACY_LOG:
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

                Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
                while (recipeIterator.hasNext()) {
                    Recipe recipe = recipeIterator.next();
                    if (recipe.getResult().getType().equals(material)) {
                        return Category.MANUFACTURED;
                    }
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
    default int processTribute(ItemStack item) {
        // Grab the value before
        int value = getValue(item);

        // Save the tribute to be used in calculations later
        saveTribute(item);

        // Return the value
        return value;
    }

    public enum Category {
        CHEATING, COOKED_FOOD, MOB_LOOT, BOSS_REWARD, RAW_ORE, MID_HIGH_RANGE, MID_RANGE, WORTHLESS, WOOD, STONE, IRON,
        GOLD, DIAMOND, EMERALD, COAL, LEATHER, MANUFACTURED, OTHER
    }

    /**
     * Initialized the tribute map with some base data. This prevents fresh data from being out of whack.
     */
    @Deprecated
    default void initializeTributeTracking() {
        for (Material material : Material.values()) {
            // Don't use certain materials
            Material[] unused = {/* TODO Fix this
                    Material.AIR, Material.WATER, Material.LEGACY_STATIONARY_WATER, Material.LAVA,
                    Material.LEGACY_STATIONARY_LAVA,
                    Material.END_PORTAL, Material.BEDROCK, Material.FIRE, Material.SPAWNER,
                    Material.LEGACY_BURNING_FURNACE, Material.FLOWER_POT, Material.SKELETON_SKULL,
                    Material.LEGACY_DOUBLE_STEP,
                    Material.LEGACY_PORTAL, Material.CAKE, Material.BREWING_STAND, Material.CARROT,
                    Material.LEGACY_DIODE_BLOCK_OFF, Material.LEGACY_DIODE_BLOCK_ON, Material.LEGACY_DOUBLE_PLANT,
                    Material.EXPERIENCE_BOTTLE,
                    Material.LEGACY_GLOWING_REDSTONE_ORE, Material.LEGACY_LOG_2, Material.LEGACY_SIGN_POST,
                    Material.SNOW,
                    Material.LEGACY_WALL_SIGN*/
            };
            if (Arrays.asList(unused).contains(material)) continue;

            // Fill it with random data
            if (getTributes(material) == 0) save(material, RandomUtil.generateIntRange(1, 10));
        }
    }
}
