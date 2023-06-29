package com.lgmrszd.compressedcreativity.compat.jei;

import com.lgmrszd.compressedcreativity.content.Mesh;
import com.lgmrszd.compressedcreativity.index.CCBlocks;
import com.lgmrszd.compressedcreativity.index.CCItems;
import com.lgmrszd.compressedcreativity.index.CCLang;
import com.lgmrszd.compressedcreativity.index.CCMisc;
import com.lgmrszd.compressedcreativity.items.MeshItem;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.fan.HauntingRecipe;
import com.simibubi.create.content.kinetics.fan.SplashingRecipe;
import com.tterrag.registrate.util.entry.ItemEntry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;

@JeiPlugin
@SuppressWarnings("unused")
public class CompressedCreativityJEI implements IModPlugin {

    public static final ResourceLocation ID = CCMisc.CCRL("");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        RecipeType<?>
                washing = new RecipeType<>(Create.asResource("fan_washing"), SplashingRecipe.class),
                smoking = new RecipeType<>(Create.asResource("fan_smoking"), SmokingRecipe.class),
                blasting = new RecipeType<>(Create.asResource("fan_blasting"), BlastingRecipe.class),
                haunting = new RecipeType<>(Create.asResource("fan_haunting"), HauntingRecipe.class);
        registerAirBlowers(registration, "washing", washing);
        registerAirBlowers(registration, "smoking", smoking);
        registerAirBlowers(registration, "blasting", blasting);
        registerAirBlowers(registration, "haunting", haunting);
        registerMeshRecipeCatalyst(registration, Mesh.MeshType.SPLASHING, washing);
        registerMeshRecipeCatalyst(registration, Mesh.MeshType.DENSE, smoking, blasting);
        registerMeshRecipeCatalyst(registration, Mesh.MeshType.HAUNTED, haunting);
    }

    private void registerAirBlowers(IRecipeCatalystRegistration registration, String name, RecipeType<?> recipeType) {
        registration.addRecipeCatalyst(getAirBlower(name, false), recipeType);
        registration.addRecipeCatalyst(getAirBlower(name, true), recipeType);
    }

    private ItemStack getAirBlower(String name, Boolean isIndustrial) {
        return (isIndustrial ? CCBlocks.INDUSTRIAL_AIR_BLOWER : CCBlocks.AIR_BLOWER).asStack().setHoverName(
                CCLang
                        .translate("recipe." + name + (isIndustrial ? ".industrial_air_blower" : ".air_blower"))
                        .component()
                        .withStyle(style -> style.withItalic(false))
        );
    }

    public void registerMeshRecipeCatalyst(IRecipeCatalystRegistration registration, Mesh.MeshType meshType, RecipeType<?>... recipeTypes) {
        ItemEntry<MeshItem> meshItem = CCItems.MESHES.get(meshType.getName());
        if (meshItem == null) return;
        registration.addRecipeCatalyst(
                meshItem.asStack(),
                recipeTypes
        );
    }
}
