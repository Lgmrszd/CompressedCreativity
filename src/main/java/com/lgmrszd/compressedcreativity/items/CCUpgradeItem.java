package com.lgmrszd.compressedcreativity.items;

import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.upgrade.IUpgradeItem;
import me.desht.pneumaticcraft.api.upgrade.PNCUpgrade;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.function.Supplier;


public class CCUpgradeItem extends Item implements IUpgradeItem {
    private final PNCUpgrade upgrade;
    private final int tier;

    public CCUpgradeItem(Properties properties, PNCUpgrade upgrade, int tier) {
        super(properties);
        this.upgrade = upgrade;
        this.tier = tier;
    }

    @Override
    public PNCUpgrade getUpgradeType() {
        return upgrade;
    }

    @Override
    public int getUpgradeTier() {
        return tier;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level world, List<Component> infoList, TooltipFlag par4) {
        if (Screen.hasShiftDown()) {
            infoList.add(Component.translatable("pneumaticcraft.gui.tooltip.item.upgrade.usedIn").withStyle(ChatFormatting.GOLD));
            PneumaticRegistry.getInstance().getUpgradeRegistry().addUpgradeTooltip(upgrade, infoList);
        } else {
            infoList.add(Component.translatable("pneumaticcraft.gui.tooltip.item.upgrade.shiftMessage").withStyle(ChatFormatting.GOLD));
        }
    }
}
