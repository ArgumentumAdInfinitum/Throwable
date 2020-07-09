package me.etmtc.fscraft

import me.etmtc.fscraft.items.ItemBlockLauncher
import net.minecraft.client.gui.ScreenManager
import net.minecraft.inventory.container.ContainerType
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraft.util.Util
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runWhenOn

const val MODID = "fscraft"
val BLOCK_LAUNCHER_ID = ResourceLocation(MODID, "block_launcher")
val BLOCK_LAUNCHER_TRANSLATION_KEY: String = Util.makeTranslationKey("container", BLOCK_LAUNCHER_ID)
lateinit var BLOCK_LAUNCHER_CONTAINER_TYPE: ContainerType<ItemBlockLauncher.Container>
@Mod(MODID)
object FSCraft {
    val LOGGER: Logger = LogManager.getLogger()
    init {

        LogManager.getRootLogger().level
        MOD_BUS.addGenericListener(::registerItems)
        MOD_BUS.addGenericListener(::registerContainers)
        runWhenOn(Dist.CLIENT){
            MOD_BUS.addListener(::clientSetup)
        }
    }
    private fun registerContainers(event:RegistryEvent.Register<ContainerType<*>>){
        LOGGER.info(event.registry.registryName)
        if(event.registry.registryName.toString() == "minecraft:menu"){
            BLOCK_LAUNCHER_CONTAINER_TYPE = ContainerType(ItemBlockLauncher.Container.Factory)
            BLOCK_LAUNCHER_CONTAINER_TYPE.registryName = BLOCK_LAUNCHER_ID
            event.registry.register(BLOCK_LAUNCHER_CONTAINER_TYPE)
        }
    }
    @OnlyIn(Dist.CLIENT)
    private fun clientSetup(event:FMLClientSetupEvent){
        ScreenManager.registerFactory(BLOCK_LAUNCHER_CONTAINER_TYPE, ItemBlockLauncher::Screen)
    }
    private fun registerItems(event:RegistryEvent.Register<Item>){
        LOGGER.info("Registering Items")
        event.registry.registerAll(ItemBlockLauncher)
    }
}