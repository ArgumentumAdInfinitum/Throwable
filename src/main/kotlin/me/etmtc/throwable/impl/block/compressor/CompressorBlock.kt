package me.etmtc.throwable.impl.block.compressor

import me.etmtc.throwable.impl.ThrowableGroup
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.InventoryHelper
import net.minecraft.inventory.container.Container
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.tileentity.TileEntity
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

object CompressorBlock : Block(Properties.create(Material.ROCK).notSolid().hardnessAndResistance(3.5F)) {

    init {
        defaultState = stateContainer.baseState.with(POWERED, false)
    }

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>) {
        builder.add(POWERED)
    }

    override fun onBlockActivated(state: BlockState, worldIn: World, pos: BlockPos, player: PlayerEntity, handIn: Hand, hit: BlockRayTraceResult): ActionResultType {
        if (!worldIn.isRemote && handIn == Hand.MAIN_HAND)
            NetworkHooks.openGui(player as ServerPlayerEntity, worldIn.getTileEntity(pos) as CompressorTileEntity, pos)
        return ActionResultType.SUCCESS
    }

    override fun onReplaced(state: BlockState, worldIn: World, pos: BlockPos, newState: BlockState, isMoving: Boolean) {
        if (state.block != newState.block) {
            val entity = worldIn.getTileEntity(pos)
            if (entity is CompressorTileEntity && !worldIn.isRemote) {
                InventoryHelper.dropInventoryItems(worldIn, pos, entity)
            }
            super.onReplaced(state, worldIn, pos, newState, isMoving)
        }
    }

    override fun harvestBlock(worldIn: World, player: PlayerEntity, pos: BlockPos, state: BlockState, te: TileEntity?, stack: ItemStack) {
        if (!player.isCreative)
            InventoryHelper.spawnItemStack(worldIn, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), ItemStack(CompressorItem))
        super.harvestBlock(worldIn, player, pos, state, te, stack)
    }

    override fun neighborChanged(state: BlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos, isMoving: Boolean) {
        if (!worldIn.isRemote) {
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

    override fun hasComparatorInputOverride(state: BlockState) = true
    override fun getComparatorInputOverride(blockState: BlockState, worldIn: World, pos: BlockPos) =
            (worldIn.getTileEntity(pos) as? CompressorTileEntity)
                    ?.getStackInSlot(1)
                    ?.let { Container.calcRedstoneFromInventory(Inventory(it)) }
                    ?: 0

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
        val headExtended = makeCuboidShape(3.0, 4.0, 3.0, 13.0, 6.0, 13.0)

        val pistonArm = makeCuboidShape(7.0, 13.0, 7.0, 9.0, 15.0, 9.0)
        val armExtended = makeCuboidShape(7.0, 6.0, 7.0, 9.0, 15.0, 9.0)

        val platform = makeCuboidShape(3.0, 3.0, 3.0, 13.0, 4.0, 13.0)
        val leg1 = makeCuboidShape(3.0, 1.0, 3.0, 4.0, 3.0, 4.0)
        val leg2 = makeCuboidShape(3.0, 1.0, 12.0, 4.0, 3.0, 13.0)
        val leg3 = makeCuboidShape(12.0, 1.0, 12.0, 13.0, 3.0, 13.0)
        val leg4 = makeCuboidShape(12.0, 1.0, 3.0, 13.0, 3.0, 4.0)
        shape = VoxelShapes.or(base, column1, column2, column3, column4, top, pane1, pane2, pane3, pane4, pistonHead, pistonArm, platform, leg1, leg2, leg3, leg4)
        shapeExtended = VoxelShapes.or(base, column1, column2, column3, column4, top, pane1, pane2, pane3, pane4, headExtended, armExtended, platform, leg1, leg2, leg3, leg4)

    }

    override fun getShape(p_220053_1_: BlockState, p_220053_2_: IBlockReader, p_220053_3_: BlockPos, p_220053_4_: ISelectionContext) =
            if (p_220053_1_.get(POWERED)) shapeExtended else shape
}

object CompressorItem : BlockItem(CompressorBlock, Properties().group(ThrowableGroup))