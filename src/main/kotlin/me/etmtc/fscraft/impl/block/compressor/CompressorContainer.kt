package me.etmtc.fscraft.impl.block.compressor

import me.etmtc.fscraft.FSContainer
import me.etmtc.fscraft.Registries
import net.minecraft.block.Block
import net.minecraft.block.NoteBlock
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.container.*
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.IContainerFactory

class CompressorContainer(type: ContainerType<*>, id: Int, playerInv: PlayerInventory, extraInv: IInventory = Inventory(2)) : FSContainer(type, id){
    object Factory: IContainerFactory<CompressorContainer> {
        override fun create(windowId: Int, inv: PlayerInventory, data: PacketBuffer?) = CompressorContainer(Registries.compressorContainerType, windowId, inv)
    }
    init {
        addSlot(object : Slot(extraInv, 0, 43+1, 34+1){
            override fun isItemValid(stack: ItemStack) = Block.getBlockFromItem(stack.item) != Items.AIR
        })
        addSlot(object : Slot(extraInv,1,43+4*18+1,34+1){
            override fun isItemValid(p_75214_1_: ItemStack) = false
        })
        addNormalPlayerSlots(playerInv, false)
    }

    override fun canInteractWith(playerIn: PlayerEntity): Boolean = true
    override fun transferStackInSlot(player: PlayerEntity, invSlot: Int): ItemStack {
        val slot = inventorySlots[invSlot]
        if(slot != null && slot.hasStack){
            val stack = slot.stack
            val copy = stack.copy()
            when(invSlot){
                0,1 -> if (mergeItemStack(stack,2, inventorySlots.size, true)) {
                    return copy
                }
                else -> if(mergeItemStack(stack, 0, 1, false)) {
                    return copy
                }
            }
        }
        return ItemStack.EMPTY
    }
}