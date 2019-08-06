package io.github.vampirestudios.hgm.item;

import io.github.vampirestudios.hgm.Config;
import io.github.vampirestudios.hgm.block.entity.TileEntityNetworkDevice;
import io.github.vampirestudios.hgm.block.entity.TileEntityRouter;
import io.github.vampirestudios.hgm.core.network.Router;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SChatPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Objects;

public class ItemEthernetCable extends BaseItem {
    public ItemEthernetCable() {
        super("ethernet_cable", new Properties().maxStackSize(1));
    }

    private static double getDistance(BlockPos source, BlockPos target) {
        return Math.sqrt(source.distanceSq(new Vec3i(target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5)));
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        if (!context.getWorld().isRemote) {
            ItemStack heldItem = context.getPlayer().getHeldItem(context.getHand());
            TileEntity tileEntity = context.getWorld().getTileEntity(context.getPos());

            if (tileEntity instanceof TileEntityRouter) {
                if (!heldItem.hasTag()) {
                    sendGameInfoMessage(context.getPlayer(), "message.invalid_cable");
                    return ActionResultType.SUCCESS;
                }

                TileEntityRouter tileEntityRouter = (TileEntityRouter) tileEntity;
                Router router = tileEntityRouter.getRouter();

                CompoundNBT tag = heldItem.getTag();
                BlockPos devicePos = BlockPos.fromLong(tag.getLong("pos"));

                TileEntity tileEntity1 = context.getWorld().getTileEntity(devicePos);
                if (tileEntity1 instanceof TileEntityNetworkDevice) {
                    TileEntityNetworkDevice TileEntityNetworkDevice = (io.github.vampirestudios.hgm.block.entity.TileEntityNetworkDevice) tileEntity1;
                    if (!router.hasDevice(TileEntityNetworkDevice)) {
                        if (router.addDevice(TileEntityNetworkDevice)) {
                            TileEntityNetworkDevice.connect(router);
                            heldItem.shrink(1);
                            if (getDistance(tileEntity1.getPos(), tileEntityRouter.getPos()) > Config.getSignalRange()) {
                                sendGameInfoMessage(context.getPlayer(), "message.successful_registered");
                            } else {
                                sendGameInfoMessage(context.getPlayer(), "message.successful_connection");
                            }
                        } else {
                            sendGameInfoMessage(context.getPlayer(), "message.router_max_devices");
                        }
                    } else {
                        sendGameInfoMessage(context.getPlayer(), "message.device_already_connected");
                    }
                } else {
                    if (router.addDevice(tag.getUniqueId("id"), tag.getString("name"))) {
                        heldItem.shrink(1);
                        sendGameInfoMessage(context.getPlayer(), "message.successful_registered");
                    } else {
                        sendGameInfoMessage(context.getPlayer(), "message.router_max_devices");
                    }
                }
                return ActionResultType.SUCCESS;
            }

            if (tileEntity instanceof TileEntityNetworkDevice) {
                TileEntityNetworkDevice TileEntityNetworkDevice = (TileEntityNetworkDevice) tileEntity;
                if (!heldItem.hasTag()) {
                    heldItem.setTag(new CompoundNBT());
                }
                CompoundNBT tag = heldItem.getTag();
                Objects.requireNonNull(tag).putUniqueId("id", TileEntityNetworkDevice.getId());
                tag.putString("name", TileEntityNetworkDevice.getDeviceName());
                tag.putLong("pos", TileEntityNetworkDevice.getPos().toLong());
                heldItem.setDisplayName(new StringTextComponent(TextFormatting.GRAY.toString() + TextFormatting.BOLD.toString() + I18n.format("item.ethernet_cable.name")));

                sendGameInfoMessage(context.getPlayer(), "message.select_router");
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.SUCCESS;
    }

    private void sendGameInfoMessage(PlayerEntity player, String message) {
        if (player instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity) player).connection.sendPacket(new SChatPacket(new TranslationTextComponent(message), ChatType.GAME_INFO));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote) {
            ItemStack heldItem = playerIn.getHeldItem(handIn);
            if (playerIn.isSneaking()) {
                heldItem.clearCustomName();
                heldItem.setTag(null);
                return ActionResult.newResult(ActionResultType.SUCCESS, heldItem);
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
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
            if (!Screen.hasShiftDown()) {
                tooltip.add(new StringTextComponent(TextFormatting.GRAY.toString() + "Use this cable to connect"));
                tooltip.add(new StringTextComponent(TextFormatting.GRAY.toString() + "a device to a router."));
                tooltip.add(new StringTextComponent(TextFormatting.YELLOW.toString() + "Hold SHIFT for How-To"));
                return;
            }

            tooltip.add(new StringTextComponent(TextFormatting.GRAY.toString() + "Start by right clicking a"));
            tooltip.add(new StringTextComponent(TextFormatting.GRAY.toString() + "device with this cable"));
            tooltip.add(new StringTextComponent(TextFormatting.GRAY.toString() + "then right click the "));
            tooltip.add(new StringTextComponent(TextFormatting.GRAY.toString() + "router you want to"));
            tooltip.add(new StringTextComponent(TextFormatting.GRAY.toString() + "connect this device to."));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean hasEffect(ItemStack p_77636_1_) {
        return p_77636_1_.hasTag();
    }
}
