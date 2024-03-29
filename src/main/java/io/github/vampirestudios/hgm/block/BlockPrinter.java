package io.github.vampirestudios.hgm.block;

import io.github.vampirestudios.hgm.block.entity.TileEntityPrinter;
import io.github.vampirestudios.hgm.object.Bounds;
import io.github.vampirestudios.hgm.utils.CollisionHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockPrinter extends BlockColoredDevice {

    private static final Bounds BODY_BOUNDS = new Bounds(5 * 0.0625, 0.0, 1 * 0.0625, 14 * 0.0625, 5 * 0.0625, 15 * 0.0625);
    private static final AxisAlignedBB BODY_BOX_NORTH = CollisionHelper.getBlockBounds(Direction.NORTH, BODY_BOUNDS);
    private static final AxisAlignedBB BODY_BOX_EAST = CollisionHelper.getBlockBounds(Direction.EAST, BODY_BOUNDS);
    private static final AxisAlignedBB BODY_BOX_SOUTH = CollisionHelper.getBlockBounds(Direction.SOUTH, BODY_BOUNDS);
    private static final AxisAlignedBB BODY_BOX_WEST = CollisionHelper.getBlockBounds(Direction.WEST, BODY_BOUNDS);
    private static final AxisAlignedBB[] BODY_BOUNDING_BOX = {BODY_BOX_SOUTH, BODY_BOX_WEST, BODY_BOX_NORTH, BODY_BOX_EAST};

    private static final Bounds TRAY_BOUNDS = new Bounds(0.5 * 0.0625, 0.0, 3.5 * 0.0625, 5 * 0.0625, 1 * 0.0625, 12.5 * 0.0625);
    private static final AxisAlignedBB TRAY_BOX_NORTH = CollisionHelper.getBlockBounds(Direction.NORTH, TRAY_BOUNDS);
    private static final AxisAlignedBB TRAY_BOX_EAST = CollisionHelper.getBlockBounds(Direction.EAST, TRAY_BOUNDS);
    private static final AxisAlignedBB TRAY_BOX_SOUTH = CollisionHelper.getBlockBounds(Direction.SOUTH, TRAY_BOUNDS);
    private static final AxisAlignedBB TRAY_BOX_WEST = CollisionHelper.getBlockBounds(Direction.WEST, TRAY_BOUNDS);
    private static final AxisAlignedBB[] TRAY_BOUNDING_BOX = {TRAY_BOX_SOUTH, TRAY_BOX_WEST, TRAY_BOX_NORTH, TRAY_BOX_EAST};

    private static final Bounds PAPER_BOUNDS = new Bounds(13 * 0.0625, 0.0, 4 * 0.0625, 1.0, 9 * 0.0625, 12 * 0.0625);
    private static final AxisAlignedBB PAPER_BOX_NORTH = CollisionHelper.getBlockBounds(Direction.NORTH, PAPER_BOUNDS);
    private static final AxisAlignedBB PAPER_BOX_EAST = CollisionHelper.getBlockBounds(Direction.EAST, PAPER_BOUNDS);
    private static final AxisAlignedBB PAPER_BOX_SOUTH = CollisionHelper.getBlockBounds(Direction.SOUTH, PAPER_BOUNDS);
    private static final AxisAlignedBB PAPER_BOX_WEST = CollisionHelper.getBlockBounds(Direction.WEST, PAPER_BOUNDS);
    private static final AxisAlignedBB[] PAPER_BOUNDING_BOX = {PAPER_BOX_SOUTH, PAPER_BOX_WEST, PAPER_BOX_NORTH, PAPER_BOX_EAST};

    public BlockPrinter(DyeColor color) {
        super("printer", color);
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
//        return BODY_BOUNDING_BOX[state.get(FACING).getHorizontalIndex()];
//        return VoxelShapes.fullCube();
        return makeShape(state);
    }

    public VoxelShape makeShape(BlockState state) {
        Direction facing = state.get(FACING);
        VoxelShape SHAPE_1 = VoxelShapes.create(BODY_BOUNDING_BOX[facing.getHorizontalIndex()]);
        VoxelShape SHAPE_2 = VoxelShapes.create(TRAY_BOUNDING_BOX[facing.getHorizontalIndex()]);
        VoxelShape SHAPE_3 = VoxelShapes.create(PAPER_BOUNDING_BOX[facing.getHorizontalIndex()]);
        VoxelShape SHAPE = VoxelShapes.combine(SHAPE_1, SHAPE_2, IBooleanFunction.OR);
        return VoxelShapes.combine(SHAPE, SHAPE_3, IBooleanFunction.OR);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return false;
        } else {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            ItemStack heldItem = player.getHeldItem(handIn);

            if (tileentity instanceof TileEntityPrinter) {
                if (((TileEntityPrinter) tileentity).addPaper(heldItem, player.isSneaking())) {
                    return true;
                }
            }

            return true;
        }
    }

    @Override
    public boolean canRenderInLayer(BlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new TileEntityPrinter();
    }

}