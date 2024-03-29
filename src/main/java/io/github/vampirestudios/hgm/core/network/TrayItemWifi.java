package io.github.vampirestudios.hgm.core.network;

import io.github.vampirestudios.hgm.Config;
import io.github.vampirestudios.hgm.api.app.Layout;
import io.github.vampirestudios.hgm.api.app.component.Button;
import io.github.vampirestudios.hgm.api.app.component.ItemList;
import io.github.vampirestudios.hgm.api.app.emojies.Icons;
import io.github.vampirestudios.hgm.api.app.renderer.ListItemRenderer;
import io.github.vampirestudios.hgm.api.task.TaskManager;
import io.github.vampirestudios.hgm.api.utils.RenderUtil;
import io.github.vampirestudios.hgm.block.entity.TileEntityDevice;
import io.github.vampirestudios.hgm.block.entity.TileEntityRouter;
import io.github.vampirestudios.hgm.core.BaseDevice;
import io.github.vampirestudios.hgm.core.Device;
import io.github.vampirestudios.hgm.core.network.task.TaskConnect;
import io.github.vampirestudios.hgm.core.network.task.TaskPing;
import io.github.vampirestudios.hgm.object.TrayItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrayItemWifi extends TrayItem {

    private int pingTimer;

    public TrayItemWifi() {
        super(Icons.WIFI_NONE);
    }

    private static Layout createWifiMenu(TrayItem item) {
        Layout layout = new Layout.Context(100, 100);
        layout.setBackground((gui, mc, x, y, width, height, mouseX, mouseY, windowActive) ->
                Screen.fill(x, y, x + width, y + height, new Color(0.65F, 0.65F, 0.65F, 0.9F).getRGB()));

        ItemList<Device> itemListRouters = new ItemList<>(5, 5, 90, 4);
        itemListRouters.setItems(getRouters());
        itemListRouters.setListItemRenderer(new ListItemRenderer<Device>(16) {
            @Override
            public void render(Device device, Screen gui, Minecraft mc, int x, int y, int width, int height, boolean selected) {
                Screen.fill(x, y, x + width, y + height, selected ? Color.DARK_GRAY.getRGB() : Color.GRAY.getRGB());
                RenderUtil.drawStringClipped(device.getName(), x + 16, y + 4, 70, Color.WHITE.getRGB(), false);

                if (device.getPos() == null)
                    return;

                BlockPos laptopPos = BaseDevice.getPos();
                double distance = Math.sqrt(device.getPos().distanceSq(new Vec3i(laptopPos.getX() + 0.5, laptopPos.getY() + 0.5, laptopPos.getZ() + 0.5)));
                if (distance > 20) {
                    Icons.WIFI_LOW.draw(mc, x + 3, y + 3);
                } else if (distance > 10) {
                    Icons.WIFI_MED.draw(mc, x + 3, y + 3);
                } else {
                    Icons.WIFI_HIGH.draw(mc, x + 3, y + 3);
                }
            }
        });
        itemListRouters.sortBy((o1, o2) -> {
            BlockPos laptopPos = BaseDevice.getPos();
            double distance1 = Math.sqrt(Objects.requireNonNull(o1.getPos()).distanceSq(new Vec3i(laptopPos.getX() + 0.5, laptopPos.getY() + 0.5, laptopPos.getZ() + 0.5)));
            double distance2 = Math.sqrt(Objects.requireNonNull(o2.getPos()).distanceSq(new Vec3i(laptopPos.getX() + 0.5, laptopPos.getY() + 0.5, laptopPos.getZ() + 0.5)));
            return Double.compare(distance1, distance2);
        });
        layout.addComponent(itemListRouters);

        Button buttonConnect = new Button(79, 79, Icons.CHECK);
        buttonConnect.setClickListener((mouseX, mouseY, mouseButton) ->
        {
            if (mouseButton == 0) {
                if (itemListRouters.getSelectedItem() != null) {
                    TaskConnect connect = new TaskConnect(BaseDevice.getPos(), itemListRouters.getSelectedItem().getPos());
                    connect.setCallback((tagCompound, success) ->
                    {
                        if (success) {
                            item.setIcon(Icons.WIFI_HIGH);
                            BaseDevice.getSystem().closeContext();
                        }
                    });
                    TaskManager.sendTask(connect);
                }
            }
        });
        layout.addComponent(buttonConnect);

        return layout;
    }

    public static List<Device> getRouters() {
        List<Device> routers = new ArrayList<>();

        World world = Minecraft.getInstance().world;
        BlockPos laptopPos = BaseDevice.getPos();
        int range = Config.getSignalRange();

        for (int y = -range; y < range + 1; y++) {
            for (int z = -range; z < range + 1; z++) {
                for (int x = -range; x < range + 1; x++) {
                    BlockPos pos = new BlockPos(laptopPos.getX() + x, laptopPos.getY() + y, laptopPos.getZ() + z);
                    TileEntity tileEntity = world.getTileEntity(pos);
                    if (tileEntity instanceof TileEntityRouter) {
                        routers.add(new Device((TileEntityDevice) tileEntity));
                    }
                }
            }
        }
        return routers;
    }

    @Override
    public void init() {
        this.setClickListener((mouseX, mouseY, mouseButton) ->
        {
            if (BaseDevice.getSystem().hasContext()) {
                BaseDevice.getSystem().closeContext();
            } else {
                BaseDevice.getSystem().openContext(createWifiMenu(this), mouseX - 100, mouseY - 100);
            }
        });

        runPingTask();
    }

    @Override
    public void tick() {
        if (++pingTimer >= Config.getPingRate()) {
            runPingTask();
            pingTimer = 0;
        }
    }

    private void runPingTask() {
        TaskPing task = new TaskPing(BaseDevice.getPos());
        task.setCallback((tagCompound, success) ->
        {
            if (success) {
                int strength = tagCompound.getInt("strength");
                switch (strength) {
                    case 2:
                        setIcon(Icons.WIFI_LOW);
                        break;
                    case 1:
                        setIcon(Icons.WIFI_MED);
                        break;
                    case 0:
                        setIcon(Icons.WIFI_HIGH);
                        break;
                    default:
                        setIcon(Icons.WIFI_NONE);
                        break;
                }
            } else {
                setIcon(Icons.WIFI_NONE);
            }
        });
        TaskManager.sendTask(task);
    }
}