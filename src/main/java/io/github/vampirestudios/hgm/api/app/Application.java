package io.github.vampirestudios.hgm.api.app;

import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.api.AppInfo;
import io.github.vampirestudios.hgm.api.io.File;
import io.github.vampirestudios.hgm.core.BaseDevice;
import io.github.vampirestudios.hgm.core.TaskBar;
import io.github.vampirestudios.hgm.core.Window;
import io.github.vampirestudios.hgm.core.Wrappable;
import io.github.vampirestudios.hgm.core.io.FileSystem;
import io.github.vampirestudios.hgm.utils.GLHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

/**
 * The abstract base class for creating applications.
 */
public abstract class Application extends Wrappable {

    protected final AppInfo info = null;
    protected Layout defaultLayout = new Layout();
    private BlockPos laptopPositon;
    private int width, height;
    private boolean resizable = false;
    private boolean decorated = true;
    private int minimumWidth = 21;
    private int minimumHeight = 1;
    private int maximumWidth = BaseDevice.SCREEN_WIDTH;
    private int maximumHeight = BaseDevice.SCREEN_HEIGHT - TaskBar.BAR_HEIGHT;
    private Layout currentLayout;

    /**
     * If set to true, will update NBT data for Application
     */
    private boolean needsDataUpdate = false;

    /**
     * If set to true, will update layout
     */
    private boolean pendingLayoutUpdate = false;

    public AppInfo getInfo() {
        return info;
    }

    /**
     * Adds a component to the default layout. Don't get this confused with your
     * custom layout. You should use
     * {@link Layout#addComponent(Component)}
     * instead.
     *
     * @param c the component to add to the default layout
     */
    protected final void addComponent(Component c) {
        if (c != null) {
            defaultLayout.addComponent(c);
        }
    }

    @Override
    public boolean resize(int width, int height) {
        if (!resizable)
            return false;
        this.width = MathHelper.clamp(width, 21, 362);
        this.height = MathHelper.clamp(height, 1, 164);
        return true;
    }

    /**
     * Gets the current layout being displayed
     *
     * @return the current layout
     */
    public final Layout getCurrentLayout() {
        return currentLayout;
    }

    /**
     * Sets the current layout of the application.
     *
     * @param layout
     */
    public final void setCurrentLayout(Layout layout) {
        if (currentLayout != null) {
            currentLayout.handleUnload();
        }
        this.currentLayout = layout;
        this.width = layout.width;
        this.height = layout.height;
        this.pendingLayoutUpdate = true;
        this.currentLayout.handleLoad();
    }

    /**
     * Gets the default layout.
     *
     * @return The default layout
     */
    public Layout getDefaultLayout() {
        return defaultLayout;
    }

    /**
     * Restores the current layout to the default layout
     */
    public final void restoreDefaultLayout() {
        this.setCurrentLayout(defaultLayout);
    }

    /**
     * The default initialization method. Clears any components in the default
     * layout and sets it as the current layout. If you override this method and
     * are using the default layout, make sure you call it using
     * <code>super.init(x, y)</code>
     * <p>
     * The parameters passed are the x and y location of the top left corner or
     * your application window.
     */
    @Override
    public abstract void init(@Nullable CompoundNBT intent);

    /**
     * Called when the content is resized.
     *
     * @param width  The new width of the window
     * @param height The new height of the window
     */
    @Override
    public void onResize(int width, int height) {
        defaultLayout.width = width;
        defaultLayout.height = height;
    }

    @Override
    public void onTick() {
        currentLayout.handleTick();
    }

    // TODO Remove laptop instance

    /**
     * @param laptop
     * @param mc
     * @param x
     * @param y
     * @param mouseX
     * @param mouseY
     * @param active
     * @param partialTicks
     */
    @Override
    public void render(BaseDevice laptop, Minecraft mc, int x, int y, int mouseX, int mouseY, boolean active, float partialTicks) {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        GLHelper.pushScissor(x, y, width, height);
        currentLayout.render(laptop, mc, x, y, mouseX, mouseY, active, partialTicks);
        GLHelper.popScissor();

        if (!GLHelper.isScissorStackEmpty()) {
            HuskysGadgetMod.LOGGER.error("ERROR: A component is not popping it's scissor!");
        }
        GLHelper.clearScissorStack();

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        currentLayout.renderOverlay(laptop, mc, mouseX, mouseY, active);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
        RenderHelper.disableStandardItemLighting();
    }

