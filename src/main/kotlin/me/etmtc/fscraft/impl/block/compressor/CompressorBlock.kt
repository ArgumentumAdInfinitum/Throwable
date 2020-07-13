package me.etmtc.fscraft.impl.block.compressor

import me.etmtc.fscraft.Initialized
import me.etmtc.fscraft.impl.FSGroup
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FurnaceBlock
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.container.FurnaceContainer
import net.minecraft.item.BlockItem
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.math.shapes.VoxelShapes
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkHooks

private val POWERED = BlockStateProperties.POWERED
object CompressorBlock : Block(Properties.create(Material.ROCK).notSolid()) {

    init {
        defaultState = stateContainer.baseState.with(POWERED, false)
    }

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>) {
        builder.add(POWERED)
    }
    override fun onBlockActivated(state: BlockState, worldIn: World, pos: BlockPos, player: PlayerEntity, handIn: Hand, hit: BlockRayTraceResult): ActionResultType {
        if (!worldIn.isRemote && handIn == Hand.MAIN_HAND)
            NetworkHooks.openGui(player as ServerPlayerEntity, worldIn.getTileEntity(pos) as CompressorTileEntity)
        return ActionResultType.SUCCESS
    }

    override fun neighborChanged(state: BlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos, isMoving: Boolean) {
        // FIXME placing block on creative mode??
        if(!worldIn.isRemote){
            val flag = worldIn.isBlockPowered(pos)
            if (flag != state.get(BlockStateProperties.POWERED)) {
                if (flag) {
                    val tile = worldIn.getTileEntity(pos)!!
                    tile as CompressorTileEntity
                    tile.compress()
                }
                worldIn.setBlockState(pos, state.with(BlockStateProperties.POWERED, flag), 3)
            }
        }
    }
    override fun hasTileEntity(state: BlockState?) = true
    override fun createTileEntity(state: BlockState?, world: IBlockReader?) = CompressorTileEntity()

    private val shape: VoxelShape
    private val shapeExtended: VoxelShape
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
        val headExtended = makeCuboidShape(3.0,4.0,3.0,13.0,6.0,13.0)

        val pistonArm = makeCuboidShape(7.0, 13.0, 7.0, 9.0, 15.0, 9.0)
        val armExtended = makeCuboidShape(7.0,6.0,7.0,9.0,15.0,9.0)

        val platform = makeCuboidShape(3.0, 3.0, 3.0, 13.0, 4.0, 13.0)
        val leg1 = makeCuboidShape(3.0, 1.0, 3.0, 4.0, 3.0, 4.0)
        val leg2 = makeCuboidShape(3.0, 1.0, 12.0, 4.0, 3.0, 13.0)
        val leg3 = makeCuboidShape(12.0, 1.0, 12.0, 13.0, 3.0, 13.0)
        val leg4 = makeCuboidShape(12.0, 1.0, 3.0, 13.0, 3.0, 4.0)
        shape = VoxelShapes.or(base, column1, column2, column3, column4, top, pane1, pane2, pane3, pane4, pistonHead, pistonArm, platform, leg1, leg2, leg3, leg4)
        shapeExtended = VoxelShapes.or(base, column1, column2, column3, column4, top, pane1, pane2, pane3, pane4, headExtended, armExtended, platform, leg1, leg2, leg3, leg4)

    }

    override fun getShape(p_220053_1_: BlockState, p_220053_2_: IBlockReader, p_220053_3_: BlockPos, p_220053_4_: ISelectionContext): VoxelShape {
        return if(p_220053_1_.get(POWERED)) shapeExtended else shape
    }
}

object CompressorItem : BlockItem(CompressorBlock, Properties().group(FSGroup))