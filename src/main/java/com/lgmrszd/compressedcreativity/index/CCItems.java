package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.ModGroup;
import com.lgmrszd.compressedcreativity.content.Mesh;
import com.lgmrszd.compressedcreativity.items.CCUpgradeItem;
import com.lgmrszd.compressedcreativity.items.MeshItem;
import com.simibubi.create.content.contraptions.itemAssembly.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.HashMap;
import java.util.Map;

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

    public static final ItemEntry<Item> ENGINE_ROTOR = REGISTRATE.item(
            "engine_rotor", Item::new
    ).register();

//    public static final ItemEntry<MeshItem> WATER_MESH = REGISTRATE.item(
//            "mesh_water", ((p) -> new MeshItem(p, Mesh.MeshType.WATER))
//    ).register();

    public static final ItemEntry<Item> MESH_EMPTY = REGISTRATE.item(
            "mesh_empty", Item::new
    ).lang("Mesh Frame").register();

    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_SPLASHING_MESH = REGISTRATE.item(
            "incomplete_mesh_splashing", SequencedAssemblyItem::new
    ).lang("Incomplete Water Soaked Mesh").register();

    public static final Map<String, ItemEntry<MeshItem>> MESHES = new HashMap<>();

    static {
        for (Mesh.MeshType meshType : Mesh.MeshType.values()) {
            MESHES.put(
                    meshType.getName(),
                    REGISTRATE.item(
                            "mesh_" + meshType.getName(),
                            ((p) -> new MeshItem(p, meshType))
                    )
                            .lang(meshType.getDisplayName())
                            .register()
            );
        }
    }

    public static void register(IEventBus eventBus) {
    }
}
