package io.github.vampirestudios.hgm.core.client;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.api.print.IPrint;
import io.github.vampirestudios.hgm.api.print.PrintingManager;
import io.github.vampirestudios.hgm.block.BlockPrinter;
import io.github.vampirestudios.hgm.block.entity.TileEntityPrinter;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class PrinterRenderer extends TileEntityRenderer<TileEntityPrinter> {

    private static final ModelPaper MODEL_PAPER = new ModelPaper();

    @Override
    public void render(TileEntityPrinter te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        {
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.translated(x, y, z);

            if (te.hasPaper()) {
                GlStateManager.pushMatrix();
                {
                    GlStateManager.translated(0.5, 0.5, 0.5);
                    BlockState state = te.getWorld().getBlockState(te.getPos());
                    GlStateManager.rotatef(state.get(BlockPrinter.FACING).getHorizontalIndex() * -90F, 0, 1, 0);
                    GlStateManager.rotated(22.5F, 1, 0, 0);
                    GlStateManager.translated(0, 0.1, 0.35);
                    GlStateManager.translated(-11 * 0.015625, -13 * 0.015625, -0.5 * 0.015625);
                    MODEL_PAPER.render(null, 0F, 0F, 0F, 0F, 0F, 0.3F);
                }
                GlStateManager.popMatrix();
            }

            GlStateManager.pushMatrix();
            {
                if (te.isLoading()) {
                    GlStateManager.translated(0.5, 0.5, 0.5);
                    BlockState state1 = te.getWorld().getBlockState(te.getPos());
                    GlStateManager.rotated(state1.get(BlockPrinter.FACING).getHorizontalIndex() * -90F, 0, 1, 0);
                    GlStateManager.rotated(22.5F, 1, 0, 0);
                    double progress = Math.max(-0.4, -0.4 + (0.4 * ((double) (te.getRemainingPrintTime() - 10) / 20)));
                    GlStateManager.translated(0, progress, 0.36875);
                    GlStateManager.translated(-11 * 0.015625, -13 * 0.015625, -0.5 * 0.015625);
                    MODEL_PAPER.render(null, 0F, 0F, 0F, 0F, 0F, 0.015625F);
                } else if (te.isPrinting()) {
                    GlStateManager.translated(0.5, 0.078125, 0.5);
                    BlockState state1 = te.getWorld().getBlockState(te.getPos());
                    GlStateManager.rotated(state1.get(BlockPrinter.FACING).getHorizontalIndex() * -90F, 0, 1, 0);
                    GlStateManager.rotated(90F, 1, 0, 0);
                    double progress = -0.35 + (0.50 * ((double) (te.getRemainingPrintTime() - 20) / te.getTotalPrintTime()));
                    GlStateManager.translated(0, progress, 0);
                    GlStateManager.translated(-11 * 0.015625, -13 * 0.015625, -0.5 * 0.015625);
                    MODEL_PAPER.render(null, 0F, 0F, 0F, 0F, 0F, 0.015625F);

                    GlStateManager.translated(0.3225, 0.085, -0.001);
                    GlStateManager.rotated(180F, 0, 1, 0);
                    GlStateManager.scaled(0.3, 0.3, 0.3);

                    IPrint print = te.getPrint();
                    if (print != null) {
                        IPrint.Renderer renderer = PrintingManager.getRenderer(print);
                        renderer.render(print.toTag());
                    }
                }
            }
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        {
            GlStateManager.translated(0, -0.5, 0);
            super.render(te, x, y, z, partialTicks, destroyStage);
        }
        GlStateManager.popMatrix();
    }

    public static class ModelPaper extends EntityModel {
        public static final ResourceLocation TEXTURE = new ResourceLocation(HuskysGadgetMod.MOD_ID, "textures/model/paper.png");

        private RendererModel box = new RendererModel(this, 0, 0).addBox(0, 0, 0, 22, 30, 1);

        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
            box.render(scale);
        }
    }
}
