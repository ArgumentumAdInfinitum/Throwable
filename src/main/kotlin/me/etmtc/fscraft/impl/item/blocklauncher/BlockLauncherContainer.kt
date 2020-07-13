package me.etmtc.fscraft.impl.item.blocklauncher

import me.etmtc.fscraft.FSContainer
import me.etmtc.fscraft.Registries
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

class BlockLauncherContainer(type: ContainerType<BlockLauncherContainer>, id: Int, val playerInventory: PlayerInventory, val inventory: IInventory) : FSContainer(type, id) {
    private val invSize = 3

    init {
        repeat(invSize) {
            // TODO dynamic
            this.addSlot(object : Slot(inventory, it, 8 + (it + 3) * 18, 2 * 18 - 1) {
                override fun isItemValid(stack: ItemStack): Boolean {
                    // decline items that are not blocks
                    return Block.getBlockFromItem(stack.item) != Blocks.AIR
                }
            })
        }
        addNormalPlayerSlots(playerInventory, true)
    }

    object Factory : IContainerFactory<BlockLauncherContainer> {
        override fun create(windowId: Int, inv: PlayerInventory, data: PacketBuffer?): BlockLauncherContainer {
            val player = inv.player
            return BlockLauncherContainer(Registries.blockLauncherContainerType, windowId, inv, BlockLauncherItem.getInventory(player))
        }
    }

    override fun canInteractWith(playerIn: PlayerEntity) = true
    override fun onContainerClosed(playerIn: PlayerEntity) {
        super.onContainerClosed(playerIn)
        inventory.closeInventory(playerIn)
    }

    override fun transferStackInSlot(player: PlayerEntity, invSlot: Int): ItemStack? {

        val slot = inventorySlots[invSlot]
        if (slot != null && slot.hasStack) {
            val stack = slot.stack
            val copyStack = stack.copy()
            if (invSlot < invSize) {
                if (!mergeItemStack(stack, invSize, inventorySlots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (!mergeItemStack(stack, 0, invSize, false)) {
                return ItemStack.EMPTY
            }
            if (stack.isEmpty) {
                slot.putStack(ItemStack.EMPTY)
            } else {
                slot.onSlotChanged()
            }
            return copyStack
        }
        return ItemStack.EMPTY
    }
}