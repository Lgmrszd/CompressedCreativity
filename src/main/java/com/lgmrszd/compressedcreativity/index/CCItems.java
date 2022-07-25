package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.ModGroup;
import com.lgmrszd.compressedcreativity.items.CCUpgradeItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;

public class CCItems {

    private static final CreateRegistrate REGISTRATE = CompressedCreativity.registrate()
            .creativeModeTab(() -> ModGroup.MAIN);

    public static final ItemEntry<Item> MECHANICAL_VISOR_UPGRADE = REGISTRATE.item(
            "mechanical_visor_upgrade", (properties) -> (Item) new CCUpgradeItem(properties, CCUpgrades.MECHANICAL_VISOR, 1)
    )
            .model(AssetLookup.existingItemModel())
            .register();

    public static final ItemEntry<Item> BRASS_GILDED_LAPIS_LAZULI = REGISTRATE.item(
            "brass_gilded_lapis_lazuli", Item::new
    ).register();

    public static final ItemEntry<Item> BRASS_COATED_UPGRADE_MATRIX = REGISTRATE.item(
            "brass_coated_upgrade_matrix", Item::new
    ).register();

    public static void register(IEventBus eventBus) {
    }
}
