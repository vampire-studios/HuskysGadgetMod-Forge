package io.github.vampirestudios.hgm.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockFacing extends BlockModContainer {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlockFacing(String name, Material materialIn) {
        super(name, materialIn);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public void onBlockAdded(BlockState thisState, World world, BlockPos pos, BlockState state2, boolean idk) {
        this.setDefaultFacing(world, pos, thisState);
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.with(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }

    private void setDefaultFacing(World worldIn, BlockPos pos, BlockState thisState) {
        if (!worldIn.isRemote) {
            BlockState state = worldIn.getBlockState(pos.north());
            BlockState state1 = worldIn.getBlockState(pos.south());
            BlockState state2 = worldIn.getBlockState(pos.west());
            BlockState state3 = worldIn.getBlockState(pos.east());
            Direction enumfacing = thisState.get(FACING);
            if (enumfacing == Direction.NORTH && state.isSolid() && !state1.isSolid()) {
                enumfacing = Direction.SOUTH;
            } else if (enumfacing == Direction.SOUTH && state1.isSolid() && !state.isSolid()) {
                enumfacing = Direction.NORTH;
            } else if (enumfacing == Direction.WEST && state2.isSolid() && !state3.isSolid()) {
                enumfacing = Direction.EAST;
            } else if (enumfacing == Direction.EAST && state3.isSolid() && !state2.isSolid()) {
                enumfacing = Direction.WEST;
            }

            worldIn.setBlockState(pos, thisState.with(FACING, enumfacing), 2);
        }

    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(FACING);
    }

}
