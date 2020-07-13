package me.etmtc.fscraft.impl

import me.etmtc.fscraft.impl.block.compressor.CompressorItem
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack

object FSGroup : ItemGroup("fscraft_group") {
    override fun createIcon() = ItemStack(CompressorItem)
}