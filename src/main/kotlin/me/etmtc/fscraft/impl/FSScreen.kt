package me.etmtc.fscraft.impl

import me.etmtc.fscraft.GUI_GENERAL
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.Container
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent



abstract class FSScreen<T : Container>(screenContainer: T, inv: PlayerInventory, titleIn: ITextComponent) : ContainerScreen<T>(screenContainer, inv, titleIn) {
    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        minecraft!!.textureManager.bindTexture(GUI_GENERAL)
        val i = (width - xSize) / 2
        val j = (height - ySize) / 2
        this.blit(i, j, 0, 0, xSize, ySize)
    }
    override fun render(p_render_1_: Int, p_render_2_: Int, p_render_3_: Float) {
        this.renderBackground()
        super.render(p_render_1_, p_render_2_, p_render_3_)
        this.renderHoveredToolTip(p_render_1_, p_render_2_)
    }
    protected inline fun blitSlot(x: Int, y: Int) = blit(x, y, 176, 0, 18, 18)
    protected fun blitTier(tier: Int, col0: Int, row0: Int) {
        minecraft!!.textureManager.bindTexture(GUI_GENERAL)
        val slot = 18
        val row1 = row0 + slot
        val col1 = col0 + slot
        val row2 = row0 + slot * 2
        val col2 = col0 + slot * 2

        blitSlot(col1, row1)
        if (tier > 0) {
            blitSlot(col0, row1)
            blitSlot(col2, row1)

            if (tier > 1) {


                blitSlot(col1, row0)
                blitSlot(col1, row2)

                if (tier > 2) {
                    blitSlot(col0, row0)
                    blitSlot(col0, row2)
                    blitSlot(col2, row0)
                    blitSlot(col2, row2)
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
