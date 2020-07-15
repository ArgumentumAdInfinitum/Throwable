package me.etmtc.throwable.impl.item

import com.mojang.blaze3d.matrix.MatrixStack
import me.etmtc.throwable.impl.block.compressor.pack
import me.etmtc.throwable.impl.item.ThrowableBlockItem.getBlockStateNBT
import net.minecraft.block.Blocks
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.Quaternion
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType.*
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer
import net.minecraft.item.ItemStack
import net.minecraftforge.client.model.data.EmptyModelData

object ThrowableISTER : ItemStackTileEntityRenderer() {
    override fun render(itemStackIn: ItemStack, matrixStackIn: MatrixStack, bufferIn: IRenderTypeBuffer, combinedLightIn: Int, combinedOverlayIn: Int) {

        matrixStackIn.pack {
            when (perspective) {
                NONE, HEAD -> {
                }
                THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND, FIRST_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND -> {
                    if (itemStackIn.orCreateTag.getBoolean("using")) {
                        translate(0.5, 0.2, 0.0)
                        scale(0.35f,0.35f,0.35f)
                    } else {
                        translate(0.0, 0.4, 0.5)
                        scale(0.4f, 0.4f, 0.4f)
                    }
                }
                GUI -> {
                    rotate(Quaternion(30f, 45f, 0f, true))
                    scale(0.63f, 0.63f, 0.63f)
                    translate(0.025, 0.4, 0.1)
                }
                GROUND -> {
                    //scale(0.5f,0.5f,0.5f)
                    translate(0.0, 0.5, 0.0)
                }
                FIXED -> {
                }
                null -> error("perspective is null")
            }
            Minecraft.getInstance().blockRendererDispatcher.renderBlock(itemStackIn.getBlockStateNBT()
                    ?: Blocks.GRASS_BLOCK.defaultState, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE)
        }

        //super.render(itemStackIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn)
    }
}