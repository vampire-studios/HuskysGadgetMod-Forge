package io.github.vampirestudios.hgm.block;

import io.github.vampirestudios.hgm.api.print.IPrint;
import io.github.vampirestudios.hgm.block.entity.TileEntityPaper;
import io.github.vampirestudios.hgm.object.Bounds;
import io.github.vampirestudios.hgm.utils.CollisionHelper;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockPaper extends BlockFacing {

    private static final Bounds SELECTION_BOUNDS = new Bounds(15 * 0.0625, 0.0, 0.0, 16 * 0.0625, 16 * 0.0625, 16 * 0.0625);
    private static final AxisAlignedBB SELECTION_BOX_NORTH = CollisionHelper.getBlockBounds(Direction.NORTH, SELECTION_BOUNDS);
    private static final AxisAlignedBB SELECTION_BOX_EAST = CollisionHelper.getBlockBounds(Direction.EAST, SELECTION_BOUNDS);
    private static final AxisAlignedBB SELECTION_BOX_SOUTH = CollisionHelper.getBlockBounds(Direction.SOUTH, SELECTION_BOUNDS);
    private static final AxisAlignedBB SELECTION_BOX_WEST = CollisionHelper.getBlockBounds(Direction.WEST, SELECTION_BOUNDS);
    private static final AxisAlignedBB[] SELECTION_BOUNDING_BOX = {SELECTION_BOX_SOUTH, SELECTION_BOX_WEST, SELECTION_BOX_NORTH, SELECTION_BOX_EAST};

    public BlockPaper() {
        super("paper", Material.WOOL);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return VoxelShapes.create(SELECTION_BOUNDING_BOX[state.get(FACING).getHorizontalIndex()]);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state.with(FACING, context.getFace().getAxis() == Direction.Axis.Y ? context.getPlayer().getHorizontalFacing() : context.getFace().getOpposite());
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileEntityPaper) {
                TileEntityPaper paper = (TileEntityPaper) tileEntity;
                paper.nextRotation();
            }
        }
        return true;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityPaper) {
            TileEntityPaper paper = (TileEntityPaper) tileEntity;
            ItemStack drop = IPrint.generateItem(paper.getPrint());
            worldIn.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityPaper();
    }

}
