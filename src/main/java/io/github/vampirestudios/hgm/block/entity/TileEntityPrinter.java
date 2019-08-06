package io.github.vampirestudios.hgm.block.entity;

import io.github.vampirestudios.hgm.Config;
import io.github.vampirestudios.hgm.api.print.IPrint;
import io.github.vampirestudios.hgm.block.BlockPrinter;
import io.github.vampirestudios.hgm.init.GadgetSounds;
import io.github.vampirestudios.hgm.init.GadgetTileEntities;
import io.github.vampirestudios.hgm.utils.CollisionHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayDeque;
import java.util.Deque;

public class TileEntityPrinter extends TileEntityNetworkDevice.Colored {
    private State state = State.IDLE;
    /**
     * The ItemStacks that hold the items currently being used in the furnace
     */
    private NonNullList<ItemStack> printerItemStacks = NonNullList.withSize(3, ItemStack.EMPTY);
    /**
     * The number of ticks that the furnace will keep burning
     */
    private int furnaceBurnTime;
    /**
     * The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for
     */
    private int printTime;
    private int totalPrintTime;
    private int inkLevels;
    private String furnaceCustomName;
    private Deque<IPrint> printQueue = new ArrayDeque<>();
    private IPrint currentPrint;
    private int remainingPrintTime;
    private int paperCount = 0;

