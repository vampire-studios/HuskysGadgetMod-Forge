package io.github.vampirestudios.hgm.core;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.api.AppInfo;
import io.github.vampirestudios.hgm.api.ApplicationManager;
import io.github.vampirestudios.hgm.api.app.Application;
import io.github.vampirestudios.hgm.api.app.Layout;
import io.github.vampirestudios.hgm.api.app.component.Label;
import io.github.vampirestudios.hgm.api.io.Drive;
import io.github.vampirestudios.hgm.api.io.File;
import io.github.vampirestudios.hgm.api.os.OperatingSystem;
import io.github.vampirestudios.hgm.api.task.Callback;
import io.github.vampirestudios.hgm.api.task.Task;
import io.github.vampirestudios.hgm.api.task.TaskManager;
import io.github.vampirestudios.hgm.api.utils.RenderUtil;
import io.github.vampirestudios.hgm.block.entity.TileEntityBaseDevice;
import io.github.vampirestudios.hgm.core.OSLayouts.LayoutDesktopOS;
import io.github.vampirestudios.hgm.core.tasks.TaskInstallApp;
import io.github.vampirestudios.hgm.object.ThemeInfo;
import io.github.vampirestudios.hgm.programs.system.SystemApplication;
import io.github.vampirestudios.hgm.programs.system.component.FileBrowser;
import io.github.vampirestudios.hgm.programs.system.tasks.TaskUpdateApplicationData;
import io.github.vampirestudios.hgm.programs.system.tasks.TaskUpdateSystemData;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

public class BaseDevice extends Screen implements System {

    public static final ResourceLocation ICON_TEXTURES = new ResourceLocation(HuskysGadgetMod.MOD_ID, "textures/gui/app_icons.png");
    public static final ResourceLocation BANNER_TEXTURES = new ResourceLocation(HuskysGadgetMod.MOD_ID, "textures/gui/app_banners.png");
    public static final FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
    public static final int DEVICE_WIDTH = 464;
    public static final int DEVICE_HEIGHT = 246;
    public static final List<ResourceLocation> WALLPAPERS = new ArrayList<>();
    public static final List<ResourceLocation> THEMES = new ArrayList<>();
    public static final ResourceLocation BOOT_NEON_TEXTURES = new ResourceLocation(HuskysGadgetMod.MOD_ID, "textures/gui/neon/neon_boot.png");
    public static final ResourceLocation BOOT_PIXEL_TEXTURES = new ResourceLocation(HuskysGadgetMod.MOD_ID, "textures/gui/pixel/pixel_boot.png");
    public static final ResourceLocation BOOT_CRAFT_TEXTURES = new ResourceLocation(HuskysGadgetMod.MOD_ID, "textures/gui/craft/craft_boot.png");
    private static final ResourceLocation LAPTOP_GUI = new ResourceLocation(HuskysGadgetMod.MOD_ID, "textures/gui/laptop.png");
    private static final List<Application> APPLICATIONS = new ArrayList<>();
    private static final int BOOT_ON_TIME = 200;
    private static final int BOOT_OFF_TIME = 100;
    private static final int[] konamiCodes = new int[]{
            GLFW.GLFW_KEY_UP,
            GLFW.GLFW_KEY_UP,
            GLFW.GLFW_KEY_DOWN,
            GLFW.GLFW_KEY_DOWN,
            GLFW.GLFW_KEY_LEFT,
            GLFW.GLFW_KEY_RIGHT,
            GLFW.GLFW_KEY_LEFT,
            GLFW.GLFW_KEY_RIGHT,
            GLFW.GLFW_KEY_B,
            GLFW.GLFW_KEY_A
    };
    private static final HashMap<Integer, String> codeToName = new HashMap<>();
    public static int ID;
    public static int BORDER = 10;
    public static final int SCREEN_WIDTH = DEVICE_WIDTH - BORDER * 2;
    public static final int SCREEN_HEIGHT = DEVICE_HEIGHT - BORDER * 2;
    public static int currentWallpaper, currentTheme;
    private static System system;
    private static BlockPos pos;
    private static Drive mainDrive;
    private static Layout context = null;

    // Populate the list above
    static {
        codeToName.put(GLFW.GLFW_KEY_UP, "up");
        codeToName.put(GLFW.GLFW_KEY_DOWN, "down");
        codeToName.put(GLFW.GLFW_KEY_LEFT, "left");
        codeToName.put(GLFW.GLFW_KEY_RIGHT, "right");
        codeToName.put(GLFW.GLFW_KEY_A, "A");
        codeToName.put(GLFW.GLFW_KEY_B, "B");
    }

    public int posX, posY;
    List<AppInfo> installedApps = new ArrayList<>();
    private Settings settings;
    private TaskBar bar;
    private Window[] windows;
    private CompoundNBT appData;
    private CompoundNBT systemData;
    private int lastMouseX, lastMouseY;
    private long lastClick;
    private boolean dragging = false;
    private boolean stretching = false;
    private boolean[] stretchDirections = new boolean[]{false, false, false, false};
    private int bootTimer = 0;
    private BootMode bootMode = BootMode.BOOTING;
    private int blinkTimer = 0;
    private int lastCode = GLFW.GLFW_KEY_DOWN;
    private int konamiProgress = 0;
    private Layout desktop, OSSelect;
    private String wallpaperOrColor, taskbarPlacement, os;

