package me.etmtc.fscraft

import me.etmtc.fscraft.impl.block.compressor.CompressorBlock
import me.etmtc.fscraft.impl.block.compressor.CompressorScreen
import me.etmtc.fscraft.impl.item.blocklauncher.BlockLauncherScreen
import net.minecraft.client.gui.ScreenManager
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.RenderTypeLookup
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.event.ModelBakeEvent
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
        ScreenManager.registerFactory(Registries.blockLauncherContainerType, ::BlockLauncherScreen)
        ScreenManager.registerFactory(Registries.compressorContainerType, ::CompressorScreen)
        RenderTypeLookup.setRenderLayer(CompressorBlock, RenderType.getCutoutMipped())
    }

    private fun onModelBaked(event: ModelBakeEvent){

    }
}