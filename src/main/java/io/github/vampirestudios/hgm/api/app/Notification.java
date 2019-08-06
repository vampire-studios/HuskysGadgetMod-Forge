package io.github.vampirestudios.hgm.api.app;

import io.github.vampirestudios.hgm.api.app.emojies.Icons;
import io.github.vampirestudios.hgm.network.PacketHandler;
import io.github.vampirestudios.hgm.network.task.MessageNotification;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.lang3.StringUtils;

/**
 * The notification class for the notification system.
 * <p>
 * This class is intended to be used only on the server (logical and physical) side only. Typically
 * you'd want to be able to send a notification to anyone on the server. There is two options to
 * perform this, either create a background task on the server (a tick event) or send a
 * {@link io.github.vampirestudios.hgm.api.task.Task} from the client to the server. It is not possible to
 * do this from the client side alone.
 * <p>
 * If a notification is needed to be produced on the client side only, see
 * {@link io.github.vampirestudios.hgm.core.client.ClientNotification}
 */
public class Notification {
    private Icons icon;
    private String title;
    private String subTitle;

    /**
     * The default constructor for a notification.
     *
     * @param icon  the icon to display
     * @param title the title of the notification
     */
    public Notification(Icons icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    /**
     * The alternate constructor for a notification. This includes a sub title.
     *
     * @param icon     the icon to display
     * @param title    the title of the notification
     * @param subTitle the sub title of the notification
     */
    public Notification(Icons icon, String title, String subTitle) {
        this(icon, title);
        this.subTitle = subTitle;
    }

    /**
     * Writes the notification to a tag for the client
     *
     * @return the notification tag
     */
    public CompoundNBT toTag() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("icon", icon.ordinal());
        tag.putString("title", title);
        if (!StringUtils.isEmpty(subTitle)) {
            tag.putString("subTitle", subTitle);
        }
        return tag;
    }

    /**
     * Sends this notification to the specified player
     *
     * @param player the target player
     */
    public void pushTo(ServerPlayerEntity player) {
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new MessageNotification(this));
    }
}
