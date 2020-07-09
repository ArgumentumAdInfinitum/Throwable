package me.etmtc.fscraft.items

import net.minecraft.item.Item

abstract class RegistryItem(properties: Properties, registryName: String) : Item(properties){
    init {
        setRegistryName("fscraft:$registryName")
    }
}