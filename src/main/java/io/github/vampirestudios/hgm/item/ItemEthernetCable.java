package io.github.vampirestudios.hgm.item;

import javafx.geometry.Side;
import net.minecraft.item.ItemStack;

public class ItemEthernetCable extends BaseItem {
    public ItemEthernetCable() {
        super("ethernet_cable", new Properties().maxStackSize(1));
    }

//    private static double getDistance(BlockPos source, BlockPos target) {
//        return Math.sqrt(source.distanceSqToCenter(target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5));
//    }
//
//    @Override
//    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
//        if (!world.isRemote) {
//            ItemStack heldItem = player.getHeldItem(hand);
//            TileEntity tileEntity = world.getTileEntity(pos);
//
//            if (tileEntity instanceof TileEntityRouter) {
//                if (!heldItem.hasTagCompound()) {
//                    sendGameInfoMessage(player, "message.invalid_cable");
//                    return EnumActionResult.SUCCESS;
//                }
//
//                TileEntityRouter tileEntityRouter = (TileEntityRouter) tileEntity;
//                Router router = tileEntityRouter.getRouter();
//
//                NBTTagCompound tag = heldItem.getTagCompound();
//                BlockPos devicePos = BlockPos.fromLong(tag.getLong("pos"));
//
//                TileEntity tileEntity1 = world.getTileEntity(devicePos);
//                if (tileEntity1 instanceof TileEntityNetworkDevice) {
//                    TileEntityNetworkDevice TileEntityNetworkDevice = (TileEntityNetworkDevice) tileEntity1;
//                    if (!router.hasDevice(TileEntityNetworkDevice)) {
//                        if (router.addDevice(TileEntityNetworkDevice)) {
//                            TileEntityNetworkDevice.connect(router);
//                            heldItem.shrink(1);
//                            if (getDistance(tileEntity1.getPos(), tileEntityRouter.getPos()) > DeviceConfig.getSignalRange()) {
//                                sendGameInfoMessage(player, "message.successful_registered");
//                            } else {
//                                sendGameInfoMessage(player, "message.successful_connection");
//                            }
//                        } else {
//                            sendGameInfoMessage(player, "message.router_max_devices");
//                        }
//                    } else {
//                        sendGameInfoMessage(player, "message.device_already_connected");
//                    }
//                } else {
//                    if (router.addDevice(tag.getUniqueId("id"), tag.getString("name"))) {
//                        heldItem.shrink(1);
//                        sendGameInfoMessage(player, "message.successful_registered");
//                    } else {
//                        sendGameInfoMessage(player, "message.router_max_devices");
//                    }
//                }
//                return EnumActionResult.SUCCESS;
//            }
//
//            if (tileEntity instanceof TileEntityNetworkDevice) {
//                TileEntityNetworkDevice TileEntityNetworkDevice = (TileEntityNetworkDevice) tileEntity;
//                if (!heldItem.hasTagCompound()) {
//                    heldItem.setTagCompound(new NBTTagCompound());
//                }
//                NBTTagCompound tag = heldItem.getTagCompound();
//                Objects.requireNonNull(tag).setUniqueId("id", TileEntityNetworkDevice.getId());
//                tag.setString("name", TileEntityNetworkDevice.getDeviceName());
//                tag.setLong("pos", TileEntityNetworkDevice.getPos().toLong());
//                heldItem.setStackDisplayName(TextFormatting.GRAY.toString() + TextFormatting.BOLD.toString() + I18n.format("item.ethernet_cable.name"));
//
//                sendGameInfoMessage(player, "message.select_router");
//                return EnumActionResult.SUCCESS;
//            }
//        }
//        return EnumActionResult.SUCCESS;
//    }
//
//    private void sendGameInfoMessage(EntityPlayer player, String message) {
//        if (player instanceof EntityPlayerMP) {
//            ((EntityPlayerMP) player).connection.sendPacket(new SPacketChat(new TextComponentTranslation(message), ChatType.GAME_INFO));
//        }
//    }
//
//    @Override
//    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
//        if (!world.isRemote) {
//            ItemStack heldItem = player.getHeldItem(hand);
//            if (player.isSneaking()) {
//                heldItem.clearCustomName();
//                heldItem.setTagCompound(null);
//                return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
//            }
//        }
//        return super.onItemRightClick(world, player, hand);
//    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
//        if (stack.hasTagCompound()) {
//            NBTTagCompound tag = stack.getTagCompound();
//            if (tag != null) {
//                tooltip.add(TextFormatting.RED.toString() + TextFormatting.BOLD.toString() + "ID: " + TextFormatting.RESET.toString() + tag.getUniqueId("id"));
//                tooltip.add(TextFormatting.RED.toString() + TextFormatting.BOLD.toString() + "Device: " + TextFormatting.RESET.toString() + tag.getString("name"));
//
//                BlockPos devicePos = BlockPos.fromLong(tag.getLong("pos"));
//                StringBuilder builder = new StringBuilder();
//                builder.append(TextFormatting.RED.toString() + TextFormatting.BOLD.toString() + "X: " + TextFormatting.RESET.toString() + devicePos.getX() + " ");
//                builder.append(TextFormatting.RED.toString() + TextFormatting.BOLD.toString() + "Y: " + TextFormatting.RESET.toString() + devicePos.getY() + " ");
//                builder.append(TextFormatting.RED.toString() + TextFormatting.BOLD.toString() + "Z: " + TextFormatting.RESET.toString() + devicePos.getZ());
//                tooltip.add(builder.toString());
//            }
//        } else {
//            if (!GuiScreen.isShiftKeyDown()) {
//                tooltip.add(TextFormatting.GRAY.toString() + "Use this cable to connect");
//                tooltip.add(TextFormatting.GRAY.toString() + "a device to a router.");
//                tooltip.add(TextFormatting.YELLOW.toString() + "Hold SHIFT for How-To");
//                return;
//            }
//
//            tooltip.add(TextFormatting.GRAY.toString() + "Start by right clicking a");
//            tooltip.add(TextFormatting.GRAY.toString() + "device with this cable");
//            tooltip.add(TextFormatting.GRAY.toString() + "then right click the ");
//            tooltip.add(TextFormatting.GRAY.toString() + "router you want to");
//            tooltip.add(TextFormatting.GRAY.toString() + "connect this device to.");
//        }
//        super.addInformation(stack, worldIn, tooltip, flagIn);
//    }

    @Override
    public boolean hasEffect(ItemStack p_77636_1_) {
        return p_77636_1_.hasTag();
    }
}
