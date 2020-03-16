package io.github.lukas2005.supernaturalcreatures.data;

import io.github.lukas2005.supernaturalcreatures.Reference;
import io.github.lukas2005.supernaturalcreatures.blocks.ModBlocks;
import io.github.lukas2005.supernaturalcreatures.items.ModItems;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

import static io.github.lukas2005.supernaturalcreatures.Reference.MOD_ID;

public class RecipesGenerator extends RecipeProvider {
    public RecipesGenerator(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        CookingRecipeBuilder.smeltingRecipe(
                Ingredient.fromItems(ModBlocks.SILVER_ORE.get().asItem()), ModItems.SILVER_INGOT.get(), 1.0f, 200)
                .addCriterion("has_silver_ore", InventoryChangeTrigger.Instance.forItems(ModBlocks.SILVER_ORE.get()))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.SILVER_BLOCK.get())
                .addIngredient(ModItems.SILVER_INGOT.get(), 9)
                .addCriterion("has_silver_ingot", InventoryChangeTrigger.Instance.forItems(ModItems.SILVER_INGOT.get()))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.SILVER_INGOT.get(), 9)
                .addIngredient(ModBlocks.SILVER_BLOCK.get())
                .addCriterion("has_silver_ingot", InventoryChangeTrigger.Instance.forItems(ModItems.SILVER_INGOT.get()))
                .build(consumer, id("silver_ingot_from_block"));

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.SILVER_NUGGET.get(), 9)
                .addIngredient(ModItems.SILVER_INGOT.get())
                .addCriterion("has_silver_ingot", InventoryChangeTrigger.Instance.forItems(ModItems.SILVER_INGOT.get()))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.SILVER_INGOT.get())
                .addIngredient(ModItems.SILVER_NUGGET.get(), 9)
                .addCriterion("has_silver_ingot", InventoryChangeTrigger.Instance.forItems(ModItems.SILVER_INGOT.get()))
                .build(consumer, id("silver_ingot_from_nugget"));
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation(MOD_ID, name);
    }
}
