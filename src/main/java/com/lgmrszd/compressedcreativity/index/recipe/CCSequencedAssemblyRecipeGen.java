package com.lgmrszd.compressedcreativity.index.recipe;

import com.lgmrszd.compressedcreativity.content.Mesh;
import com.lgmrszd.compressedcreativity.index.CCItems;
import com.lgmrszd.compressedcreativity.index.CCMisc;
import com.simibubi.create.content.contraptions.fluids.actors.FillingRecipe;
import com.simibubi.create.content.contraptions.itemAssembly.SequencedAssemblyRecipeBuilder;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.material.Fluids;

import java.util.function.UnaryOperator;

public class CCSequencedAssemblyRecipeGen extends CreateRecipeProvider {

    public CCSequencedAssemblyRecipeGen(DataGenerator generator) {
        super(generator);
    }

    GeneratedRecipe
    MESH_SPLASHING = create("mesh_splashing",b ->
            b.require(CCItems.MESHES.get(Mesh.MeshType.WOVEN.getName()).get())
                    .transitionTo(CCItems.INCOMPLETE_SPLASHING_MESH.get())
                    .addOutput(CCItems.MESHES.get(Mesh.MeshType.SPLASHING.getName()).get(), 1)
                    .loops(16)
                    .addStep(FillingRecipe::new, f -> f.require(Fluids.WATER, 1000)));

    protected GeneratedRecipe create(String name, UnaryOperator<SequencedAssemblyRecipeBuilder> transform) {
        GeneratedRecipe generatedRecipe =
                c -> transform.apply(new SequencedAssemblyRecipeBuilder(CCMisc.CCRL(name)))
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    @Override
    public String getName() {
        return "Compressed Creativity Sequenced Assembly Recipes";
    }
}
