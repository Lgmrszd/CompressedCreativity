package com.lgmrszd.compressedcreativity.content;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.simibubi.create.content.equipment.armor.BacktankItem;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.common.util.PneumaticCraftUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.List;

import static me.desht.pneumaticcraft.common.util.PneumaticCraftUtils.xlate;

@EventBusSubscriber(modid = CompressedCreativity.MOD_ID, value = Dist.CLIENT)
public class TooltipEventHandler {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getEntity() == null) return;

        ItemStack stack = event.getItemStack();

        if (stack.getItem() instanceof BacktankItem) {
            addPressureTooltip(stack, event.getToolTip());
        }
    }

    private static void addPressureTooltip(ItemStack stack, List<Component> textList) {
        PNCCapabilities.getAirHandler(stack).ifPresent(airHandler -> {
            float f = airHandler.getPressure() / airHandler.maxPressure();
            ChatFormatting color;
            if (f < 0.1f) {
                color = ChatFormatting.RED;
            } else if (f < 0.5f) {
                color = ChatFormatting.GOLD;
            } else {
                color = ChatFormatting.YELLOW;
            }
            textList.add(xlate("pneumaticcraft.gui.tooltip.pressure", PneumaticCraftUtils.roundNumberTo(airHandler.getPressure(), 1)).withStyle(color));
        });
    }
}
