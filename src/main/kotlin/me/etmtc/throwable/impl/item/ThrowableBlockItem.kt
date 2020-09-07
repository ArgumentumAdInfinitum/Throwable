package me.etmtc.throwable.impl.item

import me.etmtc.throwable.impl.CustomFallingBlockEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.*
import net.minecraft.stats.Stats
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.registries.ForgeRegistries
import java.util.*
import java.util.concurrent.Callable

fun Entity.shoot(shooter: Entity, pitch: Float, yaw: Float, velocity: Float, inaccuracy: Float) {
    // Copied from bow item
    val x = -MathHelper.sin(yaw * (Math.PI.toFloat() / 180f)) * MathHelper.cos(pitch * (Math.PI.toFloat() / 180f)).toDouble()
    val y = -MathHelper.sin(pitch * (Math.PI.toFloat() / 180f)).toDouble()
    val z = MathHelper.cos(yaw * (Math.PI.toFloat() / 180f)) * MathHelper.cos(pitch * (Math.PI.toFloat() / 180f)).toDouble()
    val vec3d = Vec3d(x, y, z).normalize().add(Random().nextGaussian() * 0.0075f.toDouble() * inaccuracy.toDouble(), Random().nextGaussian() * 0.0075f.toDouble() * inaccuracy.toDouble(), Random().nextGaussian() * 0.0075f.toDouble() * inaccuracy.toDouble()).scale(velocity.toDouble())
    motion = vec3d
    val f = MathHelper.sqrt(Entity.horizontalMag(vec3d))
    this.rotationYaw = (MathHelper.atan2(vec3d.x, vec3d.z) * (180f / Math.PI.toFloat()).toDouble()).toFloat()
    this.rotationPitch = (MathHelper.atan2(vec3d.y, f.toDouble()) * (180f / Math.PI.toFloat()).toDouble()).toFloat()
    this.prevRotationYaw = this.rotationYaw
    this.prevRotationPitch = this.rotationPitch
    this.motion = this.motion.add(shooter.motion.x, if (shooter.onGround) 0.0 else shooter.motion.y, shooter.motion.z)
}

object ThrowableBlockItem : Item(Properties().setISTER { Callable { ThrowableISTER } }) {
    override fun onItemRightClick(worldIn: World, playerIn: PlayerEntity, handIn: Hand): ActionResult<ItemStack> {
        playerIn.activeHand = handIn
        return ActionResult.resultConsume(playerIn.getHeldItem(handIn))
    }

    override fun getUseDuration(stack: ItemStack) = 72000
    override fun getUseAction(stack: ItemStack) = UseAction.BOW
    override fun onUse(worldIn: World, livingEntityIn: LivingEntity, stack: ItemStack, count: Int) {
        stack.orCreateTag.putBoolean("using", true)
    }

    override fun onPlayerStoppedUsing(stack: ItemStack, worldIn: World, entity: LivingEntity, timeLeft: Int) {
        stack.orCreateTag.putBoolean("using", false)
        if (entity is PlayerEntity) {
            val f = BowItem.getArrowVelocity(getUseDuration(stack) - timeLeft)
            if (f >= 0.1 && !worldIn.isRemote) {
                stack.getBlockStateNBT(entity)?.let {
                    val e = CustomFallingBlockEntity(worldIn, entity.posX, entity.posY, entity.posZ, it)
                    e.shoot(entity, entity.rotationPitch, entity.rotationYaw, f * 3f, 0.5f)
                    if (!entity.abilities.isCreativeMode)
                        stack.shrink(1)
                    entity.addStat(Stats.ITEM_USED[this])
                    worldIn.addEntity(e)
                }

            }

        }

    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun Block.getBlockState(playerEntity: PlayerEntity?) = try {
        getStateForPlacement(BlockItemUseContext(ItemUseContext(playerEntity, Hand.MAIN_HAND, null)))
    } catch (t: Throwable) {
        defaultState
    }

    fun ItemStack.getBlockStateNBT(playerEntity: PlayerEntity? = null): BlockState? =
            ForgeRegistries.BLOCKS.getValue(ResourceLocation(orCreateTag.getString("block")))?.getBlockState(playerEntity)


}