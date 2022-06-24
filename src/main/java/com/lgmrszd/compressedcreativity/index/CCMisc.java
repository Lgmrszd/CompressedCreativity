package com.lgmrszd.compressedcreativity.index;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CCMisc {
    public static DamageSource OVERCLOCKED_ENGINE = new DamageSource("oc_engine").setScalesWithDifficulty().setExplosion();

    @SubscribeEvent
    public static void filterExplosionItems(ExplosionEvent.Detonate event) {
        if (event.getExplosion().getDamageSource().msgId.equals("oc_engine")){
            event.getAffectedEntities().removeIf((e) -> e instanceof ItemEntity);
        }
    }
}
