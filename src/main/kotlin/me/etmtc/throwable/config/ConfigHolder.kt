package me.etmtc.throwable.config

import net.minecraftforge.common.ForgeConfigSpec

object ConfigHolder {
    val COMMON:CommonConfig
    val COMMON_SPEC: ForgeConfigSpec

    init {
        val cmSpecPair = ForgeConfigSpec.Builder().configure(::CommonConfig)
        COMMON = cmSpecPair.left
        COMMON_SPEC = cmSpecPair.right
    }
}