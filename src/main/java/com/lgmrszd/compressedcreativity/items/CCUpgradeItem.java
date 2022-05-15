package com.lgmrszd.compressedcreativity.items;

import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.item.IUpgradeItem;
import me.desht.pneumaticcraft.api.item.PNCUpgrade;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.function.Supplier;


public class CCUpgradeItem extends Item implements IUpgradeItem {
    private final Supplier<PNCUpgrade> upgrade;
    private final int tier;

    public CCUpgradeItem(Properties properties, Supplier<PNCUpgrade> upgrade, int tier) {
        super(properties);
        this.upgrade = upgrade;
        this.tier = tier;
    }

    @Override
    public PNCUpgrade getUpgradeType() {
        return upgrade.get();
    }

    @Override
    public int getUpgradeTier() {
        return tier;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level world, List<Component> infoList, TooltipFlag par4) {
        if (Screen.hasShiftDown()) {
            infoList.add(new TranslatableComponent("pneumaticcraft.gui.tooltip.item.upgrade.usedIn").withStyle(ChatFormatting.GOLD));
            PneumaticRegistry.getInstance().getUpgradeRegistry().addUpgradeTooltip(upgrade.get(), infoList);
        } else {
            infoList.add(new TranslatableComponent("pneumaticcraft.gui.tooltip.item.upgrade.shiftMessage").withStyle(ChatFormatting.GOLD));
        }
    }
}
