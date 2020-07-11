package me.etmtc.fscraft

import net.minecraftforge.registries.ForgeRegistryEntry
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.IForgeRegistryEntry



inline class RegProcess<T : ForgeRegistryEntry<T>>(val registry: IForgeRegistry<T>){
    inline operator fun T.minus(registryName: String){
        registry.register(this.setRegistryName(MODID,registryName))
    }
}


inline fun <T : ForgeRegistryEntry<T>> IForgeRegistry<T>.registers(op: RegProcess<T>.() -> Unit){
    RegProcess(this).apply(op)
}