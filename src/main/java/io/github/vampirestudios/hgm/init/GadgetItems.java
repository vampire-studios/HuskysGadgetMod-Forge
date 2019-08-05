package io.github.vampirestudios.hgm.init;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.block.ColoredBlock;
import io.github.vampirestudios.hgm.item.BaseItem;
import io.github.vampirestudios.hgm.item.ItemMotherBoard;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "hgm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class GadgetItems {

    public static final Food EASTER_EGG = new Food.Builder().saturation(0.3F).hunger(4).fastToEat().setAlwaysEdible()
            .effect(new EffectInstance(Effects.SPEED, 300, 2), 1.0F)
            .effect(new EffectInstance(Effects.REGENERATION, 300, 1), 1.0F)
            .effect(new EffectInstance(Effects.SATURATION, 300, 10), 1.0F)
            .build();

    public static final Item EASTER_EGG_ITEM = new Item(new Item.Properties().food(EASTER_EGG))
            .setRegistryName(new ResourceLocation(HuskysGadgetMod.MOD_ID, "easter_egg_item"));
    public static final Item MOTHERBOARD = new ItemMotherBoard();
    public static final ItemMotherBoard.Component CPU = new ItemMotherBoard.Component("cpu");
    public static final ItemMotherBoard.Component RAM_STICKS = new ItemMotherBoard.Component("ram_stick");
    public static final ItemMotherBoard.Component WIFI_CARD = new ItemMotherBoard.Component("wifi_card");
    public static final ItemMotherBoard.Component GPU = new ItemMotherBoard.Component("gpu");

    public static final Item CAMERA = new BaseItem("camera");
    public static final Item CD = new BaseItem("cd");
    public static final Item DVD = new BaseItem("dvd");
    public static final Item[] FLASH_DRIVES = new Item[16];

    public static final Item ID_CARD = new BaseItem("id_card");
    public static final Item WHITE_WIIU_GAMEPAD = new BaseItem("white_wiiu_gamepad");
    public static final Item BLACK_WIIU_GAMEPAD = new BaseItem("black_wiiu_gamepad");

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
        for (DyeColor color : DyeColor.values()) {
            FLASH_DRIVES[color.getId()] = new BaseItem("flash_drive_" + color.getName().toLowerCase());
        }
        event.getRegistry().registerAll(
                EASTER_EGG_ITEM,
                MOTHERBOARD,
                CPU,
                RAM_STICKS,
                WIFI_CARD,
                GPU,
                CAMERA,
                CD,
                DVD,
                ID_CARD,
                WHITE_WIIU_GAMEPAD,
                BLACK_WIIU_GAMEPAD
        );
        event.getRegistry().registerAll(FLASH_DRIVES);
    }

}
