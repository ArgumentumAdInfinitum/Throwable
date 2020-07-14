package me.etmtc.fscraft.impl.jei

import me.etmtc.fscraft.impl.block.compressor.CompressorItem
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

@JeiPlugin
class FSJeiPlugin : IModPlugin {
    override fun getPluginUid(): ResourceLocation = ResourceLocation("fscraft", "fscraft_jei_plugin")
    override fun registerRecipes(registration: IRecipeRegistration) {
        registration.addIngredientInfo(ItemStack(CompressorItem), VanillaTypes.ITEM, "yes")
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {

    }
}