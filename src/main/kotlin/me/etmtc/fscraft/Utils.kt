package me.etmtc.fscraft

import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT

@Suppress("UNCHECKED_CAST")
inline fun <T:INBT> CompoundNBT.maybePut(name:String, nbtProducer:() -> T) =
    if(!contains(name)) {
        val nbt = nbtProducer()
        put(name, nbt)
        nbt
    } else {
        get(name)!! as T
    }
