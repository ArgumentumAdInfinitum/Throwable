package me.etmtc.throwable.impl.block.compressor

import me.etmtc.throwable.GUI_GENERAL
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.text.ITextComponent


class CompressorScreen(screenContainer: CompressorContainer, inv: PlayerInventory, titleIn: ITextComponent) : ContainerScreen<CompressorContainer>(screenContainer, inv, titleIn){
    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        minecraft!!.textureManager.bindTexture(GUI_GENERAL)
        val ii = (width - xSize) / 2
        val jj = (height - ySize) / 2
        this.blit(ii, jj, 0, 0, xSize, ySize)
        val i = (width - xSize) / 2 + 43
        val j = (height - ySize) / 2 + 34
        blit(i, j, 176, 0, 18, 18)
        blit(i+4*18, j, 176, 0, 18, 18)
        blit(i + 2*18-2, j+1,194,0,22,15)
        blit(i + 2*18 + 5, j+5, 194,15,4,11)
        val str = I18n.format("gui.throwable.compressor.title")
        this.font.drawString(str, ((width - this.font.getStringWidth(str)) / 2).toFloat(), (jj+10).toFloat(), 4210752)
    }
    override fun render(p_render_1_: Int, p_render_2_: Int, p_render_3_: Float) {
        this.renderBackground()
        super.render(p_render_1_, p_render_2_, p_render_3_)
        this.renderHoveredToolTip(p_render_1_, p_render_2_)
    }
}
