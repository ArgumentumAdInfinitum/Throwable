package me.etmtc.throwable.impl.block.compressor

import me.etmtc.throwable.Registries
import me.etmtc.throwable.impl.item.ThrowableBlockItem
import net.minecraft.block.Block
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.ISidedInventory
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraft.util.text.TranslationTextComponent

class CompressorTileEntity : TileEntity(Registries.compressorTileEntityType), INamedContainerProvider, IInventory, ISidedInventory {
    // IInventory
    private val contents = MutableList(2) { ItemStack.EMPTY }
    override fun clear() = contents.fill(ItemStack.EMPTY)
    override fun isEmpty() = contents.all(ItemStack::isEmpty)
    override fun getStackInSlot(index: Int): ItemStack = contents.getOrNull(index) ?: ItemStack.EMPTY
    override fun getSizeInventory() = 2
    override fun decrStackSize(index: Int, count: Int): ItemStack = ItemStackHelper.getAndSplit(contents, index, count)
    override fun removeStackFromSlot(index: Int): ItemStack = ItemStackHelper.getAndRemove(contents, index)
    override fun setInventorySlotContents(index: Int, stack: ItemStack) {
        contents[index] = stack
    }

    override fun markDirty() = super.markDirty()
    override fun isUsableByPlayer(player: PlayerEntity) = true

    // ISidedInventory
    override fun canExtractItem(index: Int, stack: ItemStack, direction: Direction): Boolean = index == 1
    override fun canInsertItem(index: Int, itemStackIn: ItemStack, direction: Direction?): Boolean = index == 0
    override fun getSlotsForFace(side: Direction) = when (side) {
        Direction.UP -> intArrayOf(0)
        else -> intArrayOf(1)
    }

    // INamedContainerProvider
    override fun createMenu(windowId: Int, inv: PlayerInventory, p_createMenu_3_: PlayerEntity) = CompressorContainer(Registries.compressorContainerType, windowId, inv, this)
    override fun getDisplayName() = TranslationTextComponent("container.fscraft.compressor")

    // IContainerListener


    // TileEntity
    override fun write(compound: CompoundNBT): CompoundNBT {
        compound.put("Input", contents[0].write(CompoundNBT()))
        compound.put("Output", contents[1].write(CompoundNBT()))
        return super.write(compound)
    }

    override fun read(compound: CompoundNBT) {
        contents[0] = ItemStack.read(compound.getCompound("Input"))
        contents[1] = ItemStack.read(compound.getCompound("Output"))
        super.read(compound)
    }

    fun compress() {
        // TODO update to mini-block version
        if (contents[0].isEmpty) return
        when {
            contents[1].isEmpty -> {
                contents[1] = ItemStack(ThrowableBlockItem).also {
                    it.orCreateTag.putString("block", Block.getBlockFromItem(contents[0].item).registryName.toString())
                }
            }
            Block.getBlockFromItem(contents[0].item).registryName.toString() == contents[1].orCreateTag.getString("block") -> {
                contents[1].grow(1)
            }
            else -> return
        }
        contents[0].shrink(1)
        markDirty()
    }

}