package com.lgmrszd.compressedcreativity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static me.desht.pneumaticcraft.api.lib.NBTKeys.NBT_UPGRADE_INVENTORY;

public class GoggledChecker {

    @OnlyIn(Dist.CLIENT)
    public static boolean hasBlockTrackerUpgrade() {

        // TODO: rewrite using new PNC:R API
//        Minecraft mc = Minecraft.getInstance();
//        ItemStack helm = mc.player.getItemBySlot(EquipmentSlot.HEAD);
//        if (helm.isEmpty() || !helm.hasTag() || !helm.getTag().contains(NBT_UPGRADE_INVENTORY)) return false;
//        CompoundTag upgradeInventory = helm.getTag().getCompound(NBT_UPGRADE_INVENTORY);
//        ListTag itemList = upgradeInventory.getList("Items", Constants.NBT.TAG_COMPOUND);
//        for (int i = 0; i < itemList.size(); i++) {
//            CompoundTag slotEntry = itemList.getCompound(i);
//            if (slotEntry.getString("id").equals("pneumaticcraft:block_tracker_upgrade")) return true;
//        }
        return false;
    }
}
