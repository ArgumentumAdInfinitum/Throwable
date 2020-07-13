package me.etmtc.fscraft.impl.block.compressor

import me.etmtc.fscraft.impl.FSScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.text.ITextComponent


class CompressorScreen(screenContainer: CompressorContainer, inv: PlayerInventory, titleIn: ITextComponent) : FSScreen<CompressorContainer>(screenContainer, inv, titleIn){
    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY)
        val i = (width - xSize) / 2 + 43
        val j = (height - ySize) / 2 + 34
        blitSlot(i, j)
        blitSlot(i + 4*18, j)

    }
}
// val widthArrow = 22