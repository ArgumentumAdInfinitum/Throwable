package me.etmtc.fscraft.impl.block.compressor

import me.etmtc.fscraft.impl.FSScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.text.ITextComponent


class CompressorScreen(screenContainer: CompressorContainer, inv: PlayerInventory, titleIn: ITextComponent) : FSScreen<CompressorContainer>(screenContainer, inv, titleIn){
   // 17x7 (16,6)
    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY)

        val i = (width - xSize) / 2 + 43
        val j = (height - ySize) / 2 + 34
        blitSlot(i, j)
        blitSlot(i + 4*18, j)
        blit(i + 2*18-2, j+1,194,0,22,15)
        blit(i + 2*18 + 5, j+5, 194,15,4,11)
    }
}
// val widthArrow = 22