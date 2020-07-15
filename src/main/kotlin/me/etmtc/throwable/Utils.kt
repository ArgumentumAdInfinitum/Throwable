@file:Suppress("NOTHING_TO_INLINE")
package me.etmtc.throwable

import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.ContainerType
import net.minecraft.inventory.container.Slot
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
abstract class FSContainer(type: ContainerType<*>, id: Int) : Container(type, id) {
    fun addNormalPlayerSlots(playerInventory: PlayerInventory, shouldLimitTakeStack:Boolean){
        repeat(3) { row -> repeat(9) { column ->
            this.addSlot(Slot(playerInventory, row * 9 + column + 9, 8 + column * 18, 84 + row * 18))
        }}
        repeat(9) { column ->
            val slot = if (column == playerInventory.currentItem && shouldLimitTakeStack) object : Slot(playerInventory, column, 8 + column * 18, 142) {
                // Do not allow player to take out the launcher in the inventory
                override fun canTakeStack(playerIn: PlayerEntity) = false
            }
            else Slot(playerInventory, column, 8 + column * 18, 142)
            addSlot(slot)
        }
    }
}

