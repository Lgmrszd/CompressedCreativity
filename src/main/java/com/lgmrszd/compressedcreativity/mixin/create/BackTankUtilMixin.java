package com.lgmrszd.compressedcreativity.mixin.create;

import com.simibubi.create.content.equipment.armor.BacktankUtil;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.pneumatic_armor.ICommonArmorHandler;
import me.desht.pneumaticcraft.api.pneumatic_armor.ICommonArmorRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.lgmrszd.compressedcreativity.config.CommonConfig.CHESTPLATE_COMPAT;
import static com.lgmrszd.compressedcreativity.index.CCMisc.chestplatePressureAvailable;

@Mixin(BacktankUtil.class)
public class BackTankUtilMixin {
    @Inject(method = "canAbsorbDamage", at = @At(value = "INVOKE_ASSIGN", target = "Lcom/simibubi/create/content/equipment/armor/BacktankUtil;getAllWithAir(Lnet/minecraft/world/entity/LivingEntity;)Ljava/util/List;"), cancellable = true, remap = false)
    private static void atCanAbsorbDamage(LivingEntity entity, int usesPerTank, CallbackInfoReturnable<Boolean> cir) {
        if (!CHESTPLATE_COMPAT.get()) return;
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
        // Assumed volume (1800) * drawback (10)
        handler.addAir(EquipmentSlot.CHEST, - Math.round((float) 18000 / usesPerTank));
        return true;
    }

}
