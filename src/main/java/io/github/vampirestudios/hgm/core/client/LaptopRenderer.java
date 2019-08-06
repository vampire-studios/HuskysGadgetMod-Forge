package io.github.vampirestudios.hgm.core.client;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.block.BlockLaptop;
import io.github.vampirestudios.hgm.block.entity.TileEntityLaptop;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.data.EmptyModelData;

public class LaptopRenderer extends TileEntityRenderer<TileEntityLaptop> {

    private Minecraft mc = Minecraft.getInstance();

    private ItemEntity entityItem = new ItemEntity(Minecraft.getInstance().world, 0D, 0D, 0D);

    @Override
    public void render(TileEntityLaptop te, double x, double y, double z, float partialTicks, int destroyStage) {
        BlockPos pos = te.getPos();
        BlockState state = te.getWorld().getBlockState(pos).with(BlockLaptop.TYPE, BlockLaptop.Type.SCREEN);

        bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.pushMatrix();
        {
            GlStateManager.translated(x, y, z);

            if (te.isExternalDriveAttached()) {
                GlStateManager.pushMatrix();
                {
                    GlStateManager.translated(0.5, 0, 0.5);
                    GlStateManager.rotated(-100F - 90F, 0, 1, 0);
                    GlStateManager.translated(-0.5, 0, -0.5);
                    GlStateManager.translated(0.595, -0.2075, -0.005);
                    entityItem.setItem(new ItemStack(Registry.ITEM.getOrDefault(new ResourceLocation(HuskysGadgetMod.MOD_ID + "flash_drive_" + te.getExternalDriveColor().getName())), 1));
                    Minecraft.getInstance().getRenderManager().renderEntity(entityItem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F, false);
                    GlStateManager.translated(0.1, 0, 0);
                }
                GlStateManager.popMatrix();
            }

            GlStateManager.pushMatrix();
            {
                GlStateManager.translated(0.5, 0, 0.5);
                GlStateManager.rotated(-90F + 180F, 0, 1, 0);
                GlStateManager.translated(-0.5, 0, -0.5);
                GlStateManager.translated(0, 0.07, 0.12 + 1.0/16.0);
                GlStateManager.rotated(te.getScreenAngle(partialTicks), 1, 0, 0);
                GlStateManager.translated(0, -0.04, -1.0/16.0);

                GlStateManager.disableLighting();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();
                buffer.begin(7, DefaultVertexFormats.BLOCK);
                buffer.setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());

                BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

                IBakedModel ibakedmodel = mc.getBlockRendererDispatcher().getBlockModelShapes().getModel(state);
                blockrendererdispatcher.getBlockModelRenderer().renderModel(getWorld(), ibakedmodel, state, pos, buffer, false, getWorld().rand, state.getPositionRandom(pos), EmptyModelData.INSTANCE);

                buffer.setTranslation(0.0D, 0.0D, 0.0D);
                tessellator.draw();
                GlStateManager.enableLighting();
            }
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
    }
}