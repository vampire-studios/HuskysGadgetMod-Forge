package io.github.vampirestudios.hgm.block;

import io.github.vampirestudios.hgm.block.entity.TileEntityRouter;
import io.github.vampirestudios.hgm.network.PacketHandler;
import io.github.vampirestudios.hgm.network.task.MessageSyncBlock;
import io.github.vampirestudios.hgm.object.Bounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockRouter extends BlockColoredDevice {

    public static final BooleanProperty VERTICAL = BooleanProperty.create("vertical");

    private static final VoxelShape[] BODY_BOUNDING_BOX = new Bounds(4, 0, 2, 12, 2, 14).getRotatedBounds();
    private static final VoxelShape[] BODY_VERTICAL_BOUNDING_BOX = new Bounds(14, 1, 2, 16, 9, 14).getRotatedBounds();
    private static final VoxelShape[] SELECTION_BOUNDING_BOX = new Bounds(3, 0, 1, 13, 3, 15).getRotatedBounds();
    private static final VoxelShape[] SELECTION_VERTICAL_BOUNDING_BOX = new Bounds(13, 0, 1, 16, 10, 15).getRotatedBounds();

    public BlockRouter(DyeColor color) {
        super("router", color);
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH).with(VERTICAL, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (state.get(VERTICAL)) {
            return SELECTION_VERTICAL_BOUNDING_BOX[state.get(FACING).getHorizontalIndex()];
        }
        return SELECTION_BOUNDING_BOX[state.get(FACING).getHorizontalIndex()];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return this.getShape(state, worldIn, pos, context);
    }

    @Override
    public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        if (state.get(VERTICAL)) {
            return SELECTION_VERTICAL_BOUNDING_BOX[state.get(FACING).getHorizontalIndex()];
        }
        return SELECTION_BOUNDING_BOX[state.get(FACING).getHorizontalIndex()];
    }

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        if (state.get(VERTICAL)) {
            return SELECTION_VERTICAL_BOUNDING_BOX[state.get(FACING).getHorizontalIndex()];
        }
        return SELECTION_BOUNDING_BOX[state.get(FACING).getHorizontalIndex()];
    }

    @Override
    public boolean canRenderInLayer(BlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote && player.abilities.isCreativeMode) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileEntityRouter) {
                TileEntityRouter tileEntityRouter = (TileEntityRouter) tileEntity;
                tileEntityRouter.setDebug();
                if (tileEntityRouter.isDebug()) {
                    PacketHandler.INSTANCE.sendToServer(new MessageSyncBlock(pos));
                }
            }
            return true;
        }
        return true;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!worldIn.isRemote && !player.abilities.isCreativeMode) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileEntityRouter) {
                TileEntityRouter router = (TileEntityRouter) tileEntity;

                CompoundNBT tileEntityTag = new CompoundNBT();
                router.write(tileEntityTag);
                tileEntityTag.remove("x");
                tileEntityTag.remove("y");
                tileEntityTag.remove("z");
                tileEntityTag.remove("id");

                CompoundNBT compound = new CompoundNBT();
                compound.put("BlockEntityTag", tileEntityTag);

                ItemStack drop = new ItemStack(Item.getItemFromBlock(this));
                drop.setTag(compound);

                worldIn.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext blockItemUseContext) {
        BlockState state = super.getStateForPlacement(blockItemUseContext);
        return state.with(FACING, blockItemUseContext.getFace().getAxis() == Direction.Axis.Y ? blockItemUseContext.getPlayer().getHorizontalFacing() :
                blockItemUseContext.getFace().getOpposite()).with(VERTICAL, blockItemUseContext.getFace().getAxis() != Direction.Axis.Y);
    }

    /*@Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return side != EnumFacing.DOWN;
    }*/

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityRouter();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(FACING, VERTICAL);
    }

}
