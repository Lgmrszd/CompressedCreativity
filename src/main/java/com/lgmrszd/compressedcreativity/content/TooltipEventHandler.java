package com.lgmrszd.compressedcreativity.content;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.simibubi.create.content.curiosities.armor.CopperBacktankItem;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.common.util.PneumaticCraftUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static me.desht.pneumaticcraft.common.util.PneumaticCraftUtils.xlate;

@Mod.EventBusSubscriber(modid = CompressedCreativity.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TooltipEventHandler {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getPlayer() == null) return;

        ItemStack stack = event.getItemStack();

        if (stack.getItem() instanceof CopperBacktankItem) {
            addPressureTooltip(stack, event.getToolTip());
        }
    }

    private static void addPressureTooltip(ItemStack stack, List<Component> textList) {
        stack.getCapability(PNCCapabilities.AIR_HANDLER_ITEM_CAPABILITY).ifPresent(airHandler -> {
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
