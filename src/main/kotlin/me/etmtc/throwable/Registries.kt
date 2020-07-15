package me.etmtc.throwable

import me.etmtc.throwable.impl.block.compressor.CompressorBlock
import me.etmtc.throwable.impl.block.compressor.CompressorContainer
import me.etmtc.throwable.impl.block.compressor.CompressorItem
import me.etmtc.throwable.impl.block.compressor.CompressorTileEntity
import me.etmtc.throwable.impl.item.ThrowableBlockItem
import net.minecraft.inventory.container.ContainerType
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.IForgeRegistryEntry
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import java.util.function.Supplier

@FunctionalInterface
interface Initialized {
    fun init()
}

// Conveniently registers all the stuff.

object Registries {
    val compressorContainerType = ContainerType(CompressorContainer.Factory)

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    val compressorTileEntityType: TileEntityType<CompressorTileEntity> = TileEntityType.Builder.create({CompressorTileEntity()},CompressorBlock).build(null)
    private val deferredHolders : MutableMap<Class<out IForgeRegistryEntry<*>>, DeferredRegister<*>> = mutableMapOf()
    private fun registerDeferredRegisters() = deferredHolders.values.forEach { it.register(MOD_BUS) }
    operator fun invoke(){
        registerAll()
        registerDeferredRegisters()
    }
    @Suppress("UNCHECKED_CAST")
    private fun <T:IForgeRegistryEntry<T>> getDeferredRegister(clazz: Class<T>): DeferredRegister<T> =
            deferredHolders.getOrPut(clazz, {DeferredRegister(GameRegistry.findRegistry(clazz), MODID)}) as DeferredRegister<T>

    private fun <T:IForgeRegistryEntry<T>> registration(clazz :Class<T>,str:String, sup: Supplier<T> ) = getDeferredRegister(clazz).register(str, sup)
    private fun <T:IForgeRegistryEntry<T>> registration(clazz :Class<T>,str:String, inst: T ) = if(inst is Initialized) getDeferredRegister(clazz).register(str){inst.also(Initialized::init)} else getDeferredRegister(clazz).register(str) { inst }

    private inline operator fun <reified T: IForgeRegistryEntry<T>> String.minus(supplier:Supplier<T>) = registration(T::class.java, this, supplier)

    // deferred register says it should return new instances every time but i think it works with kotlin objects anyways
    private inline operator fun <reified T: IForgeRegistryEntry<T>> String.minus(inst: T) = registration(T::class.java, this,inst)

    private fun registerAll(){
        "compressor" - CompressorItem
        "throwable" - ThrowableBlockItem
        "compressor_block" - CompressorBlock
        "compressor" - compressorContainerType
        "block_launcher_tile_entity" - compressorTileEntityType
    }
}
