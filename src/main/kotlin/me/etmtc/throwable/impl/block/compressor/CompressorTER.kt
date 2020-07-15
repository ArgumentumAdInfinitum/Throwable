@file:Suppress("DEPRECATION")

package me.etmtc.throwable.impl.block.compressor

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.Quaternion
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.item.ItemStack
import net.minecraft.state.properties.BlockStateProperties
import kotlin.math.PI

inline fun MatrixStack.pack(op: MatrixStack.() -> Unit) {
    push()
    op()
    pop()
}
class CompressorTER(rendererDispatcherIn: TileEntityRendererDispatcher) : TileEntityRenderer<CompressorTileEntity>(rendererDispatcherIn) {

    override fun render(tileEntityIn: CompressorTileEntity, partialTicks: Float, matrixStackIn: MatrixStack, bufferIn: IRenderTypeBuffer, combinedLightIn: Int, combinedOverlayIn: Int) {
        if (!tileEntityIn.blockState[BlockStateProperties.POWERED])
            matrixStackIn.pack {
                ItemStack(tileEntityIn.getStackInSlot(0).item).let {
                    val l = System.currentTimeMillis() and 16383
                    // Il|1Il|1
                    translate(0.5, 0.4, 0.5)
                    scale(0.3f, 0.3f, 0.3f)
                    rotate(Quaternion(0f,(l / 16383f * PI * 4).toFloat(),0f, false))
                    val renderer = Minecraft.getInstance().itemRenderer
                    renderer.renderItem(it, TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn)
                    //Minecraft.getInstance().blockRendererDispatcher.renderBlock(it, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE)
                }
            }
    }


}
