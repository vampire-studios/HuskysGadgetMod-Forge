package io.github.vampirestudios.hgm.block;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.object.Bounds;
import io.github.vampirestudios.hgm.utils.CollisionHelper;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockElectricSecurityFence extends Block {

    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");

    private static final AxisAlignedBB[] BOUNDING_BOX = new AxisAlignedBB[] { new AxisAlignedBB(0.4375, 0.0, 0.4375, 0.5625, 1.0, 0.5625), new AxisAlignedBB(0.4375, 0.0, 0.4375, 0.5625, 1.0, 1.0), new AxisAlignedBB(0.0, 0.0, 0.4375, 0.5625, 1.0, 0.5625), new AxisAlignedBB(0.0, 0.0, 0.4375, 0.5625, 1.0, 1.0), new AxisAlignedBB(0.4375, 0.0, 0.0, 0.5625, 1.0, 0.5625), new AxisAlignedBB(0.4375, 0.0, 0.0, 0.5625, 1.0, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 0.5625, 1.0, 0.5625), new AxisAlignedBB(0.0, 0.0, 0.0, 0.5625, 1.0, 1.0), new AxisAlignedBB(0.4375, 0.0, 0.4375, 1.0, 1.0, 0.5625), new AxisAlignedBB(0.4375, 0.0, 0.4375, 1.0, 1.0, 1.0), new AxisAlignedBB(0.0, 0.0, 0.4375, 1.0, 1.0, 0.5625), new AxisAlignedBB(0.0, 0.0, 0.4375, 1.0, 1.0, 1.0), new AxisAlignedBB(0.4375, 0.0, 0.0, 1.0, 1.0, 0.5625), new AxisAlignedBB(0.4375, 0.0, 0.0, 1.0, 1.0, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.5625), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0) };

    private static final VoxelShape COLLISION_BOX_CENTER = VoxelShapes.create(new AxisAlignedBB(0.4375, 0.0, 0.4375, 0.5625, 1.0, 0.5625));
    private static final VoxelShape COLLISION_BOX_NORTH = VoxelShapes.create(CollisionHelper.getBlockBounds(Direction.NORTH, new Bounds(0.5625, 0.0, 0.4375, 1.0, 1.0, 0.5625)));
    private static final VoxelShape COLLISION_BOX_EAST = VoxelShapes.create(CollisionHelper.getBlockBounds(Direction.EAST, new Bounds(0.5625, 0.0, 0.4375, 1.0, 1.0, 0.5625)));
    private static final VoxelShape COLLISION_BOX_SOUTH = VoxelShapes.create(CollisionHelper.getBlockBounds(Direction.SOUTH, new Bounds(0.5625, 0.0, 0.4375, 1.0, 1.0, 0.5625)));
    private static final VoxelShape COLLISION_BOX_WEST = VoxelShapes.create(CollisionHelper.getBlockBounds(Direction.WEST, new Bounds(0.5625, 0.0, 0.4375, 1.0, 1.0, 0.5625)));

    private static final VoxelShape[] COLLISION_BOXES = new VoxelShape[] {COLLISION_BOX_SOUTH,COLLISION_BOX_WEST,COLLISION_BOX_NORTH,COLLISION_BOX_EAST};

    protected static DamageSource electricFence = new DamageSource("electricity");

    public BlockElectricSecurityFence() {
        super(Properties.create(Material.IRON).lightValue(2).sound(SoundType.ANVIL).hardnessAndResistance(1.0F));
        this.setRegistryName(HuskysGadgetMod.MOD_ID, "electric_fence");
        this.setDefaultState(this.getDefaultState().with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false));
    }

    @Override
    public BlockRenderType getRenderType(BlockState p_149645_1_) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean isVariableOpacity() {
        return false;
    }

    @Override
    public boolean isNormalCube(BlockState p_220081_1_, IBlockReader p_220081_2_, BlockPos p_220081_3_) {
        return false;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, IBlockReader p_220071_2_, BlockPos p_220071_3_, ISelectionContext p_220071_4_) {
        return COLLISION_BOXES[getBoundingBoxId(p_220071_1_)];
    }

    @Override
    public VoxelShape getRenderShape(BlockState p_196247_1_, IBlockReader p_196247_2_, BlockPos p_196247_3_) {
        return VoxelShapes.create(BOUNDING_BOX[getBoundingBoxId(p_196247_1_)]);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return VoxelShapes.create(BOUNDING_BOX[getBoundingBoxId(p_220053_1_)]);
    }

    private static int getBoundingBoxId(BlockState state)
    {
        int i = 0;

        if ((state.get(NORTH)))
        {
            i |= 1 << Direction.NORTH.getHorizontalIndex();
        }

        if ((state.get(EAST)))
        {
            i |= 1 << Direction.EAST.getHorizontalIndex();
        }

        if ((state.get(SOUTH)))
        {
            i |= 1 << Direction.SOUTH.getHorizontalIndex();
        }

        if ((state.get(WEST)))
        {
            i |= 1 << Direction.WEST.getHorizontalIndex();
        }

        return i;
    }

    @Override
    public void onEntityCollision(BlockState p_196262_1_, World world, BlockPos pos, Entity entity) {
        if (!(entity instanceof ItemEntity) && !entity.getName().equals("unknown"))
        {
            if (entity instanceof CreeperEntity)
            {
                CreeperEntity creeper = (CreeperEntity) entity;
                LightningBoltEntity lightning = new LightningBoltEntity(world, pos.getX(), pos.getY(), pos.getZ(), false);
                if(!creeper.getPowered())
                {
                    creeper.setFire(1);
                    creeper.onStruckByLightning(lightning);
                }
            }
            else if (entity instanceof PlayerEntity)
            {
                if (!((PlayerEntity) entity).isCreative())
                {
                    entity.attackEntityFrom(this.electricFence, (int) 2.0F);
                    world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.BLOCK_ANVIL_HIT, SoundCategory.BLOCKS, 0.2F, 1.0F);

                    this.sparkle(world, pos);
                }
            }
            else
            {
                entity.attackEntityFrom(this.electricFence, (int) 2.0F);
                world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.BLOCK_ANVIL_HIT, SoundCategory.BLOCKS, 0.2F, 1.0F);
                this.sparkle(world, pos);
            }
        }
    }

    private void sparkle(World worldIn, BlockPos pos)
    {
        BlockState state = worldIn.getBlockState(pos);
        double d0 = 0.0625D;

        for (int l = 0; l < 6; ++l)
        {
            double d1 = (pos.getX() + RANDOM.nextFloat());
            double d2 = (pos.getY() + RANDOM.nextFloat());
            double d3 = (pos.getZ() + RANDOM.nextFloat());

            if (l == 0)
            {
                d2 = (pos.getY() + 1) + d0;
            }

            if (l == 1)
            {
                d2 = (pos.getY()) - d0;
            }

            if (l == 2)
            {
                d3 = (pos.getZ() + 1) + d0;
            }

            if (l == 3)
            {
                d3 = (pos.getZ()) - d0;
            }

            if (l == 4)
            {
                d1 = (pos.getX() + 1) + d0;
            }

            if (l == 5)
            {
                d1 = (pos.getX()) - d0;
            }

            if (d1 < pos.getX() || d1 > (pos.getX() + 1) || d2 < 0.0D || d2 > (pos.getY() + 1) || d3 < pos.getZ() || d3 > (pos.getZ() + 1))
            {
                worldIn.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
            }
        }
    }


    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(NORTH,SOUTH,EAST,WEST);
    }
}
