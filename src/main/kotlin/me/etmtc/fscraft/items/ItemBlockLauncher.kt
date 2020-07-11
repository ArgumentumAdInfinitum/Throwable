package me.etmtc.fscraft.items

import me.etmtc.fscraft.*
import me.etmtc.fscraft.impl.*
import me.etmtc.fscraft.impl.item.blocklauncher.NBTInventoryImpl
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.inventory.container.ContainerType
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.inventory.container.Slot
import net.minecraft.item.*
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import net.minecraft.util.*
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World
import net.minecraftforge.fml.network.IContainerFactory
import net.minecraftforge.fml.network.NetworkHooks

val ITEM_TO_BLOCK = Item.BLOCK_TO_ITEM.map {
    it.value to it.key
}.toMap()

object ItemBlockLauncher : Item(Properties().maxStackSize(1)), INamedContainerProvider {
    init {

    }
    class Inventory(nbt: CompoundNBT, val hand: Hand) : IInventory, NBTInventoryImpl(3) {
        init {
            readContentFromNBT(nbt.getCompound("LauncherInventory"))
        }

        override fun closeInventory(player: PlayerEntity) {
            // Set NBT logic
            player.getHeldItem(Hand.MAIN_HAND).orCreateTag.getCompound("BlockLauncher").put("LauncherInventory", contentToNBT())
        }

        override fun clear() = items.clear()
        override fun getSizeInventory() = 3
        override fun isEmpty() = items.all(ItemStack::isEmpty)
        override fun getStackInSlot(index: Int): ItemStack = items[index]
        override fun decrStackSize(index: Int, count: Int): ItemStack = ItemStackHelper.getAndSplit(items, index, count)
        override fun removeStackFromSlot(index: Int): ItemStack = ItemStackHelper.getAndRemove(items, index)
        override fun setInventorySlotContents(index: Int, stack: ItemStack) {
            items[index] = stack
        }

        override fun markDirty() {}
        override fun isUsableByPlayer(player: PlayerEntity): Boolean = true
    }

    class Container(type: ContainerType<*>?, id: Int, val playerInventory: PlayerInventory, val inventory: IInventory) : net.minecraft.inventory.container.Container(type, id) {
        val invSize = 3

        init {
            addSlots()
        }

        object Factory : IContainerFactory<Container> {
            override fun create(windowId: Int, inv: PlayerInventory, data: PacketBuffer?): Container {
                val player = inv.player
                return Container(BLOCK_LAUNCHER_CONTAINER_TYPE, windowId, inv, getInventory(player))
            }
        }


        private fun addSlots() {
            val height = (3 - 4) * 18 - 1
            repeat(invSize) {
                // TODO dynamic centering
                this.addSlot(object : Slot(inventory, it, 8 + (it + 3) * 18, 2 * 18 - 1) {
                    override fun isItemValid(stack: ItemStack): Boolean {
                        // decline items that are not blocks
                        return ITEM_TO_BLOCK.containsKey(stack.item)
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



    class Screen(screenContainer: Container, inv: PlayerInventory, titleIn: ITextComponent) : AbstractEmptyContainerScreen<Container>(screenContainer, inv, titleIn) {
        val tier = 1
        override fun render(p_render_1_: Int, p_render_2_: Int, p_render_3_: Float) {
            this.renderBackground()
            super.render(p_render_1_, p_render_2_, p_render_3_)
            this.renderHoveredToolTip(p_render_1_, p_render_2_)
        }

        override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
            super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY)
            val i = (width - xSize) / 2 + 61
            val j = (height - ySize) / 2 + 16
            blitTier(tier, i, j)
        }

    }

    override fun onItemRightClick(worldIn: World, playerIn: PlayerEntity, handIn: Hand): ActionResult<ItemStack> {
        if (handIn == Hand.MAIN_HAND && !worldIn.isRemote) {
            if (playerIn.isSneaking){
                NetworkHooks.openGui(playerIn as ServerPlayerEntity, this)
            } else {

                playerIn.getHeldItem(Hand.MAIN_HAND)
                        .orCreateTag
                        .maybePut("BlockLauncher") { CompoundNBT() }
                        .getCompound("LauncherInventory")
                        .getList("Items", 10).also { listTag ->
                            listTag.firstOrNull()?.let {
                                if (it is CompoundNBT) {
                                    val stack = ItemStack.read(it)
                                    val item = stack.item
                                    if (item != Items.AIR && !stack.isEmpty) {
                                        stack.shrink(1)
                                        if (stack.isEmpty)
                                            listTag.remove(it)
                                        else stack.write(it)
                                        val vec = playerIn.positionVec
                                        val fbe = FSFallingBlockEntity(worldIn, vec.x, vec.y + 1.62, vec.z, (ITEM_TO_BLOCK[item]
                                                ?: error("")).defaultState)
                                        fbe.motion = playerIn.lookVec.mul(2.0, 2.0, 2.0)
                                        worldIn.addEntity(fbe)
                                    }
                                }
                            }
                        }
            }
        }
        return ActionResult(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn))
    }

    fun getInventory(entity: PlayerEntity): Inventory {
        val stack = entity.getHeldItem(Hand.MAIN_HAND)
        val nbt = stack.orCreateTag
        val tag = nbt.maybePut("BlockLauncher") { CompoundNBT() }
        return Inventory(tag, Hand.MAIN_HAND)
    }

    override fun createMenu(i: Int, playerInventory: PlayerInventory, playerEntity: PlayerEntity): net.minecraft.inventory.container.Container? {
        return Container(BLOCK_LAUNCHER_CONTAINER_TYPE, i, playerInventory, getInventory(playerEntity))
    }

    override fun getDisplayName(): ITextComponent = TranslationTextComponent(BLOCK_LAUNCHER_TRANSLATION_KEY)
}
