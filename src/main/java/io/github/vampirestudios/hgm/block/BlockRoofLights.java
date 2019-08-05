package io.github.vampirestudios.hgm.block;

import io.github.vampirestudios.hgm.block.entity.TileEntityRoofLights;
import io.github.vampirestudios.hgm.utils.IColored;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockRoofLights extends BlockDecoration {

    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    public BlockRoofLights() {
        super(Material.ANVIL, "roof_lights");
        this.setDefaultState(getDefaultState().with(COLOR, DyeColor.WHITE));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityRoofLights();
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityRoofLights) {
            TileEntityRoofLights roofLights = (TileEntityRoofLights) tileEntity;

            CompoundNBT tileEntityTag = new CompoundNBT();
            roofLights.write(tileEntityTag);
            tileEntityTag.remove("pos");
            tileEntityTag.remove("color");
            tileEntityTag.remove("powered");
            tileEntityTag.remove("lightColour");

            CompoundNBT compound = new CompoundNBT();
            compound.put("BlockEntityTag", tileEntityTag);

            ItemStack drop = new ItemStack(Item.getItemFromBlock(this));
            drop.setTag(compound);

            worldIn.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state.with(FACING, context.getPlayer().getHorizontalFacing());
    }

    @Override
    public BlockState getStateAtViewpoint(BlockState state, IBlockReader world, BlockPos pos, Vec3d viewpoint) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof IColored) {
            IColored colorable = (IColored) tileEntity;
            state = state.with(COLOR, colorable.getColor());
        }
        return state;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(FACING, COLOR);
    }

}