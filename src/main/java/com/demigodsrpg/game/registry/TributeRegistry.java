package com.demigodsrpg.game.registry;

import com.censoredsoftware.library.util.RandomUtil;
import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.model.TributeModel;
import com.demigodsrpg.game.util.JsonSection;
import com.google.common.base.Optional;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.Recipe;

import java.util.*;
import java.util.stream.Collectors;

public class TributeRegistry extends AbstractRegistry<TributeModel> {
    private final String FILE_NAME = "tributes.dgdat";

    @Override
    public TributeModel valueFromData(String stringKey, JsonSection data) {
        Optional<ItemType> blockType = DGGame.GAME.getRegistry().getItem(stringKey);
        if (blockType.isPresent()) {
            return new TributeModel(blockType.get(), data);
        }
        return null;
    }

    @Override
    public String getFileName() {
        return FILE_NAME;
    }

    void save(ItemType material, int amount) {
        // Remove the data if it exists already
        remove(material);

        // Make the new one
        register(new TributeModel(material, amount));
    }

    void remove(ItemType material) {
        if (fromId(material.getId()) != null) {
            unregister(fromId(material.getId()));
        }
    }

    @Override
    public TributeModel fromId(String materialName) {
        TributeModel model = super.fromId(materialName);
        if (model == null) {
            Optional<ItemType> blockType = DGGame.GAME.getRegistry().getItem(materialName);
            if (blockType.isPresent()) {
                model = new TributeModel(blockType.get(), 1);
                register(model);
            } else {
                throw new NullPointerException("Not a valid block type: " + materialName);
            }

        }
        return model;
    }

    public List<TributeModel> find(final Category category) {
        return getRegistered().stream().filter(model -> category.equals(model.getCategory())).collect(Collectors.toList());
    }

    /**
     * Returns all saved tribute data with attached current value.
     *
     * @return a Map of all tribute data.
     */
    public Map<ItemType, Integer> getTributeValuesMap() {
        Map<ItemType, Integer> map = new HashMap<>();
        for (TributeModel data : getRegistered()) {
            map.put(data.getMaterial(), (int) getValue(data.getMaterial()));
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
    int getTributes(ItemType material) {
        TributeModel data = fromId(material.getId());
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
    void saveTribute(ItemStack item) {
        TributeModel data = fromId(item.getItem().getId());

        if (data != null) {
            data.setFitness(data.getFitness() + item.getQuantity());
        } else {
            save(item.getItem(), item.getQuantity());
        }
    }

    /**
     * Returns the value for a <code>material</code>.
     */
    double getValue(ItemType material) {
        int lastKnownValue = (int) fromId(material.getId()).getLastKnownValue();
        return lastKnownValue >= 0.0 ? lastKnownValue : 1.0;
    }

    /**
     * Returns the value for the <code>item</code> based on current tribute stats.
     *
     * @param item the item whose value to calculate.
     * @return the value of the item.
     */
    public int getValue(ItemStack item) {
        return (int) getValue(item.getItem()) * item.getQuantity();
    }

    /**
     * Returns the category of the <code>material</code>.
     *
     * @param material the material whose category to check
     * @return the category
     */
    @Deprecated
    public Category getCategory(final ItemType material) {
        if (material.equals(ItemTypes.BEDROCK)) {
            return Category.CHEATING;
        } else if (material.equals(ItemTypes.DRAGON_EGG) || material.equals(ItemTypes.NETHER_STAR)) {
            return Category.BOSS_REWARD;
        }
            /*
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
                } */

        Set<Recipe> recipes = DGGame.GAME.getRegistry().getRecipeRegistry().getRecipes();
        for (Recipe recipe : recipes) {
            if (recipe.getResultTypes().contains(material)) {
                return Category.MANUFACTURED;
            }
        }

        // TODO More
        return Category.OTHER;
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
        for (ItemType material : DGGame.GAME.getRegistry().getItems()) {
            // Don't use certain materials
            ItemType[] unused = {ItemTypes.BEDROCK, ItemTypes.MOB_SPAWNER, ItemTypes.FLOWER_POT, ItemTypes.SKULL,
                    ItemTypes.STONE_SLAB2, ItemTypes.BREWING_STAND, ItemTypes.CARROT, ItemTypes.DOUBLE_PLANT,
                    ItemTypes.EXPERIENCE_BOTTLE, ItemTypes.LOG2, ItemTypes.SIGN, ItemTypes.SNOW};

            if (Arrays.asList(unused).contains(material)) continue;

            // Fill it with random data
            if (getTributes(material) == 0) save(material, RandomUtil.generateIntRange(1, 10));
        }
    }
}
