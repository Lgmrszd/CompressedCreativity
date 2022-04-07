package com.lgmrszd.compressedcreativity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Predicate;

import static me.desht.pneumaticcraft.api.lib.NBTKeys.NBT_UPGRADE_INVENTORY;

public class GoggledChecker {

//    @OnlyIn(Dist.CLIENT)
    public static boolean hasBlockTrackerUpgrade(Player player) {

        // TODO: rewrite using new PNC:R API
        ItemStack helm = player.getItemBySlot(EquipmentSlot.HEAD);
        if (helm.isEmpty() || !helm.hasTag() || !helm.getTag().contains(NBT_UPGRADE_INVENTORY)) return false;
        CompoundTag upgradeInventory = helm.getTag().getCompound(NBT_UPGRADE_INVENTORY);
        ListTag itemList = upgradeInventory.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < itemList.size(); i++) {
            CompoundTag slotEntry = itemList.getCompound(i);
            if (slotEntry.getString("id").equals("pneumaticcraft:block_tracker_upgrade")) return true;
        }
        return false;
    }
}
