package me.etmtc.fscraft

import me.etmtc.fscraft.config.ConfigHolder

import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig

@Mod(MODID)
object FSCraft {
    init {
        Events.registerListeners()
        Registries()
        ModLoadingContext.get().apply {
            registerConfig(ModConfig.Type.SERVER, ConfigHolder.SERVER_SPEC)
            registerConfig(ModConfig.Type.CLIENT, ConfigHolder.CLIENT_SPEC)
            registerConfig(ModConfig.Type.COMMON, ConfigHolder.COMMON_SPEC)
        }
    }

}