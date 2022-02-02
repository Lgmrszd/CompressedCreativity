package com.lgmrszd.compressedcreativity;

import me.desht.pneumaticcraft.api.PneumaticRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static me.desht.pneumaticcraft.api.lib.NBTKeys.NBT_UPGRADE_INVENTORY;

public class GoggledChecker {

    @OnlyIn(Dist.CLIENT)
    public static boolean hasBlockTrackerUpgrade() {
        Minecraft mc = Minecraft.getInstance();
        ItemStack helm = mc.player.getItemBySlot(EquipmentSlotType.HEAD);
        if (helm.isEmpty() || !helm.hasTag() || !helm.getTag().contains(NBT_UPGRADE_INVENTORY)) return false;
        CompoundNBT upgradeInventory = helm.getTag().getCompound(NBT_UPGRADE_INVENTORY);
        ListNBT itemList = upgradeInventory.getList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < itemList.size(); i++) {
            CompoundNBT slotEntry = itemList.getCompound(i);
            if (slotEntry.getString("id").equals("pneumaticcraft:block_tracker_upgrade")) return true;
        }
        return false;
    }
}
