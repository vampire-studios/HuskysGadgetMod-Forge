package io.github.vampirestudios.hgm.block;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class BlockElectricSecurityFence extends FenceBlock {

    protected static DamageSource electricFence = new DamageSource("electricity");

    public BlockElectricSecurityFence() {
        super(Properties.create(Material.IRON).lightValue(2).sound(SoundType.ANVIL).hardnessAndResistance(1.0F));
        this.setRegistryName(HuskysGadgetMod.MOD_ID, "electric_fence");
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

    @Override
    public boolean func_220111_a(BlockState p_220111_1_, boolean p_220111_2_, Direction p_220111_3_) {
        Block lvt_4_1_ = p_220111_1_.getBlock();
        boolean lvt_5_1_ = p_220111_1_.getMaterial() == this.material;
        boolean lvt_6_1_ = lvt_4_1_ instanceof BlockElectricSecurityFence;
        return !cannotAttach(lvt_4_1_) && p_220111_2_ || lvt_5_1_ || lvt_6_1_;
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
}
