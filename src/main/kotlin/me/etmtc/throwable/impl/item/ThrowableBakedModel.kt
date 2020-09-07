@file:Suppress("DEPRECATION")

package me.etmtc.throwable.impl.item

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.block.BlockState
import net.minecraft.client.renderer.model.BakedQuad
import net.minecraft.client.renderer.model.IBakedModel
import net.minecraft.client.renderer.model.ItemCameraTransforms
import net.minecraft.client.renderer.model.ItemOverrideList
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.Direction
import java.util.*

var perspective: ItemCameraTransforms.TransformType? = null

class ThrowableBakedModel(private val existing: IBakedModel) : IBakedModel {
    override fun getQuads(state: BlockState?, side: Direction?, rand: Random): MutableList<BakedQuad> = existing.getQuads(state, side, rand)
    override fun isAmbientOcclusion() = existing.isAmbientOcclusion
    override fun getParticleTexture(): TextureAtlasSprite = existing.particleTexture
    override fun isGui3d() = existing.isGui3d
    override fun func_230044_c_() = existing.func_230044_c_()
    override fun isBuiltInRenderer() = true
    override fun getOverrides(): ItemOverrideList = existing.overrides
    override fun handlePerspective(cameraTransformType: ItemCameraTransforms.TransformType, mat: MatrixStack?) = apply {
        perspective = cameraTransformType
    }
}