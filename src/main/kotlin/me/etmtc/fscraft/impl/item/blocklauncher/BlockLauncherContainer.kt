package me.etmtc.fscraft.impl.item.blocklauncher

import me.etmtc.fscraft.BLOCK_LAUNCHER_CONTAINER_TYPE
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.container.ContainerType
import net.minecraft.inventory.container.Slot
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.IContainerFactory

class BlockLauncherContainer(type: ContainerType<*>?, id: Int, val playerInventory: PlayerInventory, val inventory: IInventory) : net.minecraft.inventory.container.Container(type, id) {
    val invSize = 3

    init {
        addSlots()
    }

    object Factory : IContainerFactory<BlockLauncherContainer> {
        override fun create(windowId: Int, inv: PlayerInventory, data: PacketBuffer?): BlockLauncherContainer {
            val player = inv.player
            return BlockLauncherContainer(BLOCK_LAUNCHER_CONTAINER_TYPE, windowId, inv, ItemBlockLauncher.getInventory(player))
        }
    }


    private fun addSlots() {
        val height = (3 - 4) * 18 - 1
        repeat(invSize) {
            // TODO dynamic
            this.addSlot(object : Slot(inventory, it, 8 + (it + 3) * 18, 2 * 18 - 1) {
                override fun isItemValid(stack: ItemStack): Boolean {
                    // decline items that are not blocks
                    return Block.getBlockFromItem(stack.item) != Blocks.AIR
                }
            })
        }
        // Player Inv 3*9 + 9 hotbar
        repeat(3) { row ->
            repeat(9) { column ->
                this.addSlot(Slot(playerInventory, row * 9 + column + 9, 8 + column * 18, 103 + row * 18 + height))
            }
        }
        repeat(9) { column ->
            val slot = if (column == playerInventory.currentItem) object : Slot(playerInventory, column, 8 + column * 18, 161 + height) {
                // Do not allow player to take out the launcher in the inventory
                override fun canTakeStack(playerIn: PlayerEntity) = false
            }
            else Slot(playerInventory, column, 8 + column * 18, 161 + height)
            addSlot(slot)
        }
    }

    override fun canInteractWith(playerIn: PlayerEntity) = true
    override fun onContainerClosed(playerIn: PlayerEntity) {
        super.onContainerClosed(playerIn)
        inventory.closeInventory(playerIn)
    }

    override fun transferStackInSlot(player: PlayerEntity?, invSlot: Int): ItemStack? {
        var itemstack = ItemStack.EMPTY

        val slot = inventorySlots[invSlot]
        if (slot != null && slot.hasStack) {
            val itemstack1 = slot.stack
            itemstack = itemstack1.copy()
            if (invSlot < 3) {
                if (!mergeItemStack(itemstack1, invSize, inventorySlots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (!mergeItemStack(itemstack1, 0, invSize, false)) {
                return ItemStack.EMPTY
            }
            if (itemstack1.isEmpty) {
                slot.putStack(ItemStack.EMPTY)
            } else {
                slot.onSlotChanged()
            }
        }
        return itemstack
    }
}