package io.github.vampirestudios.hgm.block;

import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockColoredModContainer extends ContainerBlock implements ColoredBlock {

    private DyeColor color;

    public BlockColoredModContainer(String name, DyeColor color) {
        super(Properties.create(Material.ROCK));
        this.color = color;
    }

    @Override
    public DyeColor getDyeColor() {
        return color;
    }

    @Override
    public void addInformation(ItemStack p_190948_1_, @Nullable IBlockReader p_190948_2_, List<ITextComponent> p_190948_3_, ITooltipFlag p_190948_4_) {
        this.addInformation(p_190948_3_);
    }

}