    public BaseDevice(TileEntityBaseDevice te, int id, OperatingSystem OS) {
        super(new StringTextComponent("Device"));
        ID = id;
        this.appData = te.getApplicationData();
        this.systemData = te.getSystemData();
        this.windows = new Window[5];
        this.settings = Settings.fromTag(systemData.getCompound("Settings"));
        this.bar = OS.taskBar();
        wallpaperOrColor = Settings.fromTag(systemData.getCompound("wallpaperOrColor")).hasWallpaperOrColor();
        if (wallpaperOrColor.equals("Wallpaper")) {
            currentWallpaper = systemData.getInt("CurrentWallpaper");
            if (currentWallpaper < 0 || currentWallpaper >= WALLPAPERS.size()) {
                currentWallpaper = 0;
            }
        }
        taskbarPlacement = Settings.fromTag(systemData.getCompound("taskBarPlacement")).getTaskBarPlacement();
        currentTheme = systemData.getInt("CurrentTheme");
        if (currentTheme < 0 || currentTheme >= THEMES.size()) {
            currentTheme = 0;
        }
        os = OS.name();
        BaseDevice.system = this;
        BaseDevice.pos = te.getPos();
        java.lang.System.out.println(te.getClass().getName());
        this.desktop = new LayoutDesktopOS();
        if (systemData.contains("bootMode")) {
            this.bootMode = BootMode.getBootMode(systemData.getInt("bootMode"));
        }

        if (systemData.contains("bootTimer")) {
            this.bootTimer = systemData.getInt("bootTimer");
        }

        if (this.bootMode == null) {
            this.bootMode = BootMode.BOOTING;
            this.bootTimer = BOOT_ON_TIME;
        }
    }

    @Nullable
    public static BlockPos getPos() {
        return pos;
    }

    public static void addWallpaper(ResourceLocation wallpaper) {
        if (wallpaper != null) {
            WALLPAPERS.add(wallpaper);
        }
    }

    public static void addTheme(ResourceLocation theme) {
        if (theme != null) {
            THEMES.add(theme);
        }
    }

    public static System getSystem() {
        return system;
    }

    @Nullable
    public static Drive getMainDrive() {
        return mainDrive;
    }

    public static void setMainDrive(Drive mainDrive) {
        if (BaseDevice.mainDrive == null) {
            BaseDevice.mainDrive = mainDrive;
        }
    }

    public static void nextWallpaper() {
        if (currentWallpaper + 1 < WALLPAPERS.size()) {
            currentWallpaper++;
        }
    }

    public static void prevWallpaper() {
        if (currentWallpaper - 1 >= 0) {
            currentWallpaper--;
        }
    }

    public static int getCurrentWallpaper() {
        return currentWallpaper;
    }

    public static int getCurrentTheme() {
        return currentTheme;
    }

    public static List<ResourceLocation> getWallapapers() {
        return ImmutableList.copyOf(WALLPAPERS);
    }

    public static List<ResourceLocation> getThemes() {
        return ImmutableList.copyOf(THEMES);
    }

    public static Layout getContext() {
        return context;
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        return false;
    }

