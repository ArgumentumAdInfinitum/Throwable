package me.etmtc.fscraft.impl.item.blocklauncher

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.Hand

class BlockLauncherInventory(nbt: CompoundNBT, val hand: Hand) : IInventory, NBTInventoryImpl(3) {
    init {
        readContentFromNBT(nbt.getCompound("LauncherInventory"))
    }

    override fun closeInventory(player: PlayerEntity) {
        // Set NBT logic
        player.getHeldItem(Hand.MAIN_HAND).orCreateTag.getCompound("BlockLauncher").put("LauncherInventory", contentToNBT())
    }

    override fun clear() = items.fill(ItemStack.EMPTY)
    override fun getSizeInventory() = 3
    override fun isEmpty() = items.all(ItemStack::isEmpty)
    override fun getStackInSlot(index: Int): ItemStack = items.getOrNull(index) ?: ItemStack.EMPTY
    override fun decrStackSize(index: Int, count: Int): ItemStack = ItemStackHelper.getAndSplit(items, index, count)
    override fun removeStackFromSlot(index: Int): ItemStack = ItemStackHelper.getAndRemove(items, index)
    override fun setInventorySlotContents(index: Int, stack: ItemStack) {
        items[index] = stack
    }

    override fun markDirty() {}
    override fun isUsableByPlayer(player: PlayerEntity): Boolean = true
}