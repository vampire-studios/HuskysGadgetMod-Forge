package io.github.vampirestudios.hgm.core.client;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.api.app.emojies.Icons;
import io.github.vampirestudios.hgm.api.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

public class ClientNotification implements IToast {
    private static final ResourceLocation TEXTURE_TOASTS = new ResourceLocation(HuskysGadgetMod.MOD_ID, "textures/gui/toast.png");

    private Icons icon;
    private String title;
    private String subTitle;

    private ClientNotification() {
    }

    public static ClientNotification loadFromTag(CompoundNBT tag) {
        ClientNotification notification = new ClientNotification();
        notification.icon = Icons.values()[tag.getInt("icon")];
        notification.title = tag.getString("title");
        if (tag.contains("subTitle", Constants.NBT.TAG_STRING)) {
            notification.subTitle = tag.getString("subTitle");
        }
        return notification;
    }

    @Override
    public Visibility draw(ToastGui toastGui, long delta) {
        GlStateManager.color3f(1.0F, 1.0F, 1.0F);
        toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
        toastGui.blit(0, 0, 0, 0, 160, 32);

        toastGui.getMinecraft().getTextureManager().bindTexture(icon.getIconAsset());
        RenderUtil.drawRectWithTexture(6, 6, icon.getU(), icon.getV(), 20, 20, 10, 10, 200, 200);

        if (subTitle == null) {
            toastGui.getMinecraft().fontRenderer.drawStringWithShadow(RenderUtil.clipStringToWidth(I18n.format(title), 118), 38, 12, -1);
        } else {
            toastGui.getMinecraft().fontRenderer.drawStringWithShadow(RenderUtil.clipStringToWidth(I18n.format(title), 118), 38, 7, -1);
            toastGui.getMinecraft().fontRenderer.drawString(RenderUtil.clipStringToWidth(I18n.format(subTitle), 118), 38, 18, -1);
        }

        return delta >= 5000L ? IToast.Visibility.HIDE : IToast.Visibility.SHOW;
    }

    public void push() {
        Minecraft.getInstance().getToastGui().add(this);
    }

}