package me.etmtc.throwable.config

import net.minecraftforge.common.ForgeConfigSpec

class CommonConfig(builder: ForgeConfigSpec.Builder) {
    val emitParticles: ForgeConfigSpec.BooleanValue
    val dropTicks: ForgeConfigSpec.IntValue
    val modTick: ForgeConfigSpec.BooleanValue
    val shouldLookup: ForgeConfigSpec.BooleanValue

    init {
        builder.section("general") {
            emitParticles = configure("emitParticles", true) {
                comment = arrayOf("should emit particles for fallen and placed block")
            }
        }
        builder.comment("this section contains config that affects the server side.", "It only works when it is configured client-side and playing singleplayer", "or configured server-side.")
                .section("server") {
                    modTick = configure("useModTick", true) {
                        comment = arrayOf("should use modified fallingblock tick system instead of vanilla.", "If enabled, concrete powder will not work.")
                    }
                    val dependsModTick = "If 'useModTick' is set to false, this config value has no effect."
                    dropTicks = configure("dropTicks", 40, 0, 2000) {
                        comment = arrayOf("how many ticks(20t = 1s) should a falling block wait before dropping as an item.", dependsModTick)
                    }
                    shouldLookup = configure("shouldLookup", true) {
                        comment = arrayOf("should a falling block look for if the block above is available", "If disabled falling crop blocks will not be able to be planted.", dependsModTick)
                    }
                }
    }
}