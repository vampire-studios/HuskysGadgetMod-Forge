package io.github.vampirestudios.hgm.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemUSBCable extends BaseItem {
    public ItemUSBCable() {
        super("usb_cable");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World p_77624_2_, List<ITextComponent> tooltip, ITooltipFlag p_77624_4_) {
        if (stack.hasTag()) {
            CompoundNBT tag = stack.getTag();
            if (tag != null) {
                tooltip.add(new StringTextComponent(TextFormatting.RED.toString() + TextFormatting.BOLD.toString() + "ID: " + TextFormatting.RESET.toString() + tag.getUniqueId("id")));
                tooltip.add(new StringTextComponent(TextFormatting.RED.toString() + TextFormatting.BOLD.toString() + "Device: " + TextFormatting.RESET.toString() + tag.getString("name")));

                BlockPos devicePos = BlockPos.fromLong(tag.getLong("pos"));
                StringBuilder builder = new StringBuilder();
                builder.append(TextFormatting.RED.toString() + TextFormatting.BOLD.toString() + "X: " + TextFormatting.RESET.toString() + devicePos.getX() + " ");
                builder.append(TextFormatting.RED.toString() + TextFormatting.BOLD.toString() + "Y: " + TextFormatting.RESET.toString() + devicePos.getY() + " ");
                builder.append(TextFormatting.RED.toString() + TextFormatting.BOLD.toString() + "Z: " + TextFormatting.RESET.toString() + devicePos.getZ());
                tooltip.add(new StringTextComponent(builder.toString()));
            }
        } else {
//            if (!GuiScreen.isShiftKeyDown()) {
//                tooltip.add(new StringTextComponent(TextFormatting.GRAY.toString() + "Use this cable to connect"));
//                tooltip.add(new StringTextComponent(TextFormatting.GRAY.toString() + "a device to either a drawing tablet or a server terminal."));
//                tooltip.add(TextFormatting.YELLOW.toString() + "Hold SHIFT for How-To");
//                return;
//            }

            tooltip.add(new StringTextComponent(TextFormatting.GRAY.toString() + "Start by right clicking a"));
            tooltip.add(new StringTextComponent(TextFormatting.GRAY.toString() + "device with this cable"));
            tooltip.add(new StringTextComponent(TextFormatting.GRAY.toString() + "then right click the "));
            tooltip.add(new StringTextComponent(TextFormatting.GRAY.toString() + "either the drawing tablet or server terminal "));
            tooltip.add(new StringTextComponent(TextFormatting.GRAY.toString() + "you want to connect this device to."));
        }
        super.addInformation(stack, p_77624_2_, tooltip, p_77624_4_);
    }
}
