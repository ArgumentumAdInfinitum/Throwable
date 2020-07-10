package me.etmtc.fscraft.config

import net.minecraftforge.common.ForgeConfigSpec

class CommonConfig(builder: ForgeConfigSpec.Builder) {
    val emitParticles: ForgeConfigSpec.BooleanValue
    init {
        builder.section("general"){
            emitParticles = configure("emitParticles", true){
                comment = arrayOf("should emit particles for fallen and placed block")
            }
        }
    }
}