package me.etmtc.throwable.impl

import me.etmtc.throwable.impl.block.compressor.CompressorItem
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack

object ThrowableGroup : ItemGroup("throwableGroup") {
    override fun createIcon() = ItemStack(CompressorItem)
}