    /**
     * Called when you press a mouse button. Note if you override, make sure you
     * call this super method.
     *
     * @param mouseX      the current x position of the mouse
     * @param mouseY      the current y position of the mouse
     * @param mouseButton the clicked mouse button
     */
    @Override
    public void handleMouseClick(int mouseX, int mouseY, int mouseButton) {
        currentLayout.handleMouseClick(mouseX, mouseY, mouseButton);
    }

    /**
     * Called when you drag the mouse with a button pressed down Note if you
     * override, make sure you call this super method.
     *
     * @param mouseX      the current x position of the mouse
     * @param mouseY      the current y position of the mouse
     * @param mouseButton the pressed mouse button
     */
    @Override
    public void handleMouseDrag(int mouseX, int mouseY, int mouseButton) {
        currentLayout.handleMouseDrag(mouseX, mouseY, mouseButton);
    }

    /**
     * Called when you release the currently pressed mouse button. Note if you
     * override, make sure you call this super method.
     *
     * @param mouseX      the x position of the release
     * @param mouseY      the y position of the release
     * @param mouseButton the button that was released
     */
    @Override
    public void handleMouseRelease(int mouseX, int mouseY, int mouseButton) {
        currentLayout.handleMouseRelease(mouseX, mouseY, mouseButton);
    }

    /**
     * Called when you scroll the wheel on your mouse. Note if you override,
     * make sure you call this super method.
     *
     * @param mouseX    the x position of the mouse
     * @param mouseY    the y position of the mouse
     * @param direction the direction of the scroll. true is up, false is down
     */
    @Override
    public void handleMouseScroll(int mouseX, int mouseY, boolean direction) {
        currentLayout.handleMouseScroll(mouseX, mouseY, direction);
    }

    /**
     * Called when a key is typed from your keyboard. Note if you override, make
     * sure you call this super method.
     *
     * @param character the typed character
     * @param code      the typed character code
     */
    @Override
    public void handleKeyTyped(char character, int code) {
        currentLayout.handleKeyTyped(character, code);
    }

    @Override
    public void handleKeyReleased(char character, int code) {
        currentLayout.handleKeyReleased(character, code);
    }

    // TODO: Remove from here and put into core

    /**
     * Updates the components of the current layout to adjust to new window
     * position. There is really be no reason to call this method.
     *
     * @param x
     * @param y
     */
    @Override
    public final void updateComponents(int x, int y) {
        currentLayout.updateComponents(x, y);
    }

    /**
     * Called when the application is closed
     */
    @Override
    public void onClose() {
        defaultLayout.clear();
        currentLayout = null;
    }

    /**
     * Called when you first load up your application. Allows you to read any
     * stored data you have saved. Only called if you have saved data. This
     * method is called after {{@link Wrappable#init(CompoundNBT)} so you can update any
     * Components with this data.
     *
     * @param tagCompound the tag compound where you saved data is
     */
    public abstract void load(CompoundNBT tagCompound);

    /**
     * Allows you to save data from your application. This is only called if
     * {@link #isDirty()} returns true. You can mark your application as dirty
     * by calling {@link #markDirty()}.
     *
     * @param tagCompound the tag compound to save your data to
     */
    public abstract void save(CompoundNBT tagCompound);

    /**
     * Sets the defaults layout width. It should be noted that the width must be
     * within 20 to 362.
     *
     * @param width the width
     */
    protected final void setDefaultWidth(int width) {
        if (width < 20) throw new IllegalArgumentException("Width must be larger than 20");
        this.defaultLayout.width = width;
    }

    /**
     * Sets the defaults layout height. It should be noted that the height must
     * be within 20 to 164.
     *
     * @param height the height
     */
    protected final void setDefaultHeight(int height) {
        if (height < 20) throw new IllegalArgumentException("Height must be larger than 20");
        this.defaultLayout.height = height;
    }