    public TileEntityPrinter() {
        super(GadgetTileEntities.PRINTERS);
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory() {
        return this.printerItemStacks.size();
    }

    public boolean isEmpty() {
        for (ItemStack itemstack : this.printerItemStacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the stack in the given slot.
     */
    public ItemStack getStackInSlot(int index) {
        return this.printerItemStacks.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.printerItemStacks, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.printerItemStacks, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.printerItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.printerItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag) {
            this.totalPrintTime = this.getTotalPrintTime();
            this.printTime = 0;
            this.markDirty();
        }
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName() {
        return this.hasCustomName() ? this.furnaceCustomName : "container.printer";
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName() {
        return this.furnaceCustomName != null && !this.furnaceCustomName.isEmpty();
    }

    public void setCustomInventoryName(String p_145951_1_) {
        this.furnaceCustomName = p_145951_1_;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            if (remainingPrintTime > 0) {
                if (remainingPrintTime % 20 == 0 || state == State.LOADING_PAPER) {
                    pipeline.putInt("remainingPrintTime", remainingPrintTime);
                    sync();
                    if (remainingPrintTime != 0 && state == State.PRINTING) {
                        world.playSound(null, pos, GadgetSounds.PRINTER_PRINTING, SoundCategory.BLOCKS, 0.5F, 1.0F);
                    }
                }
                remainingPrintTime--;
            } else {
                setState(state.next());
            }
        }

        if (state == State.IDLE && remainingPrintTime == 0 && currentPrint != null) {
            if (!world.isRemote) {
                BlockState state = world.getBlockState(pos);
                double[] fixedPosition = CollisionHelper.fixRotation(state.get(BlockPrinter.FACING), 0.15, 0.5, 0.15, 0.5);
                ItemEntity entity = new ItemEntity(world, pos.getX() + fixedPosition[0], pos.getY() + 0.0625, pos.getZ() + fixedPosition[1], IPrint.generateItem(currentPrint));
                entity.setMotion(new Vec3d(0, 0, 0));
                world.addEntity(entity);
            }
            currentPrint = null;
        }

        if (state == State.IDLE && currentPrint == null && !printQueue.isEmpty() && paperCount > 0) {
            print(printQueue.poll());
        }
    }

    @Override
    public String getDeviceName() {
        return "Printer";
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        if (compound.contains("currentPrint", Constants.NBT.TAG_COMPOUND)) {
            currentPrint = IPrint.loadFromTag(compound.getCompound("currentPrint"));
        }
        if (compound.contains("totalPrintTime", Constants.NBT.TAG_INT)) {
            totalPrintTime = compound.getInt("totalPrintTime");
        }
        if (compound.contains("remainingPrintTime", Constants.NBT.TAG_INT)) {
            remainingPrintTime = compound.getInt("remainingPrintTime");
        }
        if (compound.contains("state", Constants.NBT.TAG_INT)) {
            state = State.values()[compound.getInt("state")];
        }
        if (compound.contains("paperCount", Constants.NBT.TAG_INT)) {
            paperCount = compound.getInt("paperCount");
        }
        if (compound.contains("queue", Constants.NBT.TAG_LIST)) {
            printQueue.clear();
            ListNBT queue = compound.getList("queue", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < queue.size(); i++) {
                IPrint print = IPrint.loadFromTag(queue.getCompound(i));
                printQueue.offer(print);
            }
        }
        this.printerItemStacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.printerItemStacks);
        this.printTime = compound.getInt("printTime");
        this.totalPrintTime = compound.getInt("printTimeTotal");
        this.inkLevels = compound.getInt("inkLevels");

        if (compound.contains("CustomName", 8)) {
            this.furnaceCustomName = compound.getString("CustomName");
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("totalPrintTime", totalPrintTime);
        compound.putInt("remainingPrintTime", remainingPrintTime);
        compound.putInt("state", state.ordinal());
        compound.putInt("paperCount", paperCount);
        if (currentPrint != null) {
            compound.put("currentPrint", IPrint.writeToTag(currentPrint));
        }
        if (!printQueue.isEmpty()) {
            ListNBT queue = new ListNBT();
            printQueue.forEach(print -> {
                queue.add(IPrint.writeToTag(print));
            });
            compound.put("queue", queue);
        }
        compound.putInt("printTime", (short) this.printTime);
        compound.putInt("inkLevels", (short) this.inkLevels);
        ItemStackHelper.saveAllItems(compound, this.printerItemStacks);

        if (this.hasCustomName()) {
            compound.putString("CustomName", this.furnaceCustomName);
        }
        return compound;
    }

    @Override
    public CompoundNBT writeSyncTag() {
        CompoundNBT tag = super.writeSyncTag();
        tag.putInt("paperCount", paperCount);
        return tag;
    }

    public void setState(State newState) {
        if (newState == null)
            return;

        state = newState;
        if (state == State.PRINTING) {
            if (Config.isOverridePrintSpeed()) {
                remainingPrintTime = Config.getCustomPrintSpeed() * 20;
            } else {
                remainingPrintTime = currentPrint.speed() * 20;
            }
        } else {
            remainingPrintTime = state.animationTime;
        }
        totalPrintTime = remainingPrintTime;

        pipeline.putInt("state", state.ordinal());
        pipeline.putInt("totalPrintTime", totalPrintTime);
        pipeline.putInt("remainingPrintTime", remainingPrintTime);
        sync();
    }

    public void addToQueue(IPrint print) {
        printQueue.offer(print);
    }

    private void print(IPrint print) {
        world.playSound(null, pos, GadgetSounds.PRINTER_LOADING_PAPER, SoundCategory.BLOCKS, 0.5F, 1.0F);

        setState(State.LOADING_PAPER);
        currentPrint = print;
        paperCount--;

        pipeline.putInt("paperCount", paperCount);
        pipeline.put("currentPrint", IPrint.writeToTag(currentPrint));
        sync();
    }

    public boolean isLoading() {
        return state == State.LOADING_PAPER;
    }

    public boolean isPrinting() {
        return state == State.PRINTING;
    }

    public int getTotalPrintTime() {
        return totalPrintTime;
    }

    public int getRemainingPrintTime() {
        return remainingPrintTime;
    }

    public boolean addPaper(ItemStack stack, boolean addAll) {
        if (!stack.isEmpty() && stack.getItem() == Items.PAPER && paperCount < Config.getMaxPaperCount()) {
            if (!addAll) {
                paperCount++;
                stack.shrink(1);
            } else {
                paperCount += stack.getCount();
                stack.setCount(Math.max(0, paperCount - 64));
                paperCount = Math.min(64, paperCount);
            }
            pipeline.putInt("paperCount", paperCount);
            sync();
            world.playSound(null, pos, SoundEvents.ENTITY_ITEM_FRAME_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return true;
        }
        return false;
    }

    public boolean hasPaper() {
        return paperCount > 0;
    }

    public int getPaperCount() {
        return paperCount;
    }

    public IPrint getPrint() {
        return currentPrint;
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    public void openInventory(PlayerEntity player) {
    }

    public void closeInventory(PlayerEntity player) {
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        if (direction == Direction.DOWN && index == 1) {
            Item item = stack.getItem();

            return item == Items.WATER_BUCKET || item == Items.BUCKET;
        }

        return true;
    }

    public String getGuiID() {
        return "hgm:printer";
    }

    public int getField(int id) {
        switch (id) {
            case 0:
                return this.printTime;
            case 1:
                return this.totalPrintTime;
            case 2:
                return this.inkLevels;
            default:
                return 0;
        }
    }

    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.printTime = value;
                break;
            case 1:
                this.totalPrintTime = value;
                break;
            case 2:
                this.inkLevels = value;
        }
    }

    public int getFieldCount() {
        return 4;
    }

    public void clear() {
        this.printerItemStacks.clear();
    }

    public enum State {
        LOADING_PAPER(30), PRINTING(0), IDLE(0);

        final int animationTime;

        State(int time) {
            this.animationTime = time;
        }

        public State next() {
            if (ordinal() + 1 >= values().length)
                return null;
            return values()[ordinal() + 1];
        }
    }
}