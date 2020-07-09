@file:Suppress("NOTHING_TO_INLINE")
package me.etmtc.fscraft

import net.minecraft.entity.Entity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.util.math.Vec3d

@Suppress("UNCHECKED_CAST")
inline fun <T:INBT> CompoundNBT.maybePut(name:String, nbtProducer:() -> T) =
    if(!contains(name)) {
        val nbt = nbtProducer()
        put(name, nbt)
        nbt
    } else {
        get(name)!! as T
    }
inline fun Entity.hasGravity() = !hasNoGravity()
inline operator fun Vec3d.plus(another: Vec3d) = Vec3d(x+another.x, y+another.y, z+another.z)
inline operator fun Vec3d.times(another: Vec3d) = Vec3d(x*another.x, y*another.y, z*another.z)
inline operator fun Vec3d.times(factor:Double) = Vec3d(x*factor, y*factor, z*factor)