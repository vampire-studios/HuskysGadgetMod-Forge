package io.github.vampirestudios.hgm.api.print;

import io.github.vampirestudios.hgm.init.GadgetBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public interface IPrint {
    static CompoundNBT writeToTag(IPrint print) {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("type", PrintingManager.getPrintIdentifier(print));
        tag.put("data", print.toTag());
        return tag;
    }

    @Nullable
    static IPrint loadFromTag(CompoundNBT tag) {
        IPrint print = PrintingManager.getPrint(tag.getString("type"));
        if (print != null) {
            print.fromTag(tag.getCompound("data"));
            return print;
        }
        return null;
    }

    static ItemStack generateItem(IPrint print) {
        CompoundNBT blockEntityTag = new CompoundNBT();
        blockEntityTag.put("print", writeToTag(print));

        CompoundNBT itemTag = new CompoundNBT();
        itemTag.put("BlockEntityTag", blockEntityTag);

        ItemStack stack = new ItemStack(GadgetBlocks.PAPER);
        stack.setTag(itemTag);

        if (print.getName() != null && !print.getName().isEmpty()) {
            stack.setDisplayName(new StringTextComponent(print.getName()));
        }
        return stack;
    }

    String getName();

    /**
     * Gets the speed of the print. The higher the value, the longer it will take to print.
     *
     * @return the speed of this print
     */
    int speed();

    /**
     * Gets whether or not this print requires coloured ink.
     *
     * @return if print requires ink
     */
    boolean requiresColor();

    /**
     * Converts print into an NBT tag compound. Used for the renderer.
     *
     * @return nbt form of print
     */
    CompoundNBT toTag();

    void fromTag(CompoundNBT tag);

    @OnlyIn(Dist.CLIENT)
    Class<? extends Renderer> getRenderer();

    interface Renderer {
        boolean render(CompoundNBT data);
    }
}