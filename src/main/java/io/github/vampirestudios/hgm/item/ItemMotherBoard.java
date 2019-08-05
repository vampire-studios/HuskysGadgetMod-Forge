package io.github.vampirestudios.hgm.item;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.List;

public class ItemMotherBoard extends Item {

    public ItemMotherBoard() {
        super(new Properties().group(HuskysGadgetMod.deviceItems));
        this.setRegistryName(HuskysGadgetMod.MOD_ID, "mother_board");
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT tag = stack.getTag();
        if (!Screen.hasShiftDown()) {
            tooltip.add(new StringTextComponent("CPU: " + getComponentStatus(tag, "cpu")));
            tooltip.add(new StringTextComponent("RAM: " + getComponentStatus(tag, "ram")));
            tooltip.add(new StringTextComponent("GPU: " + getComponentStatus(tag, "gpu")));
            tooltip.add(new StringTextComponent("WIFI: " + getComponentStatus(tag, "wifi")));
            tooltip.add(new StringTextComponent(TextFormatting.YELLOW + "Hold shift for help"));
        } else {
            tooltip.add(new StringTextComponent("To add the required components"));
            tooltip.add(new StringTextComponent("place the motherboard and the"));
            tooltip.add(new StringTextComponent("corresponding component into a"));
            tooltip.add(new StringTextComponent("crafting table to combine them."));
        }
    }

    private String getComponentStatus(CompoundNBT tag, String component) {
        if (tag != null && tag.contains("components", Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT components = tag.getCompound("components");
            if (components.contains(component, Constants.NBT.TAG_BYTE)) {
                return TextFormatting.GREEN + "Added";
            }
        }
        return TextFormatting.RED + "Missing";
    }

    public static class Component extends ItemComponent {
        public Component(String id) {
            super(id);
        }
    }

    public static class ColoredComponent extends ItemColoredComponent {
        public ColoredComponent(String id, DyeColor color) {
            super(id, color);
        }
    }

}
