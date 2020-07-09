package me.etmtc.fscraft.items

import com.google.common.collect.ImmutableMap
import me.etmtc.fscraft.*
import net.minecraft.block.BlockState
import net.minecraft.block.SandBlock
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.entity.item.FallingBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.inventory.container.ContainerType
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.inventory.container.Slot
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.item.SpawnEggItem
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.fml.network.IContainerFactory
import net.minecraftforge.fml.network.NetworkHooks

val ITEM_TO_BLOCK = Item.BLOCK_TO_ITEM.map {
    it.value to it.key
}.toMap()

object ItemBlockLauncher : RegistryItem(Properties().maxStackSize(1), "block_launcher"), INamedContainerProvider {
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
            val height = (6 - 4) * 18
            repeat(3) {
                this.addSlot(object : Slot(inventory, it, 8 + (it + 3) * 18, (3.5 * 18).toInt()) {
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
                    if (!mergeItemStack(itemstack1, 3, inventorySlots.size, true)) {
                        return ItemStack.EMPTY
                    }
                } else if (!mergeItemStack(itemstack1, 0, 3, false)) {
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

    class Screen(screenContainer: Container, inv: PlayerInventory, titleIn: ITextComponent) : ContainerScreen<Container>(screenContainer, inv, titleIn) {
        init {
            FSCraft.LOGGER.info("Scren initializted")
            ySize = 221
        }

        private val threeSlotBG = ResourceLocation("fscraft", "textures/gui/container/generic_3.png")
        override fun render(p_render_1_: Int, p_render_2_: Int, p_render_3_: Float) {
            this.renderBackground()
            super.render(p_render_1_, p_render_2_, p_render_3_)
            this.renderHoveredToolTip(p_render_1_, p_render_2_)
        }

        override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
            minecraft!!.textureManager!!.bindTexture(threeSlotBG)
            val i = (width - xSize) / 2
            val j = (height - ySize) / 2
            this.blit(i, j, 0, 0, xSize, 6 * 18 + 17)
            this.blit(i, j + 6 * 18 + 17, 0, 126, xSize, 96)
        }
        // blit(int x, int y, int textureX, int textureY, int width, int height);
        // blit(int x, int y, TextureAtlasSprite icon, int width, int height);
        // blit(int x, int y, int textureX, int textureY, int width, int height, int textureWidth, int textureHeight);
        // blit(int x, int y, int zLevel, float textureX, float textureY, int width, int height, int textureWidth, int textureHeight);
        // blit(int x, int y, int desiredWidth, int desiredHeight, int textureX, int textureY, int width, int height, int textureWidth, int textureHeight);
        // innerBlit(int x, int endX, int y, int endY, int zLevel, int width, int height, float textureX, float textureY, int textureWidth, int textureHeight);
    }

    override fun onItemRightClick(worldIn: World, playerIn: PlayerEntity, handIn: Hand): ActionResult<ItemStack> {
        if (handIn == Hand.MAIN_HAND)
            if (!worldIn.isRemote) {
                if (playerIn.func_225608_bj_())/* Is Sneaking */ {
                    NetworkHooks.openGui(playerIn as ServerPlayerEntity, this)
                } else {
                    playerIn.getHeldItem(Hand.MAIN_HAND)
                            .orCreateTag
                            .maybePut("BlockLauncher") { CompoundNBT() }
                            .getCompound("LauncherInventory")
                            .getList("Items", 10)
                            .getOrNull(0)?.let {
                                if (it is CompoundNBT) {
                                    val stack = ItemStack.read(it)
                                    val item = stack.item
                                    if (item != Items.AIR && !stack.isEmpty) {
                                        stack.shrink(1)
                                        stack.write(it)
                                        val vec = playerIn.positionVec
                                        val fbe = FallingBlockEntity(worldIn, vec.x, vec.y, vec.z, (ITEM_TO_BLOCK[item] ?: error("")).defaultState)
                                        fbe.fallTime = -100
                                        fbe.motion = playerIn.lookVec.mul(2.0, 2.0, 2.0)
                                        worldIn.addEntity(fbe)
                                    }
                                }
                            }
                }
            } else if(!playerIn.func_225608_bj_()) {

                //getInventory(playerIn).items.getOrNull(0).item.
            }
        return super.onItemRightClick(worldIn, playerIn, handIn)
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
