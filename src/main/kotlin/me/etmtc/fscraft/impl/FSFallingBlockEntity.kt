package me.etmtc.fscraft.impl

import me.etmtc.fscraft.hasGravity
import me.etmtc.fscraft.plus
import me.etmtc.fscraft.times
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.FallingBlock
import net.minecraft.entity.MoverType
import net.minecraft.fluid.Fluids
import net.minecraft.item.DirectionalPlaceContext
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.GameRules
import net.minecraft.world.World

class FSFallingBlockEntity(world: World, x: Double, y: Double, z: Double, private var fallingBlockState: BlockState, var timeOut: Int) : net.minecraft.entity.item.FallingBlockEntity(world, x, y, z, fallingBlockState) {
    var waitTicks = 0
    var shouldWait = 20
    private fun doDrop() {
        if (world.gameRules.getBoolean(GameRules.DO_ENTITY_DROPS)) {
            this.entityDropItem(fallingBlockState.block)
        }
        this.remove()
    }

    // Returns true if wants to drop.
    private fun doTick(): Boolean {
        val isServerSide = !world.isRemote
        if (timeOut-- == 0 && isServerSide) {
            doDrop()
            return false
        }
        if (hasGravity()) motion += Vec3d(0.0, -0.04, 0.0)
        move(MoverType.SELF, motion)
        if (isServerSide) {
            // TO not DO: Concrete powder logic

            if (onGround) {
                val block = fallingBlockState.block
                motion *= Vec3d(0.7, -0.5, 0.7)
                var pos = BlockPos(this)
                var blockstate = world.getBlockState(pos)
                if (blockstate.block !== Blocks.MOVING_PISTON) {
                    if (FallingBlock.canFallThrough(world.getBlockState(pos.func_177977_b()))) return false
                    val placeable = blockstate.isReplaceable(DirectionalPlaceContext(world, pos, Direction.DOWN, ItemStack.EMPTY, Direction.UP))
                            && fallingBlockState.isValidPosition(world, pos)
                    if (!placeable) {
                        val up = pos.up()
                        val newState = world.getBlockState(up)
                        if (fallingBlockState.isValidPosition(world, up) && newState.isReplaceable(DirectionalPlaceContext(world, up, Direction.DOWN, ItemStack.EMPTY, Direction.UP))) {
                            pos = up
                            blockstate = newState
                        } else return true
                    }
                    if (fallingBlockState.has(BlockStateProperties.WATERLOGGED) && world.getFluidState(pos).fluid === Fluids.WATER) {
                        fallingBlockState = fallingBlockState.with(BlockStateProperties.WATERLOGGED, true)
                    }
                    if (world.setBlockState(pos, fallingBlockState, 3)) {
                        this.remove()
                        if (block is FallingBlock) {
                            block.onEndFalling(world, pos, fallingBlockState, blockstate)
                        }
                        if (tileEntityData != null && fallingBlockState.hasTileEntity()) {
                            val tileentity = world.getTileEntity(pos)
                            if (tileentity != null) {
                                val compoundnbt = tileentity.write(CompoundNBT())
                                for (s in tileEntityData.keySet()) {
                                    val inbt = tileEntityData[s]
                                    if ("x" != s && "y" != s && "z" != s) {
                                        compoundnbt.put(s, inbt!!.copy())
                                    }
                                }
                                tileentity.read(compoundnbt)
                                tileentity.markDirty()
                            }
                        }
                    } else return true
                }
            }
        }
        motion *= 0.98
        return false
    }

    override fun tick() {
        if (doTick()) {
            if (waitTicks++ > shouldWait) {
                waitTicks = 0
                doDrop()
            }
        } else {
            waitTicks = 0
        }
    }
}