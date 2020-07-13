package me.etmtc.fscraft.impl.block.compressor

import me.etmtc.fscraft.FSContainer
import me.etmtc.fscraft.Registries
import net.minecraft.block.NoteBlock
import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.inventory.container.ContainerType
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.inventory.container.Slot
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.fml.network.IContainerFactory

class CompressorTileEntity : TileEntity(Registries.compressorTileEntityType), INamedContainerProvider, IInventory {
    // IInventory
    private val contents = MutableList(2) { ItemStack.EMPTY }
    override fun clear() = contents.fill(ItemStack.EMPTY)
    override fun isEmpty() = contents.all(ItemStack::isEmpty)
    override fun getStackInSlot(index: Int): ItemStack = contents.getOrNull(index) ?: ItemStack.EMPTY
    override fun getSizeInventory() = 2
    override fun decrStackSize(index: Int, count: Int): ItemStack = ItemStackHelper.getAndSplit(contents, index, count)
    override fun removeStackFromSlot(index: Int): ItemStack = ItemStackHelper.getAndRemove(contents, index)
    override fun setInventorySlotContents(index: Int, stack: ItemStack){
        contents[index] = stack
    }
    override fun markDirty() = super.markDirty()
    override fun isUsableByPlayer(player: PlayerEntity) = true

    // INamedContainerProvider
    override fun createMenu(windowId: Int, inv: PlayerInventory, p_createMenu_3_: PlayerEntity) = CompressorContainer(Registries.compressorContainerType, windowId, inv,this)
    override fun getDisplayName() = TranslationTextComponent("container.fscraft.compressor")

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

    fun compress(){
        // TODO update to mini-block version
        if(contents[0].isEmpty) return
        contents[0].shrink(1)
        when {
            contents[0].isItemEqual(contents[1]) -> {
                contents[1].grow(1)
            }
            contents[1].isEmpty -> {
                contents[1] = contents[0].copy().also {
                    it.count = 1
                }
            }
            else -> return
        }
        markDirty()
    }

}