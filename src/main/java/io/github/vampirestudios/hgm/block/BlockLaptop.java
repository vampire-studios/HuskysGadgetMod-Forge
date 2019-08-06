package io.github.vampirestudios.hgm.block;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.block.entity.TileEntityLaptop;
import io.github.vampirestudios.hgm.core.Laptop;
import io.github.vampirestudios.hgm.object.Bounds;
import io.github.vampirestudios.hgm.utils.TileEntityUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class BlockLaptop extends BlockColoredDevice {

    public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);

    private static final AxisAlignedBB[] SCREEN_BOXES = new Bounds(13 * 0.0625, 0.0625, 1 * 0.0625, 1.0, 12 * 0.0625, 0.9375).getRotatedBounds();
    private static final AxisAlignedBB BODY_OPEN_BOX = new AxisAlignedBB(1 * 0.0625, 0.0, 1 * 0.0625, 13 * 0.0625, 1 * 0.0625, 15 * 0.0625);
    private static final AxisAlignedBB BODY_CLOSED_BOX = new AxisAlignedBB(1 * 0.0625, 0.0, 1 * 0.0625, 13 * 0.0625, 2 * 0.0625, 15 * 0.0625);
    private static final AxisAlignedBB SELECTION_BOX_OPEN = new AxisAlignedBB(0, 0, 0, 1, 12 * 0.0625, 1);
    private static final AxisAlignedBB SELECTION_BOX_CLOSED = new AxisAlignedBB(0, 0, 0, 1, 3 * 0.0625, 1);

    public BlockLaptop(DyeColor color) {
        super("laptop", color);
        this.setDefaultState(getDefaultState().with(FACING, Direction.NORTH).with(TYPE, Type.BASE));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityLaptop) {
            TileEntityLaptop laptop = (TileEntityLaptop) tileEntity;
            if (laptop.isOpen()) {
                VoxelShape SHAPE_1 = VoxelShapes.create(BODY_OPEN_BOX);
                VoxelShape SHAPE_2 = VoxelShapes.create(SCREEN_BOXES[state.get(FACING).getHorizontalIndex()]);
                return VoxelShapes.combine(SHAPE_1, SHAPE_2, IBooleanFunction.OR);
            } else {
                return VoxelShapes.create(BODY_CLOSED_BOX);
            }
        }
        return VoxelShapes.fullCube();
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityLaptop) {
            TileEntityLaptop laptop = (TileEntityLaptop) tileEntity;

            if (player.isSneaking()) {
                if (!worldIn.isRemote) {
                    laptop.openClose();
                }
            } if (!laptop.isOpen() && player.isSneaking() && Screen.hasControlDown()) {
                if (!worldIn.isRemote) {
                    laptop.powerUnpower();
                }
            } else {
                /*if (side == state.get(FACING).rotateYCCW()) {
                    ItemStack heldItem = player.getHeldItem(handIn);
                    if (!heldItem.isEmpty() && (heldItem.getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation(HuskysGadgetMod.MOD_ID, "flash_drive_" + getDyeColor().getName())))) {
                        if (!worldIn.isRemote) {
                            if (laptop.getFileSystem().setAttachedDrive(heldItem.copy())) {
                                heldItem.shrink(1);
                                TileEntityUtil.markBlockForUpdate(worldIn, pos);
                            } else {
                                player.sendMessage(new StringTextComponent("No more available USB slots!"));
                            }
                        }
                    }

                    if (!worldIn.isRemote) {
                        ItemStack stack = laptop.getFileSystem().removeAttachedDrive();
                        if (stack != null) {
                            BlockPos summonPos = pos.offset(state.get(FACING).rotateYCCW());
                            worldIn.addEntity(new ItemEntity(worldIn, summonPos.getX() + 0.5, summonPos.getY(), summonPos.getZ() + 0.5, stack));
                            TileEntityUtil.markBlockForUpdate(worldIn, pos);
                        }
                    }
                    return true;
                }*/
                ItemStack heldItem = player.getHeldItem(handIn);
                if (!heldItem.isEmpty() && (heldItem.getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation(HuskysGadgetMod.MOD_ID, "flash_drive_" + getDyeColor().getName())))) {
                    if (!worldIn.isRemote) {
                        if (laptop.getFileSystem().setAttachedDrive(heldItem.copy())) {
                            heldItem.shrink(1);
                            TileEntityUtil.markBlockForUpdate(worldIn, pos);
                        } else {
                            player.sendMessage(new StringTextComponent("No more available USB slots!"));
                        }
                    }
                }

                if (!worldIn.isRemote) {
                    ItemStack stack = laptop.getFileSystem().removeAttachedDrive();
                    if (stack != null) {
                        BlockPos summonPos = pos.offset(state.get(FACING).rotateYCCW());
                        worldIn.addEntity(new ItemEntity(worldIn, summonPos.getX() + 0.5, summonPos.getY(), summonPos.getZ() + 0.5, stack));
                        TileEntityUtil.markBlockForUpdate(worldIn, pos);
                    }
                }

                /*if(laptop.isPowered()) {
                    if (laptop.isOpen() && worldIn.isRemote) {
//                        player.openGui(HuskyGadgetMod.instance, Laptop.ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
                    }
                }*/
                Minecraft.getInstance().displayGuiScreen(new Laptop());
            }

            if(!laptop.isPowered()) {
                if (laptop.isOpen() && worldIn.isRemote) {
                    player.sendStatusMessage(new StringTextComponent("The laptop is not powered. To power it do: CTRL + Shift + Right Click it"), true);
                }
            }

        }
        return true;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBlockHarvested(worldIn, pos, state, player);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityLaptop) {
            TileEntityLaptop laptop = (TileEntityLaptop) tileEntity;

            CompoundNBT tileEntityTag = new CompoundNBT();
            laptop.write(tileEntityTag);
            tileEntityTag.remove("open");
            tileEntityTag.remove("powered");

            CompoundNBT compound = new CompoundNBT();
            compound.put("BlockEntityTag", tileEntityTag);

            ItemStack drop = new ItemStack(Item.getItemFromBlock(this));
            drop.setTag(compound);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext blockItemUseContext) {
        BlockState state = super.getStateForPlacement(blockItemUseContext);
        return state.with(FACING, blockItemUseContext.getPlayer().getAdjustedHorizontalFacing());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(FACING, TYPE);
    }

    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityLaptop();
    }

    public enum Type implements IStringSerializable {
        BASE, SCREEN;

        @Override
        public String getName() {
            return name().toLowerCase();
        }

    }

}