    /**
     * Marks that data in this application has changed and needs to be saved.
     * You must call this otherwise your data wont be saved!
     */
    protected void markDirty() {
        needsDataUpdate = true;
    }

    /**
     * Gets if this application is pending for it's data to be saved.
     *
     * @return if currently requiring data to be saved
     */
    public final boolean isDirty() {
        return needsDataUpdate;
    }

    /**
     * Cancels the data saving for this application
     */
    public final void clean() {
        needsDataUpdate = false;
    }

    public final void markForLayoutUpdate() {
        this.pendingLayoutUpdate = true;
    }

    /**
     * Gets if a layout is currently pending a layout update
     *
     * @return if pending layout update
     */
    @Override
    public final boolean isPendingLayoutUpdate() {
        return pendingLayoutUpdate;
    }

    /**
     * Clears the pending layout update
     */
    @Override
    public final void clearPendingLayout() {
        this.pendingLayoutUpdate = false;
    }

    /**
     * Gets the whether or not the content (application/dialog) should have the borders and title.
     *
     * @return if the content should render borders and title
     */
    @Override
    public boolean isDecorated() {
        return decorated;
    }

    public void setDecorated(boolean decorated) {
        this.decorated = decorated;
    }

    /**
     * Gets the whether or not the content (application/dialog) can be resized.
     *
     * @return if the content can be resized
     */
    @Override
    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    /**
     * Gets the width of this application including the border.
     *
     * @return the height
     */
    @Override
    public final int getWidth() {
        return width;
    }

    /**
     * Gets the height of this application including the title bar.
     *
     * @return the height
     */
    @Override
    public final int getHeight() {
        return height;
    }

    /**
     * Gets the text in the title bar of the application. You can change the text by setting a custom title for your layout. See {@link Layout#setTitle}.
     *
     * @return The display name
     */
    @Override
    public String getWindowTitle() {
        if (currentLayout.hasTitle()) {
            return currentLayout.getTitle().getFormattedText();
        }
        return info.getName();
    }

    public String getApplicationFolderPath() {
        return FileSystem.DIR_APPLICATION_DATA + "/" + info.getFormattedId();
    }

    /**
     * Gets the active dialog window for this application
     *
     * @return
     */
    @Nullable
    public Window<Dialog> getActiveDialog() {
        Window<Dialog> dialogWindow = getWindow().getDialogWindow();
        if (dialogWindow != null) {
            while (true) {
                if (dialogWindow.getDialogWindow() == null) {
                    return dialogWindow;
                }
                dialogWindow = dialogWindow.getDialogWindow();
            }
        }
        return dialogWindow;
    }

    /**
     * Determines how a file is handled when it's opened with this application. By default, this
     * method returns true, however returning false indicates that the file could not be opened and
     * will prompt the user with a dialog.
     *
     * @param file the file attempting to be opened
     */
    public boolean handleFile(File file) {
        return false;
    }

    public void setMinimumSize(int minimumWidth, int minimumHeight) {
        this.minimumWidth = MathHelper.clamp(minimumWidth, 21, 362);
        this.minimumHeight = MathHelper.clamp(minimumHeight, 1, 164);
    }

    public void setMaximumSize(int maximumWidth, int maximumHeight) {
        if (maximumWidth < this.minimumWidth)
            maximumWidth = this.minimumWidth + 21;
        if (maximumHeight < this.minimumHeight)
            maximumHeight = this.minimumHeight + 1;

        this.maximumWidth = MathHelper.clamp(maximumWidth, 21, 362);
        this.maximumHeight = MathHelper.clamp(maximumHeight, 1, 164);
    }

    /**
     * Check if an application is equal to another. Checking the ID is
     * sufficient as they should be unique.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Application))
            return false;
        Application app = (Application) obj;
        return app.info.getFormattedId().equals(this.info.getFormattedId());
    }

    @Override
    public AppInfo getAppInfo() {
        return info;
    }
}