    @Override
    public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
        return false;
    }

    @Override
    public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
        return false;
    }

    @Override
    public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
        return false;
    }

    @Override
    public void mouseMoved(double p_212927_1_, double p_212927_3_) {

    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public boolean keyReleased(int p_223281_1_, int p_223281_2_, int p_223281_3_) {
        return false;
    }

    /*@Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.lastMouseX = mouseX;
        this.lastMouseY = mouseY;

        int posX = width / 2 - SCREEN_WIDTH / 2;
        int posY = height / 2 - SCREEN_HEIGHT / 2;

        if (this.bootMode == BootMode.NOTHING) {
            if (context != null) {
                int dropdownX = context.xPosition;
                int dropdownY = context.yPosition;
                if (GuiHelper.isMouseInside(mouseX, mouseY, dropdownX, dropdownY, dropdownX + context.width, dropdownY + context.height)) {
                    context.handleMouseClick(mouseX, mouseY, mouseButton);
                    return;
                } else {
                    context = null;
                }
            }

            switch (taskbarPlacement) {
                case "Top":
                    this.bar.handleClick(this, posX, posY + SCREEN_HEIGHT - 226, mouseX, mouseY, mouseButton);
                    break;
                case "Bottom":
                    this.bar.handleClick(this, posX, posY + SCREEN_HEIGHT - TaskBar.BAR_HEIGHT, mouseX, mouseY, mouseButton);
                    break;
                case "Left":
                    this.bar.handleClick(this, posX - TaskBar.BAR_HEIGHT, posY + SCREEN_HEIGHT, mouseX, mouseY, mouseButton);
                    break;
                case "Right":
                    this.bar.handleClick(this, posX, posY + SCREEN_HEIGHT - TaskBar.BAR_HEIGHT, mouseX, mouseY, mouseButton);
                    break;
            }

            for (int i = 0; i < windows.length; i++) {
                Window<Application> window = windows[i];
                if (window != null) {
                    Window dialogWindow = window.getContent().getActiveDialog();
                    if (this.isMouseWithinWindow(mouseX, mouseY, window) || this.isMouseWithinWindow(mouseX, mouseY, dialogWindow)) {
                        windows[i] = null;
                        updateWindowStack();
                        windows[0] = window;

                        window.handleMouseClick(this, posX, posY, mouseX, mouseY, mouseButton);

                        Window stretchingWindow = dialogWindow == null ? window : dialogWindow;
                        if (this.isMouseWithinWindow(mouseX, mouseY, stretchingWindow) && stretchingWindow.isDecorated() && !stretchingWindow.isMaximized())
                        {
                            boolean left = mouseX < posX + stretchingWindow.getOffsetX() + 1;
                            boolean right = mouseX > posX + stretchingWindow.getOffsetX() + stretchingWindow.getWidth() - 2;
                            boolean top = mouseY < posY + stretchingWindow.getOffsetY() + 1;
                            boolean bottom = mouseY > posY + stretchingWindow.getOffsetY() + stretchingWindow.getHeight() - 2;

                            if (left || right || top || bottom)
                            {
                                this.stretching = true;
                                this.stretchDirections[0] = left;
                                this.stretchDirections[1] = right;
                                this.stretchDirections[2] = top;
                                this.stretchDirections[3] = bottom;
                                return;
                            }
                        }

                        if (this.isMouseWithinWindowBar(mouseX, mouseY, dialogWindow) && dialogWindow.isDecorated()) {
                            if (dialogWindow.isResizable() && dialogWindow.isDecorated() && java.lang.System.currentTimeMillis() - this.lastClick <= 200) {
                                dialogWindow.setMaximized(!dialogWindow.isMaximized());
                                dialogWindow.setMinimized(!dialogWindow.isMinimized());
                                dialogWindow.setFullScreen(!dialogWindow.isFullScreen());
                            } else {
                                this.lastClick = java.lang.System.currentTimeMillis();
                                this.dragging = true;
                            }
                            return;
                        }

                        if (this.isMouseWithinWindowBar(mouseX, mouseY, window) && window.isDecorated() && dialogWindow == null) {
                            if (window.isResizable() && window.isDecorated() && java.lang.System.currentTimeMillis() - this.lastClick <= 200) {
                                window.setMaximized(!window.isMaximized());
                                window.setMinimized(!window.isMinimized());
                                window.setFullScreen(!window.isFullScreen());
                            } else {
                                this.lastClick = java.lang.System.currentTimeMillis();
                                this.dragging = true;
                            }
                            return;
                        }
                        break;
                    }
                }
            }
        } else if (this.bootMode == BootMode.BOOTING) {
            if (isMouseInHusky(mouseX, mouseY)) {
                this.blinkTimer = 20;
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.dragging = false;
        if (context != null) {
            int dropdownX = context.xPosition;
            int dropdownY = context.yPosition;
            if (GuiHelper.isMouseInside(mouseX, mouseY, dropdownX, dropdownY, dropdownX + context.width, dropdownY + context.height)) {
                context.handleMouseRelease(mouseX, mouseY, state);
            }
        } else if (windows[0] != null) {
            windows[0].handleMouseRelease(mouseX, mouseY, state);
        }
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        if (Keyboard.getEventKeyState()) {
            char pressed = Keyboard.getEventCharacter();
            int code = Keyboard.getEventKey();

            if (this.bootMode == BootMode.SHUTTING_DOWN) {
                if (codeToName.containsKey(code)) {
                    boolean valid = (this.konamiProgress < 8 && code != Keyboard.KEY_A && code != Keyboard.KEY_B) || (this.konamiProgress >= 8 && (code == Keyboard.KEY_A || code == Keyboard.KEY_B));
                    if (valid) {
                        this.lastCode = code;
                        if (this.konamiProgress != -1 && code == konamiCodes[this.konamiProgress]) {
                            this.konamiProgress++;
                            if (this.konamiProgress == konamiCodes.length) {
                                this.minecraft.getToastGui().add(new BaseDevice.EasterEggToast());
                                PacketHandler.INSTANCE.sendToServer(new MessageUnlockAdvancement());
                                this.konamiProgress = -1;
                            }
                        } else {
                            this.konamiProgress = 0;
                        }
                    }
                }
            }

            if (this.bootMode == BootMode.BOOTING) {

                if (pressed == Keybinds.bios.getKeyCode()) {
                    Layout bios = new LayoutBios();
                    bios.init();

                    if (pressed == Keybinds.leaveBios.getKeyCode()) {
                        this.desktop.init();
                    }

                }

            }

            if (windows[0] != null) {
                windows[0].handleKeyTyped(pressed, code);
            }

            super.keyTyped(pressed, code);
        } else {
            if (windows[0] != null) {
                windows[0].handleKeyReleased(Keyboard.getEventCharacter(), Keyboard.getEventKey());
            }
        }

        this.minecraft.keyboardListener.tick();
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (this.bootMode == BootMode.NOTHING) {
            int posX = width / 2 - SCREEN_WIDTH / 2;
            int posY = height / 2 - SCREEN_HEIGHT / 2;

            if (context != null) {
                int dropdownX = context.xPosition;
                int dropdownY = context.yPosition;
                if (GuiHelper.isMouseInside(mouseX, mouseY, dropdownX, dropdownY, dropdownX + context.width, dropdownY + context.height)) {
                    context.handleMouseDrag(mouseX, mouseY, clickedMouseButton);
                }
                return;
            }

            if (windows[0] != null) {
                Window<Application> window = windows[0];
                Window<Dialog> dialogWindow = window.getContent().getActiveDialog();
                if (dragging) {
                    if (isMouseOnScreen(mouseX, mouseY)) {
                        if (dialogWindow == null) {
                            window.handleWindowMove(-(lastMouseX - mouseX), -(lastMouseY - mouseY));
                        } else {
                            dialogWindow.handleWindowMove(-(lastMouseX - mouseX), -(lastMouseY - mouseY));
                        }
                    } else {
                        dragging = false;
                    }
                } else if (stretching) {
                    if (isMouseOnScreen(mouseX, mouseY)) {
                        int newX = 0;
                        int newY = 0;
                        int newWidth = 0;
                        int newHeight = 0;

                        int deltaX = (lastMouseX - mouseX);
                        int deltaY = (lastMouseY - mouseY);

                        boolean left = stretchDirections[0];
                        boolean right = stretchDirections[1];
                        boolean top = stretchDirections[2];
                        boolean bottom = stretchDirections[3];

                        if (left) {
                            newX = deltaX;
                            newWidth = deltaX;
                        } else if (right) {
                            newWidth = -deltaX;
                        }

                        if (top) {
                            newY = deltaY;
                            newHeight = deltaY;
                        } else if (bottom) {
                            newHeight = -deltaY;
                        }

                        if (dialogWindow == null) {
                            if (window.resize(window.getWidth() + newWidth - 2, window.getHeight() + newHeight - 14))
                                window.setPosition(window.getOffsetX() - newX, window.getOffsetY() - newY);
                        } else {
                            if (dialogWindow.resize(dialogWindow.getWidth() + newWidth - 2, dialogWindow.getHeight() + newHeight - 14))
                                dialogWindow.setPosition(dialogWindow.getOffsetX() - newX, dialogWindow.getOffsetY() - newY);
                        }
                    } else {
                        stretching = false;
                    }
                } else {
                    if (isMouseWithinWindow(mouseX, mouseY, window) || isMouseWithinWindow(mouseX, mouseY, dialogWindow)) {
                        window.handleMouseDrag(mouseX, mouseY, clickedMouseButton);
                    }
                }
            }
            this.lastMouseX = mouseX;
            this.lastMouseY = mouseY;
        }
    }*/

    /*@Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int scroll = Mouse.getEventDWheel();
        if (this.bootMode == BootMode.NOTHING) {
            if (scroll != 0) {
                if (windows[0] != null) {
                    windows[0].handleMouseScroll(mouseX, mouseY, scroll >= 0);
                }
            }
        }
    }*/

    /*@Override
    @ParametersAreNonnullByDefault
    public void drawHoveringText(List<String> textLines, int x, int y) {
        int guiLeft = width / 2 - DEVICE_WIDTH / 2;
        int guiTop = height / 2 - DEVICE_HEIGHT / 2;
        drawHoveringText(textLines, x - guiLeft, y - guiTop, minecraft.fontRenderer);
    }*/

    @Override
    public void init() {
        Minecraft.getInstance().keyboardListener.enableRepeatEvents(true);
        posX = width / 2 - DEVICE_WIDTH / 2;
        posY = height / 2 - DEVICE_HEIGHT / 2;
        switch (taskbarPlacement) {
            case "Top":
                bar.init(posX + BORDER, posY + DEVICE_HEIGHT - 236);
                break;
            case "Bottom":
                bar.init(posX + BORDER, posY + DEVICE_HEIGHT - 28);
                break;
            case "Left":
                bar.init(posX + BORDER, posY + DEVICE_HEIGHT - 28);
                break;
            case "Right":
                bar.init(posX + BORDER, posY + DEVICE_HEIGHT - 28);
                break;
        }

        installedApps.clear();
        ListNBT tagList = systemData.getList("InstalledApps", Constants.NBT.TAG_STRING);
        for (int i = 0; i < tagList.size(); i++) {
            AppInfo info = ApplicationManager.getApplication(tagList.getString(i));
            if (info != null && !installedApps.contains(info)) {
                installedApps.add(info);
            }
        }
        installedApps.sort(AppInfo.SORT_NAME);
    }

    @Override
    public void removed() {
        Minecraft.getInstance().keyboardListener.enableRepeatEvents(false);

        for (Window window : windows) {
            if (window != null) {
                window.close();
            }
        }

        /* Send system data */
        this.updateSystemData();

        BaseDevice.pos = null;
        BaseDevice.system = null;
        BaseDevice.mainDrive = null;
    }

    private void updateSystemData() {
        systemData.putInt("CurrentWallpaper", currentWallpaper);
        systemData.putInt("CurrentTheme", currentTheme);
        systemData.putString("wallpaperOrColor", wallpaperOrColor);
        systemData.putString("taskbarPlacement", taskbarPlacement);
        systemData.putString("OS", os);
        systemData.put("Settings", settings.toTag());

        ListNBT tagListApps = new ListNBT();
        installedApps.forEach(info -> tagListApps.add(new StringNBT(info.getFormattedId())));
        systemData.put("InstalledApps", tagListApps);

        TaskManager.sendTask(new TaskUpdateSystemData(pos, systemData));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void resize(Minecraft mcIn, int width, int height) {
        super.resize(mcIn, width, height);
        for (Window window : windows) {
            if (window != null)
                window.getContent().markForLayoutUpdate();
        }
    }

    @Override
    public void tick() {
        if (this.bootMode == BootMode.NOTHING) {
            bar.onTick();

            for (Window window : windows) {
                if (window != null) {
                    window.onTick();
                }
            }

            FileBrowser.refreshList = false;
        } else if (this.bootMode != null) {
            this.bootTimer = Math.max(this.bootTimer - 1, 0);
            this.blinkTimer = Math.max(this.blinkTimer - 1, 0);
            if (this.bootTimer == 0) {
                if (this.bootMode == BootMode.BOOTING) {
                    this.bootMode = BootMode.NOTHING;
                } else {
                    this.bootMode = null;
                }
            }
        }

    }

    public TaskBar getTaskBar() {
        return bar;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (DEVICE_WIDTH > this.width || DEVICE_HEIGHT > this.height) {
            GlStateManager.scalef(0.5f, 0.5f, 0.5f);
        }

        this.renderBackground();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(LAPTOP_GUI);

        /* Physical Screen */
        int posX = width / 2 - DEVICE_WIDTH / 2;
        int posY = height / 2 - DEVICE_HEIGHT / 2;

        /* Corners */
        this.blit(posX, posY, 0, 0, BORDER, BORDER); // TOP-LEFT
        this.blit(posX + DEVICE_WIDTH - BORDER, posY, 11, 0, BORDER, BORDER); // TOP-RIGHT
        this.blit(posX + DEVICE_WIDTH - BORDER, posY + DEVICE_HEIGHT - BORDER, 11, 11, BORDER, BORDER); // BOTTOM-RIGHT
        this.blit(posX, posY + DEVICE_HEIGHT - BORDER, 0, 11, BORDER, BORDER); // BOTTOM-LEFT

        /* Edges */
        RenderUtil.drawRectWithTexture(posX + BORDER, posY, 10, 0, SCREEN_WIDTH, BORDER, 1, BORDER); // TOP
        RenderUtil.drawRectWithTexture(posX + DEVICE_WIDTH - BORDER, posY + BORDER, 11, 10, BORDER, SCREEN_HEIGHT, BORDER, 1); // RIGHT
        RenderUtil.drawRectWithTexture(posX + BORDER, posY + DEVICE_HEIGHT - BORDER, 10, 11, SCREEN_WIDTH, BORDER, 1, BORDER); // BOTTOM
        RenderUtil.drawRectWithTexture(posX, posY + BORDER, 0, 11, BORDER, SCREEN_HEIGHT, BORDER, 1); // LEFT

        if (os.equals("None")) {
            OSSelect = new Layout();
            OSSelect.setBackground((gui, mc, x, y, width, height, mouseX1, mouseY1, windowActive) -> {
                RenderUtil.drawRectWithTexture(posX + BORDER, posY + BORDER, 10, 10, SCREEN_WIDTH, SCREEN_HEIGHT, 1, 1);
            });
            Label labelTitle = new Label("Choose What OS You Want To Have", posX + 10, posY + 40);
            labelTitle.setScale(2);
            OSSelect.addComponent(labelTitle);
            OSSelect.init();
            OSSelect.render(this, this.minecraft, posX + BORDER, posY + BORDER, mouseX, mouseY, true, partialTicks);
        }

        /* Center */
        if (!os.equals("None")) {
            RenderUtil.drawRectWithTexture(posX + BORDER, posY + BORDER, 10, 10, SCREEN_WIDTH, SCREEN_HEIGHT, 1, 1);
        }

        if (this.bootMode == BootMode.BOOTING) {
            fill(posX + BORDER, posY + BORDER, posX + DEVICE_WIDTH - BORDER, posY + DEVICE_HEIGHT - BORDER, 0xFF000000);
            switch (os) {
                case "NeonOS":
                    this.minecraft.getTextureManager().bindTexture(BOOT_NEON_TEXTURES);
                    break;
                case "CraftOS":
                    this.minecraft.getTextureManager().bindTexture(BOOT_CRAFT_TEXTURES);
                    break;
                case "PixelOS":
                    this.minecraft.getTextureManager().bindTexture(BOOT_PIXEL_TEXTURES);
                    break;
                case "None":
                    this.minecraft.getTextureManager().bindTexture(new ResourceLocation("textures/blocks/dirt.png"));
                    break;
            }
            float f = 1.0f;
            if (this.bootTimer > BOOT_ON_TIME - 20) {
                f = ((float) (BOOT_ON_TIME - this.bootTimer)) / 20.0f;
            }
            int value = (int) (255 * f);
            GlStateManager.color3f(f, f, f);
            int cX = posX + DEVICE_WIDTH / 2;
            int cY = posY + DEVICE_HEIGHT / 2;

            if (!os.equals("None")) {
                /* Husky and NeonOs logos */
                this.blit(cX - 35, cY - 80, 0, 0, 70, 90);
                if ((this.blinkTimer % 10) > 5) {
                    this.blit(cX + 1, cY - 48, 70, 15, 24, 22);
                }
                this.blit(cX - 64, cY + 15, 2, 94, 128, 30);

                /* Legal information stuff */
                this.blit(posX + BORDER + 2, posY + DEVICE_HEIGHT - BORDER - 10, 1, 152, 150, 8);
                this.blit(posX + DEVICE_WIDTH - BORDER - 41, posY + DEVICE_HEIGHT - BORDER - 10, 1, 162, 39, 7);

                /* Loading bar */
                /*GL11.glEnable(GL11.GL_SCISSOR_TEST);
                ScaledResolution sr = new ScaledResolution(this.mc);
                int scale = sr.getScaleFactor();
                GL11.glScissor((cX - 70) * scale, (height - (cY + 74)) * scale, 140 * scale, 13 * scale);
                if (this.bootTimer <= BOOT_ON_TIME - 20) {
                    int xAdd = (BOOT_ON_TIME - (this.bootTimer + 20)) * 4;
                    this.blit(cX - 87 + xAdd % 184, cY + 61, 78, 1, 17, 13);
                }
                //this.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
                GL11.glDisable(GL11.GL_SCISSOR_TEST);*/

                /* Loading bar outline */
                this.blit(cX - 70, cY + 60, 70, 0, 3, 15);
                this.blit(cX + 67, cY + 60, 74, 0, 3, 15);
                int color = 0xFF000000 + (value << 16) + (value << 8) + value;
                fill(cX - 67, cY + 60, cX + 67, cY + 61, color);
                fill(cX - 67, cY + 74, cX + 67, cY + 75, color);
            }
        } else if (this.bootMode != null) {
            if (!os.equals("None")) {
                /* Wallpaper */
                this.desktop.render(this, this.minecraft, posX + BORDER, posY + BORDER, mouseX, mouseY, true, partialTicks);

                if (this.bootMode == BootMode.NOTHING) {
                    boolean insideContext = false;
                    if (context != null) {
                        insideContext = RenderUtil.isMouseInside(mouseX, mouseY, context.xPosition, context.yPosition, context.xPosition + context.width, context.yPosition + context.height);
                    }

                    /*Image.CACHE.entrySet().removeIf(entry ->
                    {
                        Image.CachedImage cachedImage = entry.getValue();
                        if(cachedImage.isDynamic() && cachedImage.isPendingDeletion())
                        {
                            int texture = cachedImage.getTextureId();
                            if(texture != -1)
                            {
                                GL11.glDeleteTextures(texture);
                            }
                            return true;
                        }
                        return false;
                    });*/

                    /* Window */
                    for (int i = windows.length - 1; i >= 0; i--) {
                        Window window = windows[i];
                        if (window != null) {
                            window.render(this, minecraft, posX + BORDER, posY + BORDER, mouseX, mouseY, i == 0 && !insideContext, partialTicks);
                        }
                    }

                    /* Application Bar */
                    switch (taskbarPlacement) {
                        case "Top":
                            bar.render(this, minecraft, posX + BORDER, posY + DEVICE_HEIGHT - 236, mouseX, mouseY, partialTicks);
                            break;
                        case "Bottom":
                            bar.render(this, minecraft, posX + BORDER, posY + DEVICE_HEIGHT - 28, mouseX, mouseY, partialTicks);
                            break;
                        case "Left":
                            bar.renderOnSide(this, minecraft, posX + 28, posY + DEVICE_HEIGHT - 28, mouseX, mouseY, partialTicks);
                            break;
                        case "Right":
                            bar.renderOnSide(this, minecraft, posX + BORDER, posY + DEVICE_HEIGHT - 28, mouseX, mouseY, partialTicks);
                            break;
                    }

                    if (context != null) {
                        context.render(this, minecraft, context.xPosition, context.yPosition, mouseX, mouseY, true, partialTicks);
                    }

                    super.render(mouseX, mouseY, partialTicks);
                } else {
                    fill(posX + BORDER, posY + BORDER, posX + DEVICE_WIDTH - BORDER, posY + DEVICE_HEIGHT - BORDER, 0x7F000000);
                    GlStateManager.pushMatrix();
                    StringBuilder s;
                    if (this.konamiProgress == -1) {
                        s = new StringBuilder("Shutting up, up, down, down, left, right, left, right, B, A...");
                    } else if (this.konamiProgress == 0) {
                        s = new StringBuilder("Shutting " + codeToName.get(this.lastCode) + "...");
                    } else {
                        s = new StringBuilder("Shutting ");
                        for (int i = 0; i < this.konamiProgress - 1; i++) {
                            s.append(codeToName.get(konamiCodes[i])).append(", ");
                        }
                        s.append(codeToName.get(konamiCodes[this.konamiProgress - 1])).append("...");
                    }
                    int w = this.minecraft.fontRenderer.getStringWidth(s.toString());
                    float scale = 3;
                    while (scale > 1 && w * scale > DEVICE_WIDTH) {
                        scale = scale - 0.5f;
                    }
                    GlStateManager.scalef(scale, scale, 1);
                    GlStateManager.translatef((posX + (DEVICE_WIDTH - w * scale) / 2) / scale, (posY + (DEVICE_HEIGHT - 8 * scale) / 2) / scale, 0);
                    this.minecraft.fontRenderer.drawStringWithShadow(TextFormatting.ITALIC + s.toString(), 0, 0, 0xFFFFFFFF);
                    GlStateManager.popMatrix();
                }
            }
        }

        if (DEVICE_WIDTH > this.width || DEVICE_HEIGHT > this.height) {
            GlStateManager.scalef(2f, 2f, 2f);
        }
    }

    @Override
    public void openApplication(AppInfo info) {
        openApplication(info, (CompoundNBT) null);
    }

    @Override
    public void openApplication(AppInfo info, CompoundNBT intentTag) {
        Optional<Application> optional = APPLICATIONS.stream().filter(app -> app.getInfo() == info).findFirst();
        optional.ifPresent(application -> openApplication(application, intentTag));
    }

    private void openApplication(Application app, CompoundNBT intent) {
        if (!isApplicationInstalled(app.getInfo()))
            return;

        if (!isValidApplication(app.getInfo()))
            return;

        if (sendApplicationToFront(app.getInfo()))
            return;

        Window<Application> window = new Window<>(app, this);
        window.init((width - SCREEN_WIDTH) / 2, (height - SCREEN_HEIGHT) / 2, intent);

        if (appData.contains(app.getInfo().getFormattedId())) {
            app.load(appData.getCompound(app.getInfo().getFormattedId()));
        }

        if (app instanceof SystemApplication) {
            ((SystemApplication) app).setLaptop(this);
        }

        if (app.getCurrentLayout() == null) {
            app.restoreDefaultLayout();
        }

        window.setPosition((SCREEN_WIDTH - app.getWidth()) / 2, (SCREEN_HEIGHT - app.getHeight()) / 2 - TaskBar.BAR_HEIGHT);
        addWindow(window);

        Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    @Override
    public boolean openApplication(AppInfo info, File file) {
        if (!isApplicationInstalled(info))
            return false;

        if (!isValidApplication(info))
            return false;

        Optional<Application> optional = APPLICATIONS.stream().filter(app -> app.getInfo() == info).findFirst();
        if (optional.isPresent()) {
            Application application = optional.get();
            boolean alreadyRunning = isApplicationRunning(info);
            openApplication(application, null);
            if (isApplicationRunning(info)) {
                if (!application.handleFile(file)) {
                    if (!alreadyRunning) {
                        closeApplication(application);
                    }
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private Application getRunningApplication(AppInfo info) {
        for (Window window : windows) {
            if (window != null && window.getContent() instanceof Application) {
                Application application = (Application) window.getContent();
                if (application.getInfo() == info) {
                    return application;
                }
            }
        }
        return null;
    }

    @Override
    public void closeApplication(AppInfo info) {
        Application application = getRunningApplication(info);
        if (application != null) {
            closeApplication(application);
        }
    }

    private void closeApplication(Application app) {
        for (int i = 0; i < windows.length; i++) {
            Window window = windows[i];
            if (window != null && window.getContent() instanceof Application) {
                if (((Application) window.getContent()).getInfo().equals(app.getInfo())) {
                    if (app.isDirty()) {
                        CompoundNBT container = new CompoundNBT();
                        app.save(container);
                        app.clean();
                        appData.put(app.getInfo().getFormattedId(), container);
                        TaskManager.sendTask(new TaskUpdateApplicationData(pos, app.getInfo().getFormattedId(), container));
                    }

                    if (app instanceof SystemApplication) {
                        ((SystemApplication) app).setLaptop(null);
                    }

                    window.handleClose();
                    windows[i] = null;
                    return;
                }
            }
        }
    }

    public boolean sendApplicationToFront(AppInfo info) {
        for (int i = 0; i < windows.length; i++) {
            Window window = windows[i];
            if (window != null && window.getContent() instanceof Application && ((Application) window.getContent()).getInfo() == info) {
                windows[i] = null;
                updateWindowStack();
                windows[0] = window;
                return true;
            }
        }
        return false;
    }

    private void addWindow(Window<Application> window) {
        if (hasReachedWindowLimit())
            return;

        updateWindowStack();
        windows[0] = window;
    }

    private void updateWindowStack() {
        for (int i = windows.length - 1; i >= 0; i--) {
            if (windows[i] != null) {
                if (i + 1 < windows.length) {
                    if (i == 0 || windows[i - 1] != null) {
                        if (windows[i + 1] == null) {
                            windows[i + 1] = windows[i];
                            windows[i] = null;
                        }
                    }
                }
            }
        }
    }

    private boolean hasReachedWindowLimit() {
        for (Window window : windows) {
            if (window == null) return false;
        }
        return true;
    }

    private boolean isMouseOnScreen(int mouseX, int mouseY) {
        int posX = (width - SCREEN_WIDTH) / 2;
        int posY = (height - SCREEN_HEIGHT) / 2;
        return RenderUtil.isMouseInside(mouseX, mouseY, posX, posY, posX + SCREEN_WIDTH, posY + SCREEN_HEIGHT);
    }

    private boolean isMouseWithinWindowBar(int mouseX, int mouseY, Window window) {
        if (window == null)
            return false;
        int posX = (width - SCREEN_WIDTH) / 2;
        int posY = (height - SCREEN_HEIGHT) / 2;
        return RenderUtil.isMouseInside(mouseX, mouseY, posX + window.getOffsetX() + 1, posY + window.getOffsetY() + 1, posX + window.getOffsetX() + window.getWidth() - 13, posY + window.getOffsetY() + 11);
    }

    private boolean isMouseWithinWindow(int mouseX, int mouseY, Window window) {
        if (window == null) return false;
        int posX = (width - SCREEN_WIDTH) / 2;
        int posY = (height - SCREEN_HEIGHT) / 2;
        return RenderUtil.isMouseInside(mouseX, mouseY, posX + window.getOffsetX(), posY + window.getOffsetY(), posX + window.getOffsetX() + window.getWidth(), posY + window.getOffsetY() + window.getHeight());
    }

    public boolean isApplicationRunning(AppInfo info) {
        for (Window window : windows) {
            if (window != null && ((Application) window.getContent()).getInfo() == info) {
                return true;
            }
        }
        return false;
    }

    public void nextTheme() {
        if (currentTheme + 1 < THEMES.size()) {
            currentTheme++;
        }
    }

    public void prevTheme() {
        if (currentTheme - 1 >= 0) {
            currentTheme--;
        }
    }

    @Nullable
    public Application getApplication(String appId) {
        return APPLICATIONS.stream().filter(app -> app.getInfo().getFormattedId().equals(appId)).findFirst().orElse(null);
    }

    @Override
    public Collection<ThemeInfo> getInstalledThemes() {
        return null;
    }

    @Override
    public List<AppInfo> getInstalledApplications() {
        return ImmutableList.copyOf(installedApps);
    }

    public boolean isApplicationInstalled(AppInfo info) {
        return info.isSystemApp() || installedApps.contains(info);
    }

    private boolean isValidApplication(AppInfo info) {
        if (HuskysGadgetMod.setup.hasAllowedApplications()) {
            return HuskysGadgetMod.setup.getAllowedApplications().contains(info);
        }
        return true;
    }

    public void installApplication(AppInfo info, @Nullable Callback<Object> callback) {
        if (!isValidApplication(info))
            return;

        Task task = new TaskInstallApp(info, pos, true);
        task.setCallback((tagCompound, success) ->
        {
            if (success) {
                installedApps.add(info);
                installedApps.sort(AppInfo.SORT_NAME);
            }
            if (callback != null) {
                callback.execute(null, success);
            }
        });
        TaskManager.sendTask(task);
    }

    public void removeApplication(AppInfo info, @Nullable Callback<Object> callback) {
        if (!isValidApplication(info))
            return;

        Task task = new TaskInstallApp(info, pos, false);
        task.setCallback((tagCompound, success) ->
        {
            if (success) {
                installedApps.remove(info);
            }
            if (callback != null) {
                callback.execute(null, success);
            }
        });
        TaskManager.sendTask(task);
    }

    public Settings getSettings() {
        return settings;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void openContext(Layout layout, int x, int y) {
        layout.updateComponents(x, y);
        context = layout;
        layout.init();
    }

    @Override
    public boolean hasContext() {
        return context != null;
    }

    @Override
    public void closeContext() {
        context = null;
        dragging = false;
    }

    private boolean isMouseInHusky(int mouseX, int mouseY) {
        if (this.bootTimer > BOOT_ON_TIME - 20 || this.blinkTimer > 0) {
            return false;
        }
        int posX = (width - DEVICE_WIDTH) / 2;
        int posY = (height - DEVICE_HEIGHT) / 2;
        int cX = posX + DEVICE_WIDTH / 2;
        int cY = posY + DEVICE_HEIGHT / 2;
        return RenderUtil.isMouseInside(mouseX, mouseY, cX - 34, cY - 80, cX + 34, cY + 10);
    }

    public void shutdown() {
        this.bootTimer = BOOT_OFF_TIME;
        this.bootMode = BootMode.SHUTTING_DOWN;
    }

    public enum BootMode {

        BOOTING,
        NOTHING,
        SHUTTING_DOWN,
        BIOS,
        RESTARTING,
        SLEEPING,
        BIOS_SETTINGS;

        public static BootMode getBootMode(int i) {
            return (i >= 0 && i < values().length) ? values()[i] : null;
        }

        public static int ordinal(BootMode bm) {
            return bm != null ? bm.ordinal() : -1;
        }
    }

    public static class EasterEggToast implements IToast {

        private boolean hasPlayedSound = false;

        @Override
        @MethodsReturnNonnullByDefault
        @ParametersAreNonnullByDefault
        public Visibility draw(ToastGui toastGui, long delta) {
            toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
            GlStateManager.color3f(1.0F, 1.0F, 1.0F);
            toastGui.blit(0, 0, 0, 0, 160, 32);

            int i = 16776960;

            String s = "Easter egg found ;p";
            int w = toastGui.getMinecraft().fontRenderer.getStringWidth(s);
            toastGui.getMinecraft().fontRenderer.drawString(s, 80 - w / 2, 12, i | -16777216);

            if (!this.hasPlayedSound && delta > 0L) {
                this.hasPlayedSound = true;
            }

            RenderHelper.enableGUIStandardItemLighting();
            return delta >= 5000L ? IToast.Visibility.HIDE : IToast.Visibility.SHOW;
        }
    }

}
