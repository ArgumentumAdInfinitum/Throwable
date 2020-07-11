package me.etmtc.fscraft.impl.item.blocklauncher

import me.etmtc.fscraft.impl.AbstractEmptyContainerScreen
import me.etmtc.fscraft.items.ItemBlockLauncher
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.text.ITextComponent

class BlockLauncherScreen(screenContainer: ItemBlockLauncher.Container, inv: PlayerInventory, titleIn: ITextComponent) : AbstractEmptyContainerScreen<ItemBlockLauncher.Container>(screenContainer, inv, titleIn) {
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