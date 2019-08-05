package io.github.vampirestudios.hgm.block;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.block.entity.TileEntityEasterEgg;
import io.github.vampirestudios.hgm.init.GadgetItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockEasterEgg extends Block {

    public BlockEasterEgg() {
        super(Properties.create(Material.CARPET).hardnessAndResistance(1.0F, 1.0F));
        this.setRegistryName(new ResourceLocation(HuskysGadgetMod.MOD_ID, "easter_egg"));
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileEntityEasterEgg) {
                TileEntityEasterEgg eggte = (TileEntityEasterEgg) te;
                ItemStack egg = new ItemStack(GadgetItems.EASTER_EGG_ITEM);
                CompoundNBT nbt = eggte.writeColorsToNBT(new CompoundNBT());
                egg.setTag(nbt);
                worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), egg));
                System.out.println("Breaking block");
            }
            worldIn.destroyBlock(pos, false);
        }

    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityEasterEgg();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

}
