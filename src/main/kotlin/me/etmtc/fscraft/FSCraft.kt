package me.etmtc.fscraft

import me.etmtc.fscraft.config.ConfigHolder
import me.etmtc.fscraft.items.ItemBlockLauncher
import net.minecraft.client.gui.ScreenManager
import net.minecraft.inventory.container.ContainerType
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraft.util.Util
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.MINECRAFT
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runWhenOn

const val MODID = "fscraft"
val BLOCK_LAUNCHER_ID = ResourceLocation(MODID, "block_launcher")
val BLOCK_LAUNCHER_TRANSLATION_KEY: String = Util.makeTranslationKey("container", BLOCK_LAUNCHER_ID)
lateinit var BLOCK_LAUNCHER_CONTAINER_TYPE: ContainerType<ItemBlockLauncher.Container>
val LOGGER:Logger = LogManager.getLogger(MODID)
@Mod(MODID)
object FSCraft {
    init {
        Events.registerListeners()

        ModLoadingContext.get().apply {
            registerConfig(ModConfig.Type.SERVER, ConfigHolder.SERVER_SPEC)
            registerConfig(ModConfig.Type.CLIENT, ConfigHolder.CLIENT_SPEC)
            registerConfig(ModConfig.Type.COMMON, ConfigHolder.COMMON_SPEC)
        }
    }

}