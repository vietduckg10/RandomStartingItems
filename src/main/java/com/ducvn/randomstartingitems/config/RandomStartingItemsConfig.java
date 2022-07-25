package com.ducvn.randomstartingitems.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class RandomStartingItemsConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> item_pool;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> quantity;
    public static final ForgeConfigSpec.ConfigValue<Integer> num_roll;
    public static final ForgeConfigSpec.ConfigValue<Integer> rng_control;
    public static final ForgeConfigSpec.ConfigValue<Boolean> exclude_rng_control_items;

    static {
        BUILDER.push("Random Starting Items Config");

         item_pool = BUILDER.comment("Items pool.")
                .defineList("Pool", Arrays.asList("minecraft:stick","minecraft:apple","minecraft:dirt"),
                        (p) -> {return p instanceof String;});
        quantity = BUILDER.comment("Quantity match with each item in pool.")
                .defineList("Quantity", Arrays.asList(2,1,2),
                        (p) -> {return p instanceof Integer;});
        num_roll = BUILDER.comment("Number of items player will get")
                .define("Roll", 5);
        rng_control = BUILDER.comment("Items player always get. Example: 2 will give player the first 2 items in pool, default: 0")
                .define("RNG_Control", 0);
        exclude_rng_control_items = BUILDER.comment("Exclude RNG Control items from items pool")
                        .define("Exclude", false);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
