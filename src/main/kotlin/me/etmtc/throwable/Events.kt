package me.etmtc.throwable

import me.etmtc.throwable.impl.block.compressor.CompressorBlock
import me.etmtc.throwable.impl.block.compressor.CompressorScreen
import me.etmtc.throwable.impl.block.compressor.CompressorTER
import me.etmtc.throwable.impl.item.ThrowableBakedModel
import me.etmtc.throwable.impl.item.ThrowableBlockItem
import net.minecraft.client.gui.ScreenManager
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.RenderTypeLookup
import net.minecraft.client.renderer.model.ModelResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runWhenOn


@Suppress("UNUSED_PARAMETER")
internal object Events {
    fun registerListeners(){
        runWhenOn(Dist.CLIENT){
            MOD_BUS.addListener(::clientSetup)
            MOD_BUS.addListener(::onModelBaked)
        }
    }

    @OnlyIn(Dist.CLIENT)
    private fun clientSetup(event: FMLClientSetupEvent){
        ScreenManager.registerFactory(Registries.compressorContainerType, ::CompressorScreen)
        ClientRegistry.bindTileEntityRenderer(Registries.compressorTileEntityType, ::CompressorTER)
        RenderTypeLookup.setRenderLayer(CompressorBlock, RenderType.getCutoutMipped())
    }

    private fun onModelBaked(event: ModelBakeEvent){
        val modelRegistry = event.modelRegistry
        val location = ModelResourceLocation(ThrowableBlockItem.getRegistryName(), "inventory")
        when (val existingModel = modelRegistry[location]) {
            null -> {
                error("Did not find throwable in registry")
            }
            is ThrowableBakedModel -> {
                error("model is already custom baked")
            }
            else -> {
                val bakedModel = ThrowableBakedModel(existingModel)
                event.modelRegistry[location] = bakedModel
            }
        }
    }
}