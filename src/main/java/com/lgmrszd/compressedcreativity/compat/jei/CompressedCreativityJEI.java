package com.lgmrszd.compressedcreativity.compat.jei;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.content.Mesh;
import com.lgmrszd.compressedcreativity.index.CCBlocks;
import com.lgmrszd.compressedcreativity.index.CCItems;
import com.lgmrszd.compressedcreativity.items.MeshItem;
import com.simibubi.create.Create;
import com.tterrag.registrate.util.entry.ItemEntry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
@SuppressWarnings("unused")
public class CompressedCreativityJEI implements IModPlugin {

    private static final ResourceLocation PLUGIN_UID = ResourceLocation.fromNamespaceAndPath(CompressedCreativity.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        RecipeType<?> washing = RecipeType.createRecipeHolderType(Create.asResource("fan_washing"));
        RecipeType<?> smoking = RecipeType.createRecipeHolderType(Create.asResource("fan_smoking"));
        RecipeType<?> blasting = RecipeType.createRecipeHolderType(Create.asResource("fan_blasting"));
        RecipeType<?> haunting = RecipeType.createRecipeHolderType(Create.asResource("fan_haunting"));

        registerAirBlowers(registration, washing);
        registerAirBlowers(registration, smoking);
        registerAirBlowers(registration, blasting);
        registerAirBlowers(registration, haunting);

        registerMeshCatalyst(registration, Mesh.MeshType.SPLASHING, washing);
        registerMeshCatalyst(registration, Mesh.MeshType.DENSE, smoking, blasting);
        registerMeshCatalyst(registration, Mesh.MeshType.HAUNTED, haunting);
    }

    private void registerAirBlowers(IRecipeCatalystRegistration registration, RecipeType<?> recipeType) {
        registration.addRecipeCatalyst(CCBlocks.AIR_BLOWER.asStack(), recipeType);
        registration.addRecipeCatalyst(CCBlocks.INDUSTRIAL_AIR_BLOWER.asStack(), recipeType);
    }

    private void registerMeshCatalyst(IRecipeCatalystRegistration registration, Mesh.MeshType meshType, RecipeType<?>... recipeTypes) {
        ItemEntry<MeshItem> meshItem = CCItems.MESHES.get(meshType.getName());
        if (meshItem == null) return;
        registration.addRecipeCatalyst(meshItem.asStack(), recipeTypes);
    }
}
