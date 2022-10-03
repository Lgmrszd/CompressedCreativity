package com.lgmrszd.compressedcreativity.mixin.create;

import com.lgmrszd.compressedcreativity.config.CommonConfig;
import com.simibubi.create.content.curiosities.armor.BackTankUtil;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.pneumatic_armor.ICommonArmorHandler;
import me.desht.pneumaticcraft.api.pneumatic_armor.ICommonArmorRegistry;
import me.desht.pneumaticcraft.common.pneumatic_armor.CommonUpgradeHandlers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BackTankUtil.class)
public class BackTankUtilMixin {
    @Inject(method = "canAbsorbDamage", at = @At(value = "INVOKE_ASSIGN", target = "Lcom/simibubi/create/content/curiosities/armor/BackTankUtil;get(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;"), cancellable = true, remap = false)
    private static void atCanAbsorbDamage(LivingEntity entity, int usesPerTank, CallbackInfoReturnable<Boolean> cir){
        if(canAbsorbDamageChestplate(entity, usesPerTank)) {
            cir.setReturnValue(true);
        }
    }

    private static boolean canAbsorbDamageChestplate(LivingEntity entity, int usesPerTank) {
        if (!(entity instanceof Player player)) return false;
        float pressure = chestplatePressureAvailable(player);
        if (pressure == 0) return false;
        ICommonArmorRegistry reg = PneumaticRegistry.getInstance().getCommonArmorRegistry();
        ICommonArmorHandler handler = reg.getCommonArmorHandler(player);
        handler.addAir(EquipmentSlot.CHEST, - Math.round((float) CommonConfig.BACKTANK_VOLUME.get() / usesPerTank));
        return true;
    }

    private static float chestplatePressureAvailable(Player player) {
        ICommonArmorRegistry reg = PneumaticRegistry.getInstance().getCommonArmorRegistry();
        ICommonArmorHandler handler = reg.getCommonArmorHandler(player);
//        if (!handler.isArmorEnabled()) return 0;
        if (!handler.upgradeUsable(CommonUpgradeHandlers.chargingHandler, true)) return 0;
        float pressure = handler.getArmorPressure(EquipmentSlot.CHEST);
        if (pressure > CommonConfig.CHESTPLATE_MIN_PRESSURE.get()) {
            return pressure;
        }
        return 0;
    }

    @Inject(method = "isBarVisible", at = @At(value = "INVOKE_ASSIGN", target = "Lcom/simibubi/create/content/curiosities/armor/BackTankUtil;get(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;"), cancellable = true, remap = false)
    private static void atIsBarVisible(ItemStack stack, int usesPerTank, CallbackInfoReturnable<Boolean> cir) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(chestplatePressureAvailable(player) > 0) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getBarWidth", at = @At(value = "INVOKE_ASSIGN", target = "Lcom/simibubi/create/content/curiosities/armor/BackTankUtil;get(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;"), cancellable = true, remap = false)
    private static void atGetBarWidth(ItemStack stack, int usesPerTank, CallbackInfoReturnable<Integer> cir) {
        LocalPlayer player = Minecraft.getInstance().player;
        float pressure = chestplatePressureAvailable(player);
        if(pressure > 0) {
            cir.setReturnValue(Math.round(
                    (pressure - CommonConfig.CHESTPLATE_MIN_PRESSURE.get().floatValue()) / (10f - CommonConfig.CHESTPLATE_MIN_PRESSURE.get().floatValue()) * 13F
            ));
        }
    }

    @Inject(method = "getBarColor", at = @At(value = "INVOKE_ASSIGN", target = "Lcom/simibubi/create/content/curiosities/armor/BackTankUtil;get(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;"), cancellable = true, remap = false)
    private static void atGetBarColor(ItemStack stack, int usesPerTank, CallbackInfoReturnable<Integer> cir) {
        LocalPlayer player = Minecraft.getInstance().player;
        ICommonArmorRegistry reg = PneumaticRegistry.getInstance().getCommonArmorRegistry();
        ICommonArmorHandler handler = reg.getCommonArmorHandler(player);
        float pressure = handler.getArmorPressure(EquipmentSlot.CHEST);
        if(chestplatePressureAvailable(player) > 0) {
            float f = (pressure - CommonConfig.CHESTPLATE_MIN_PRESSURE.get().floatValue()) / (10f - CommonConfig.CHESTPLATE_MIN_PRESSURE.get().floatValue());
            int c = (int)(64.0F + 191.0F * f);
            cir.setReturnValue(4194304 | c << 8 | 255);
        }
    }
}
