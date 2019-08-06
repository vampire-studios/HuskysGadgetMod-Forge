package io.github.vampirestudios.hgm.api.app;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class CurrencySets {

    /*static ArrayList<Item> foodArray = new ArrayList<Item>();
    private static ItemStack[] moddedFood = new ItemStack[foodArray.size()];

    static {
        for (ResourceLocation rl : Registry.ITEM.keySet()) {
            Item i = Registry.ITEM.getOrDefault(rl);
            if (i instanceof FoodI) {
                foodArray.add(i);
            }
        }

        moddedFood = foodArray.toArray(moddedFood);
    }*/

    private ItemStack[] emeralds = new ItemStack[]{
            new ItemStack(Items.EMERALD),
            new ItemStack(Blocks.EMERALD_BLOCK)
    };
    private ItemStack defaultCurrency = getEmeralds()[0];
    private ItemStack[] diamond = new ItemStack[]{
            new ItemStack(Items.DIAMOND),
            new ItemStack(Blocks.DIAMOND_BLOCK)
    };

    private ItemStack[] gold = new ItemStack[]{
            new ItemStack(Items.GOLD_INGOT),
            new ItemStack(Blocks.GOLD_BLOCK)
    };

    private ItemStack[] iron = new ItemStack[]{
            new ItemStack(Items.IRON_INGOT),
            new ItemStack(Blocks.IRON_BLOCK)
    };

    private ItemStack[] food = new ItemStack[]{
            new ItemStack(Items.APPLE),
            new ItemStack(Items.COOKIE),
            new ItemStack(Items.POTATO),
            new ItemStack(Items.MELON),
            new ItemStack(Items.TROPICAL_FISH),
            new ItemStack(Items.COD),
            new ItemStack(Items.SALMON),
            new ItemStack(Items.PUFFERFISH),
            new ItemStack(Items.SWEET_BERRIES),
            new ItemStack(Items.MUSHROOM_STEW),
            new ItemStack(Items.BREAD),
            new ItemStack(Items.PORKCHOP),
            new ItemStack(Items.COOKED_BEEF),
            new ItemStack(Items.COOKED_SALMON),
            new ItemStack(Items.COOKED_COD),
            new ItemStack(Items.COOKED_CHICKEN),
            new ItemStack(Items.COOKED_PORKCHOP),
            new ItemStack(Items.COOKED_MUTTON),
            new ItemStack(Items.COOKED_RABBIT),
            new ItemStack(Items.GOLDEN_APPLE),
            new ItemStack(Items.CAKE),
            new ItemStack(Items.BEEF),
            new ItemStack(Items.BEETROOT),
            new ItemStack(Items.RABBIT),
            new ItemStack(Items.CHICKEN),
            new ItemStack(Items.RABBIT_STEW),
            new ItemStack(Items.BAKED_POTATO)
    };

    /*public ItemStack[] getModdedFood() {
        return moddedFood;
    }*/

    public ItemStack[] getDiamond() {
        return diamond;
    }

    public ItemStack[] getEmeralds() {
        return emeralds;
    }

    public ItemStack[] getFood() {
        return food;
    }

    public ItemStack[] getGold() {
        return gold;
    }

    public ItemStack[] getIron() {
        return iron;
    }

    public ItemStack getDefaultCurrency() {
        return defaultCurrency;
    }

    public ItemStack setDefaultCurrency(ItemStack defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
        return defaultCurrency;
    }
}