package io.github.vampirestudios.hgm.core.client;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.vampirestudios.hgm.block.BlockPrinter;
import io.github.vampirestudios.hgm.block.BlockRouter;
import io.github.vampirestudios.hgm.block.entity.TileEntityRouter;
import io.github.vampirestudios.hgm.core.network.NetworkDevice;
import io.github.vampirestudios.hgm.core.network.Router;
import io.github.vampirestudios.hgm.utils.CollisionHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.util.Collection;

public class RouterRenderer extends TileEntityRenderer<TileEntityRouter> {
    @Override
    public void render(TileEntityRouter te, double x, double y, double z, float partialTicks, int destroyStage) {
        BlockState state = te.getWorld().getBlockState(te.getPos());
        if (!(state.getBlock() instanceof BlockRouter))
            return;

        if (te.isDebug()) {
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(770, 771, 1, 0);
            GlStateManager.disableLighting();
            GlStateManager.disableTexture();
            GlStateManager.enableAlphaTest();
            GlStateManager.pushMatrix();
            {
                GlStateManager.translated(x, y, z);
                Router router = te.getRouter();
                BlockPos routerPos = router.getPos();

                Vec3d linePositions = getLineStartPosition(state);
                final double startLineX = linePositions.x;
                final double startLineY = linePositions.y;
                final double startLineZ = linePositions.z;

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();

                final Collection<NetworkDevice> DEVICES = router.getConnectedDevices(Minecraft.getInstance().world);
                DEVICES.forEach(networkDevice ->
                {
                    BlockPos devicePos = networkDevice.getPos();

                    GL11.glLineWidth(14F);
                    buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
                    buffer.pos(startLineX, startLineY, startLineZ).color(0.0F, 0.0F, 0.0F, 0.5F).endVertex();
                    buffer.pos((devicePos.getX() - routerPos.getX()) + 0.5F, (devicePos.getY() - routerPos.getY()), (devicePos.getZ() - routerPos.getZ()) + 0.5F).color(1.0F, 1.0F, 1.0F, 0.35F).endVertex();
                    tessellator.draw();

                    GL11.glLineWidth(4F);
                    buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
                    buffer.pos(startLineX, startLineY, startLineZ).color(0.0F, 0.0F, 0.0F, 0.5F).endVertex();
                    buffer.pos((devicePos.getX() - routerPos.getX()) + 0.5F, (devicePos.getY() - routerPos.getY()), (devicePos.getZ() - routerPos.getZ()) + 0.5F).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
                    tessellator.draw();
                });
            }
            GlStateManager.popMatrix();
            GlStateManager.disableBlend();
            GlStateManager.disableAlphaTest();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture();
        }
    }

    private Vec3d getLineStartPosition(BlockState state) {
        float lineX = 0.5F;
        float lineY = 0.1F;
        float lineZ = 0.5F;

        if (state.get(BlockRouter.VERTICAL)) {
            double[] fixedPosition = CollisionHelper.fixRotation(state.get(BlockPrinter.FACING), 14 * 0.0625, 0.5, 14 * 0.0625, 0.5);
            lineX = (float) fixedPosition[0];
            lineY = 0.35F;
            lineZ = (float) fixedPosition[1];
        }

        return new Vec3d(lineX, lineY, lineZ);
    }
}