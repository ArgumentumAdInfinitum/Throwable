package me.etmtc.fscraft.impl.jei

import net.minecraft.item.ItemStack

data class FSCompressorRecipe(var input: MutableList<ItemStack>, var output: MutableList<ItemStack>)