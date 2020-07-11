package me.etmtc.fscraft.impl.item.blocklauncher

import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.ListNBT

abstract class NBTInventoryImpl(size: Int) {
    val items: MutableList<ItemStack> = MutableList(size){ ItemStack.EMPTY }
    fun contentToNBT(): CompoundNBT = CompoundNBT().also { nbt ->
        val listNBT = ListNBT()
        listNBT.addAll(items.mapIndexed { index, itemStack -> index to itemStack }.toMap().filterValues { !it.isEmpty }.map { (i, item) ->
            CompoundNBT().also {
                it.putInt("Index", i)
                item.write(it)
            }
        })
        nbt.put("Items", listNBT)
    }
    fun readContentFromNBT(nbt:CompoundNBT){
        val list = nbt.getList("Items", 10)
        repeat(list.size){ i ->
            val compound = list.getCompound(i)
            val index = compound.getInt("Index")
            if(index >= 0 && index < items.size)
                items[index] = ItemStack.read(compound)
        }
    }
}