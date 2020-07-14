package me.etmtc.fscraft.impl.jei

import me.etmtc.fscraft.impl.block.compressor.CompressorItem
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.util.ResourceLocation

class FSCompressorRecipeCategory(private val helper: IGuiHelper) : IRecipeCategory<FSCompressorRecipe>{
    override fun getUid(): ResourceLocation {
        TODO("Not yet implemented")
    }

    override fun getRecipeClass() = FSCompressorRecipe::class.java

    override fun getTitle(): String {
        TODO("Not yet implemented")
    }

    override fun getBackground() = TODO("Not yet implemented")

    override fun getIcon(): IDrawable = helper.createDrawableIngredient(CompressorItem)

    override fun setIngredients(recipe: FSCompressorRecipe, ingredients: IIngredients) {
        ingredients.setInputs(VanillaTypes.ITEM, recipe.input)
        ingredients.setOutputs(VanillaTypes.ITEM, recipe.output)
    }

    override fun setRecipe(recipeLayout: IRecipeLayout, recipe: FSCompressorRecipe, ingredients: IIngredients) {
        recipeLayout.itemStacks.also {
            it.set(0, recipe.input)
            it.set(1, recipe.output)
        }
    }

}