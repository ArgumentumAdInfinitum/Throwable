package me.etmtc.fscraft.impl.item.blocklauncher

import me.etmtc.fscraft.impl.FSScreen

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.text.ITextComponent

class BlockLauncherScreen(screenContainer: BlockLauncherContainer, inv: PlayerInventory, titleIn: ITextComponent) : FSScreen<BlockLauncherContainer>(screenContainer, inv, titleIn) {
    private val tier = 1

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY)
        val i = (width - xSize) / 2 + 61
        val j = (height - ySize) / 2 + 16
        blitTier(tier, i, j)
    }
}