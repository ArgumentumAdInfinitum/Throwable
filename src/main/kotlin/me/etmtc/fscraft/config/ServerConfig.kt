package me.etmtc.fscraft.config

import net.minecraftforge.common.ForgeConfigSpec
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ServerConfig(builder: ForgeConfigSpec.Builder) {
    private val shouldUseRewrittenPhysics:ForgeConfigSpec.BooleanValue
    init {
        builder.section("general") {
            shouldUseRewrittenPhysics = configure("shouldUseRewrittenPhysics", true){
                comment = arrayOf("Use rewritten physics for falling blocks. " +
                        "If set to true falling blocks will wait for specified value before dropping an item.")

            }
        }
    }
}
