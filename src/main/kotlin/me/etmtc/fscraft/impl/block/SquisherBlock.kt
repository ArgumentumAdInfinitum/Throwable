package me.etmtc.fscraft.impl.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.item.BlockItem
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.math.shapes.VoxelShapes
import net.minecraft.world.IBlockReader


object SquisherBlock : Block(Properties.create(Material.ROCK).notSolid()){
    private val shape:VoxelShape
    init {
        val base = makeCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0)
        val column1 = makeCuboidShape(0.0, 1.0, 0.0, 1.0, 15.0, 1.0)
        val column2 = makeCuboidShape(15.0, 1.0, 0.0, 16.0, 15.0, 1.0)
        val column3 = makeCuboidShape(0.0, 1.0, 15.0, 1.0, 15.0, 16.0)
        val column4 = makeCuboidShape(15.0, 1.0, 15.0, 16.0, 15.0, 16.0)
        val top = makeCuboidShape(0.0, 15.0, 0.0, 16.0, 16.0, 16.0)
        val pane1 = makeCuboidShape(0.0, 1.0, 1.0, 1.0, 15.0, 15.0)
        val pane2 = makeCuboidShape(1.0, 1.0, 15.0, 15.0, 15.0, 16.0)
        val pane3 = makeCuboidShape(15.0, 1.0, 1.0, 16.0, 15.0, 15.0)
        val pane4 = makeCuboidShape(1.0, 1.0, 0.0, 15.0, 15.0, 1.0)
        val pistonHead = makeCuboidShape(3.0, 11.0, 3.0, 13.0, 13.0, 13.0)
        val pistonArm = makeCuboidShape(7.0, 13.0, 7.0, 9.0, 15.0, 9.0)
        val platform = makeCuboidShape(3.0, 3.0, 3.0, 13.0, 4.0, 13.0)
        val leg1 = makeCuboidShape(3.0, 1.0, 3.0, 4.0, 3.0, 4.0)
        val leg2 = makeCuboidShape(3.0, 1.0, 12.0, 4.0, 3.0, 13.0)
        val leg3 = makeCuboidShape(12.0, 1.0, 12.0, 13.0, 3.0, 13.0)
        val leg4 = makeCuboidShape(12.0, 1.0, 3.0, 13.0, 3.0, 4.0)
        shape = VoxelShapes.or(base, column1, column2, column3, column4, top, pane1, pane2, pane3, pane4, pistonHead, pistonArm,platform, leg1, leg2, leg3, leg4)
    }

    override fun getShape(p_220053_1_: BlockState, p_220053_2_: IBlockReader, p_220053_3_: BlockPos, p_220053_4_: ISelectionContext): VoxelShape {
        return shape
    }
}
object SquisherItem : BlockItem(SquisherBlock, Properties())