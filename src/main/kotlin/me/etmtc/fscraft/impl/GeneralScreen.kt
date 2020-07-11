package me.etmtc.fscraft.impl

import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.Container
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent

val GUI_GENERAL = ResourceLocation("fscraft", "textures/gui/container/general_small.png")

abstract class AbstractEmptyContainerScreen<T : Container>(screenContainer: T, inv: PlayerInventory, titleIn: ITextComponent) : ContainerScreen<T>(screenContainer, inv, titleIn) {
    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        minecraft!!.textureManager.bindTexture(GUI_GENERAL)
        val i = (width - xSize) / 2
        val j = (height - ySize) / 2
        this.blit(i, j, 0, 0, xSize, ySize)
    }

    protected fun blitTier(tier: Int, col0: Int, row0: Int) {
        minecraft!!.textureManager.bindTexture(GUI_GENERAL)
        val slot = 18
        val slotX = 176
        val slotY = 0
        val row1 = row0 + slot
        val row2 = row0 + slot * 2
        val col1 = col0 + slot
        val col2 = col0 + slot * 2

        blit(col1, row1, slotX, slotY, slot, slot)
        if (tier > 0) {
            blit(col0, row1, slotX, slotY, slot, slot)
            blit(col2, row1, slotX, slotY, slot, slot)

            if (tier > 1) {
                blit(col1, row0, slotX, slotY, slot, slot)
                blit(col1, row2, slotX, slotY, slot, slot)

                if (tier > 2) {
                    blit(col0, row0, slotX, slotY, slot, slot)
                    blit(col0, row2, slotX, slotY, slot, slot)
                    blit(col2, row0, slotX, slotY, slot, slot)
                    blit(col2, row2, slotX, slotY, slot, slot)
                }
            }
        }
    }
    // blit(int x, int y, int textureX, int textureY, int width, int height);
    // blit(int x, int y, TextureAtlasSprite icon, int width, int height);
    // blit(int x, int y, int textureX, int textureY, int width, int height, int textureWidth, int textureHeight);
    // blit(int x, int y, int zLevel, float textureX, float textureY, int width, int height, int textureWidth, int textureHeight);
    // blit(int x, int y, int desiredWidth, int desiredHeight, int textureX, int textureY, int width, int height, int textureWidth, int textureHeight);
    // innerBlit(int x, int endX, int y, int endY, int zLevel, int width, int height, float textureX, float textureY, int textureWidth, int textureHeight);
}
