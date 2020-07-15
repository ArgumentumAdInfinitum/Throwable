@file:Suppress("nothing_to_inline")
package me.etmtc.throwable.config

import net.minecraftforge.common.ForgeConfigSpec
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun ForgeConfigSpec.Builder.section(str:String, op:ForgeConfigSpec.Builder.() -> Unit) {
    contract { callsInPlace(op, InvocationKind.EXACTLY_ONCE) }
    push(str)
    op()
    pop()
}
@OptIn(ExperimentalContracts::class)
inline fun ForgeConfigSpec.Builder.configure(path:String,boolean: Boolean, op: DefiningContext.() -> Unit):ForgeConfigSpec.BooleanValue{
    contract { callsInPlace(op, InvocationKind.EXACTLY_ONCE) }
    val ctx = DefiningContext().apply(op)
    ctx.comment?.let { comment(*it) }
    ctx.translation?.let { translation(it) }
    return define(path,boolean)
}
@OptIn(ExperimentalContracts::class)
inline fun ForgeConfigSpec.Builder.configure(path:String, default: Int, min:Int, max:Int, op: DefiningContext.() -> Unit):ForgeConfigSpec.IntValue {
    contract { callsInPlace(op, InvocationKind.EXACTLY_ONCE) }
    val ctx = DefiningContext().apply(op)
    ctx.comment?.let { comment(*it) }
    ctx.translation?.let { translation(it) }
    return defineInRange(path,default,min, max)
}
class DefiningContext @PublishedApi internal constructor() {
    var comment :Array<String>? = null
    var translation: String? = null
}
inline operator fun ForgeConfigSpec.BooleanValue.invoke(): Boolean = get()
inline operator fun ForgeConfigSpec.IntValue.invoke():Int = get()
inline operator fun <T> ForgeConfigSpec.ConfigValue<T>.invoke():T = get()