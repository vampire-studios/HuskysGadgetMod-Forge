package io.github.vampirestudios.hgm.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

public class BlockServerTerminal extends BlockDevice {

    public BlockServerTerminal() {
        super(Material.ANVIL, "server_rack");
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
//        this.setCreativeTab(HuskyGadgetMod.deviceDecoration);
    }

    /*@Nullable
    @Override
    public TileEntity createTileEntity(World world, BlockState state) {
        return new TileEntityServerTerminal();
    }*/

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state.with(FACING, context.getPlayer().getHorizontalFacing());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(FACING);
    }

}