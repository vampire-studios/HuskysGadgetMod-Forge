package io.github.vampirestudios.hgm.blocks;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

public class BlockColored extends Block implements ColoredBlock {

    public final DyeColor color;

    public BlockColored(String name, DyeColor color) {
        super(Properties.create(Material.ROCK));
        this.color = color;
        this.setRegistryName(new ResourceLocation(HuskysGadgetMod.MOD_ID, name + "_" + color.getName()));
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