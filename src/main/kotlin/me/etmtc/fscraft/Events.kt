package me.etmtc.fscraft

import me.etmtc.fscraft.impl.block.squisher.SquisherBlock
import me.etmtc.fscraft.impl.block.squisher.SquisherItem
import me.etmtc.fscraft.impl.item.blocklauncher.BlockLauncherScreen
import me.etmtc.fscraft.impl.item.blocklauncher.ItemBlockLauncher
import net.minecraft.block.Block
import net.minecraft.client.gui.ScreenManager
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.RenderTypeLookup
import net.minecraft.inventory.container.ContainerType
import net.minecraft.item.Item
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runWhenOn

@Suppress("UNUSED_PARAMETER")
internal object Events {
    fun registerListeners(){
        MOD_BUS.addGenericListener(::registerItems)
        MOD_BUS.addGenericListener(::registerContainers)
        MOD_BUS.addGenericListener(::registerBlocks)
        runWhenOn(Dist.CLIENT){
            MOD_BUS.addListener(::clientSetup)
            MOD_BUS.addListener(::onModelBaked)
        }
    }
    private fun registerContainers(event: RegistryEvent.Register<ContainerType<*>>){
        LOGGER.info(event.registry.registryName)
        if(event.registry.registryName.toString() == "minecraft:menu"){
            event.registry.register(BLOCK_LAUNCHER_CONTAINER_TYPE)
        }
    }
    @OnlyIn(Dist.CLIENT)
    private fun clientSetup(event: FMLClientSetupEvent){
        ScreenManager.registerFactory(BLOCK_LAUNCHER_CONTAINER_TYPE, ::BlockLauncherScreen)
        RenderTypeLookup.setRenderLayer(SquisherBlock, RenderType.getCutoutMipped())
    }

    private fun registerItems(event: RegistryEvent.Register<Item>){
        LOGGER.info("Registering Items")
        event.registry.registers {
            ItemBlockLauncher - "block_launcher"
            SquisherItem - "squisher"
        }
    }
    private fun registerBlocks(event: RegistryEvent.Register<Block>){
        LOGGER.info("Registering Blocks")
        event.registry.registers {
            SquisherBlock - "squisher_block"
        }
    }
    private fun onModelBaked(event: ModelBakeEvent){

    }
}