package me.etmtc.fscraft.config

import net.minecraftforge.common.ForgeConfigSpec

object ConfigHolder {
    val CLIENT: ClientConfig
    val CLIENT_SPEC: ForgeConfigSpec

    val SERVER: ServerConfig
    val SERVER_SPEC: ForgeConfigSpec

    val COMMON:CommonConfig
    val COMMON_SPEC: ForgeConfigSpec

    init {
        val cSpecPair = ForgeConfigSpec.Builder().configure(::ClientConfig)
        CLIENT = cSpecPair.left
        CLIENT_SPEC = cSpecPair.right

        val sSpecPair = ForgeConfigSpec.Builder().configure(::ServerConfig)
        SERVER = sSpecPair.left
        SERVER_SPEC = sSpecPair.right

        val cmSpecPair = ForgeConfigSpec.Builder().configure(::CommonConfig)
        COMMON = cmSpecPair.left
        COMMON_SPEC = cmSpecPair.right
    }
}