package io.github.vampirestudios.hgm.core.client;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.vampirestudios.hgm.DeviceConfig;
import io.github.vampirestudios.hgm.api.print.IPrint;
import io.github.vampirestudios.hgm.api.print.PrintingManager;
import io.github.vampirestudios.hgm.block.BlockPaper;
import io.github.vampirestudios.hgm.block.entity.TileEntityPaper;
import io.github.vampirestudios.hgm.init.GadgetBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;
import org.lwjgl.opengl.GL11;

public class PaperRenderer extends TileEntityRenderer<TileEntityPaper> {
    private static void drawCuboid(double x, double y, double z, double width, double height, double depth) {
        x /= 16;
        y /= 16;
        z /= 16;
        width /= 16;
        height /= 16;
        depth /= 16;
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.normal3f(0.0F, 1.0F, 0.0F);
        drawQuad(x + (1 - width), y, z, x + width + (1 - width), y + height, z, Direction.NORTH);
        drawQuad(x + 1, y, z, x + 1, y + height, z + depth, Direction.EAST);
        drawQuad(x + width + 1 - (width + width), y, z + depth, x + width + 1 - (width + width), y + height, z, Direction.WEST);
        drawQuad(x + (1 - width), y, z + depth, x + width + (1 - width), y, z, Direction.DOWN);
        drawQuad(x + (1 - width), y + height, z, x + width + (1 - width), y, z + depth, Direction.UP);
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
    }

    private static void drawQuad(double xFrom, double yFrom, double zFrom, double xTo, double yTo, double zTo, Direction facing) {
        double textureWidth = Math.abs(xTo - xFrom);
        double textureHeight = Math.abs(yTo - yFrom);
        double textureDepth = Math.abs(zTo - zFrom);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        switch (facing.getAxis()) {
            case X:
                buffer.pos(xFrom, yFrom, zFrom).tex(1 - xFrom + textureDepth, 1 - yFrom + textureHeight).endVertex();
                buffer.pos(xFrom, yTo, zFrom).tex(1 - xFrom + textureDepth, 1 - yFrom).endVertex();
                buffer.pos(xTo, yTo, zTo).tex(1 - xFrom, 1 - yFrom).endVertex();
                buffer.pos(xTo, yFrom, zTo).tex(1 - xFrom, 1 - yFrom + textureHeight).endVertex();
                break;
            case Y:
                buffer.pos(xFrom, yFrom, zFrom).tex(1 - xFrom + textureWidth, 1 - yFrom + textureDepth).endVertex();
                buffer.pos(xFrom, yFrom, zTo).tex(1 - xFrom + textureWidth, 1 - yFrom).endVertex();
                buffer.pos(xTo, yFrom, zTo).tex(1 - xFrom, 1 - yFrom).endVertex();
                buffer.pos(xTo, yFrom, zFrom).tex(1 - xFrom, 1 - yFrom + textureDepth).endVertex();
                break;
            case Z:
                buffer.pos(xFrom, yFrom, zFrom).tex(1 - xFrom + textureWidth, 1 - yFrom + textureHeight).endVertex();
                buffer.pos(xFrom, yTo, zFrom).tex(1 - xFrom + textureWidth, 1 - yFrom).endVertex();
                buffer.pos(xTo, yTo, zTo).tex(1 - xFrom, 1 - yFrom).endVertex();
                buffer.pos(xTo, yFrom, zTo).tex(1 - xFrom, 1 - yFrom + textureHeight).endVertex();
                break;
        }
        tessellator.draw();
    }

    private static void drawPixels(int[] pixels, int resolution, boolean cut) {
        double scale = 16 / (double) resolution;
        for (int i = 0; i < resolution; i++) {
            for (int j = 0; j < resolution; j++) {
                float a = (float) Math.floor((pixels[j + i * resolution] >> 24 & 255) / 255.0F);
                if (a < 1.0F) {
                    if (cut) continue;
                    GlStateManager.color3f(1.0F, 1.0F, 1.0F);
                } else {
                    float r = (float) (pixels[j + i * resolution] >> 16 & 255) / 255.0F;
                    float g = (float) (pixels[j + i * resolution] >> 8 & 255) / 255.0F;
                    float b = (float) (pixels[j + i * resolution] & 255) / 255.0F;
                    GlStateManager.color4f(r, g, b, a);
                }
                drawCuboid(j * scale - (resolution - 1) * scale, -i * scale + (resolution - 1) * scale, -1, scale, scale, 1);
            }
        }
    }

    @Override
    public void render(TileEntityPaper te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        {
            GlStateManager.translated(x, y, z);
            GlStateManager.translated(0.5, 0.5, 0.5);
            BlockState state = te.getWorld().getBlockState(te.getPos());
            if (state.getBlock() != GadgetBlocks.PAPER) return;
            GlStateManager.rotatef(state.get(BlockPaper.FACING).getHorizontalIndex() * -90F + 180F, 0, 1, 0);
            GlStateManager.rotatef(-te.getRotation(), 0, 0, 1);
            GlStateManager.translated(-0.5, -0.5, -0.5);

            IPrint print = te.getPrint();
            if (print != null) {
                CompoundNBT data = print.toTag();
                if (data.contains("pixels", Constants.NBT.TAG_INT_ARRAY) && data.contains("resolution", Constants.NBT.TAG_INT)) {
                    Minecraft.getInstance().getTextureManager().bindTexture(PrinterRenderer.ModelPaper.TEXTURE);
                    if (DeviceConfig.isRenderPrinted3D() && !data.getBoolean("cut")) {
                        drawCuboid(0, 0, 0, 16, 16, 1);
                    }

                    GlStateManager.translated(0, 0, DeviceConfig.isRenderPrinted3D() ? 0.0625 : 0.001);

                    GlStateManager.pushMatrix();
                    {
                        IPrint.Renderer renderer = PrintingManager.getRenderer(print);
                        renderer.render(data);
                    }
                    GlStateManager.popMatrix();

                    GlStateManager.pushMatrix();
                    {
                        if (DeviceConfig.isRenderPrinted3D() && data.getBoolean("cut")) {
                            CompoundNBT tag = print.toTag();
                            drawPixels(tag.getIntArray("pixels"), tag.getInt("resolution"), tag.getBoolean("cut"));
                        }
                    }
                    GlStateManager.popMatrix();
                }
            }
        }
        GlStateManager.popMatrix();
    }
}