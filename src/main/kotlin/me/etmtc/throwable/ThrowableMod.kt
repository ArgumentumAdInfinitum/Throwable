package me.etmtc.throwable

import me.etmtc.throwable.config.ConfigHolder

import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig

@Mod(MODID)
object ThrowableMod {
    init {
        Events.registerListeners()
        Registries()
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHolder.COMMON_SPEC)
    }
}