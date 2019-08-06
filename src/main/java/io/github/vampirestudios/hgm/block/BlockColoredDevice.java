package io.github.vampirestudios.hgm.block;

import io.github.vampirestudios.hgm.block.entity.TileEntityDevice;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public abstract class BlockColoredDevice extends BlockColoredFacing {

    public BlockColoredDevice(String name, DyeColor color) {
        super(name, color);
        this.setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext blockItemUseContext) {
        BlockState state = super.getStateForPlacement(blockItemUseContext);
        return state.with(FACING, blockItemUseContext.getPlayer().getHorizontalFacing());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityDevice) {
            TileEntityDevice tileEntityDevice = (TileEntityDevice) tileEntity;
            if (stack.hasDisplayName()) {
                tileEntityDevice.setCustomName(stack.getDisplayName().getFormattedText());
            }
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!worldIn.isRemote && player.abilities.isCreativeMode) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileEntityDevice) {
                TileEntityDevice device = (TileEntityDevice) tileEntity;

                CompoundNBT tileEntityTag = new CompoundNBT();
                tileEntity.write(tileEntityTag);
                tileEntityTag.remove("x");
                tileEntityTag.remove("y");
                tileEntityTag.remove("z");
                tileEntityTag.remove("id");

                CompoundNBT compound = new CompoundNBT();
                compound.put("BlockEntityTag", tileEntityTag);

                ItemStack drop;
                drop = new ItemStack(Item.getItemFromBlock(this));
                drop.setTag(compound);

                if (device.hasCustomName()) {
                    drop.setDisplayName(new StringTextComponent(device.getCustomName()));
                }

                worldIn.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));
            }
        }
    }

    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

}
