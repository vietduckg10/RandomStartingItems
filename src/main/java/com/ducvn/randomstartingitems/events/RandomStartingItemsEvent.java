package com.ducvn.randomstartingitems.events;

import com.ducvn.randomstartingitems.RandomStartingItemsMod;
import com.ducvn.randomstartingitems.config.RandomStartingItemsConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Random;
import java.util.Set;

@Mod.EventBusSubscriber(modid = RandomStartingItemsMod.MODID)
public class RandomStartingItemsEvent {
    @SubscribeEvent
    public static void GiveStartingItems(EntityJoinWorldEvent event) {
        Entity playerEntity = event.getEntity();
        Level world = event.getWorld();
        if (!world.isClientSide) {
            if (playerEntity instanceof Player) {
                Player player = (Player) playerEntity;
                if (isFirstJoin(player, world)) {
                    List<? extends String> itemList = RandomStartingItemsConfig.item_pool.get();
                    List<? extends Integer> quantityList = RandomStartingItemsConfig.quantity.get();
                    int NumOfItem = RandomStartingItemsConfig.num_roll.get();
                    int RNGControl = RandomStartingItemsConfig.rng_control.get();
                    boolean excludeItem = RandomStartingItemsConfig.exclude_rng_control_items.get();
                    if (RNGControl > 0 && !excludeItem) {
                        for (int i = 0; i < RNGControl; i++) {
                            givePlayerItem(player, itemList.get(i), quantityList.get(i));
                        }
                        NumOfItem = NumOfItem - RNGControl;
                    }
                    if (NumOfItem > 0) {
                        int adjustValue = 0;
                        if (excludeItem) {
                            adjustValue = RNGControl;
                        }
                        Random roll = new Random();
                        int rollResult;
                        for (int i = 0; i < NumOfItem; i++) {
                            rollResult = roll.nextInt(itemList.size() - adjustValue) + adjustValue;
                            givePlayerItem(player, itemList.get(rollResult), quantityList.get(rollResult));
                        }
                    }
                }
            }
        }
    }

    public static boolean isFirstJoin(Player playerEntity, Level world){
        String modTag = RandomStartingItemsMod.MODID + "firstjointag";
        Set<String> tags = playerEntity.getTags();
        if (tags.contains(modTag)){
            return false;
        }
        playerEntity.addTag(modTag);
        ServerLevel ServerWorld = (ServerLevel) world;
        BlockPos wspos = ServerWorld.getSharedSpawnPos();
        BlockPos ppos = playerEntity.blockPosition();
        BlockPos cpos = new BlockPos(ppos.getX(), wspos.getY(), ppos.getZ());

        return cpos.closerThan(wspos, 50);
    }

    public static void givePlayerItem(Player player, String item, int quantity){
        if (ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(item)).getDefaultInstance().isStackable()){
            ItemStack stack =
                    new ItemStack(
                            ForgeRegistries
                                    .ITEMS
                                    .getValue(ResourceLocation.tryParse(item)), quantity);
            player.getInventory().add(stack);
        }
        else {
            for (int i = 0; i < quantity; i++){
                ItemStack stack =
                        new ItemStack(
                                ForgeRegistries
                                        .ITEMS
                                        .getValue(ResourceLocation.tryParse(item)), 1);
                player.getInventory().add(stack);
            }
        }
    }
}
