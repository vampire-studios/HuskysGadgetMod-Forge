package io.github.vampirestudios.hgm.api.app.component;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.vampirestudios.hgm.api.app.Component;
import io.github.vampirestudios.hgm.api.app.Layout;
import io.github.vampirestudios.hgm.api.utils.RenderUtil;
import io.github.vampirestudios.hgm.core.BaseDevice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public class Banner extends Component {

    private static final Map<String, CachedImage> CACHE = new ImageCache(10);
    protected ImageLoader loader;
    protected CachedImage image;
    protected boolean drawFull = false;
    protected int imageU, imageV;
    protected int imageWidth, imageHeight;
    protected int componentWidth, componentHeight;
    private Spinner spinner;
    private float alpha = 1.0F;

    private boolean hasBorder = false;
    private int borderColour = Color.BLACK.getRGB();
    private int borderThickness = 1;

    /**
     * Creates a new Image using a ResourceLocation. This constructor allows the specification of
     * the width and height of the component instead of automatically unlike
     * {@link Banner#Banner(int, int, int, int, ResourceLocation)}
     *
     * @param left     the amount of pixels to be offset from the left
     * @param top      the amount of pixels to be offset from the top
     * @param imageU   the u position on the image resource
     * @param imageV   the v position on the image resource
     * @param resource the resource location of the image
     */
    public Banner(int left, int top, int imageU, int imageV, ResourceLocation resource) {
        super(left, top);
        this.loader = new StandardLoader(resource);
        this.componentWidth = 250;
        this.componentHeight = 40;
        this.imageU = imageU;
        this.imageV = imageV;
        this.imageWidth = 250;
        this.imageHeight = 40;
    }

    /**
     * Creates a new Image from a url. This allows a resource to be downloaded from the internet
     * and be used as the Image. In the case that the resource could not be downloaded or the player
     * is playing the game in an offline state, the Image will default to a missing texture.
     * <p>
     * It should be noted that the remote resource is cached, so updating it may not result in an
     * instant change. Caching has a default limit of 10 resources but this can be changed by the
     * player in the configuration.
     *
     * @param left the amount of pixels to be offset from the left
     * @param top  the amount of pixels to be offset from the top
     * @param url  the url of the resource
     */
    public Banner(int left, int top, String url) {
        super(left, top);
        this.loader = new DynamicLoader(url);
        this.componentWidth = 250;
        this.componentHeight = 40;
        this.drawFull = true;
    }

    @Override
    public void init(Layout layout) {
        spinner = new Spinner(left + (componentWidth / 2) - 6, top + (componentHeight / 2) - 6);
        layout.addComponent(spinner);
    }

    @Override
    public void handleLoad() {
        loader.setup(this);
    }

    @Override
    public void render(BaseDevice laptop, Minecraft mc, int x, int y, int mouseX, int mouseY, boolean windowActive, float partialTicks) {
        if (this.visible) {
            if (loader.setup) {
                image = loader.load(this);
                spinner.setVisible(false);
                loader.setup = false;
            }

            if (image != null && image.textureId != -1) {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
                GlStateManager.enableAlphaTest();
                GlStateManager.enableBlend();
                GlStateManager.bindTexture(image.textureId);

                if (hasBorder) {
                    fill(xPosition, yPosition, xPosition + componentWidth, yPosition + componentHeight, borderColour);
                    GlStateManager.color4f(1.0F, 1.0F, 1.0F, alpha);
                    if (drawFull) {
                        RenderUtil.drawRectWithFullTexture(xPosition + borderThickness, yPosition + borderThickness, imageU, imageV, componentWidth - borderThickness * 2, componentHeight - borderThickness * 2);
                    } else {
                        RenderUtil.drawRectWithTexture(xPosition + borderThickness, yPosition + borderThickness, imageU, imageV, componentWidth - borderThickness * 2, componentHeight - borderThickness * 2, imageWidth, imageHeight);
                    }
                } else {
                    if (drawFull) {
                        RenderUtil.drawRectWithFullTexture(xPosition, yPosition, imageU, imageV, componentWidth, componentHeight);
                    } else {
                        RenderUtil.drawRectWithTexture(xPosition, yPosition, imageU, imageV, componentWidth, componentHeight, imageWidth, imageHeight);
                    }
                }
            } else {
                fill(xPosition, yPosition, xPosition + componentWidth, yPosition + componentHeight, Color.LIGHT_GRAY.getRGB());
            }
        }

        if (image != null) {
            if (image.delete) {
                GlStateManager.deleteTexture(image.textureId);
                image = null;
            }
        }
    }

    public void reload() {
        loader.setup(this);
    }

    public void setImage(ResourceLocation resource) {
        setLoader(new StandardLoader(resource));
    }

    public void setImage(String url) {
        setLoader(new DynamicLoader(url));
    }

    private void setLoader(ImageLoader loader) {
        this.loader = loader;
        loader.setup(this);
        spinner.setVisible(true);
    }

    /**
     * Sets the alpha for this image. Must be in the range
     * of 0.0F to 1.0F
     *
     * @param alpha how transparent you want it to be.
     */
    public void setAlpha(float alpha) {
        if (alpha < 0.0F) {
            this.alpha = 0.0F;
            return;
        }
        if (alpha > 1.0F) {
            this.alpha = 1.0F;
            return;
        }
        this.alpha = alpha;
    }

    /**
     * Makes it so the border shows
     *
     * @param show should the border show
     */
    public void setBorderVisible(boolean show) {
        this.hasBorder = show;
    }

    /**
     * Sets the border colour for this component
     *
     * @param colour the border colour
     */
    private void setBorderColor(Color colour) {
        this.borderColour = colour.getRGB();
    }

    /**
     * Sets the thickness of the border
     *
     * @param thickness how thick in pixels
     */
    public void setBorderThickness(int thickness) {
        this.borderThickness = thickness;
    }

    /**
     * Image Loader
     */
    private static abstract class ImageLoader {
        protected boolean setup = false;

        public final boolean isSetup() {
            return setup;
        }

        protected void setup(Banner image) {
            setup = false;
        }

        public abstract CachedImage load(Banner image);
    }

    private static class StandardLoader extends ImageLoader {
        private final Texture texture;
        private final String resource;

        public StandardLoader(ResourceLocation resource) {
            this.texture = new SimpleTexture(resource);
            this.resource = resource.toString();
        }

        @Override
        protected void setup(Banner image) {
            setup = true;
        }

        @Override
        public CachedImage load(Banner image) {
            /*if (CACHE.containsKey(resource)) {
                return CACHE.get(resource);
            }

            try {
                texture.loadTexture(Minecraft.getInstance().getResourceManager());
                CachedImage cachedImage = new CachedImage(texture.getGlTextureId(), 0, 0);
                CACHE.put(resource, cachedImage);
                return cachedImage;
            } catch (IOException e) {
                return new CachedImage(TextureUtil.MISSING_TEXTURE.getGlTextureId(), 0, 0);
            }*/
            return null;
        }
    }

    private static class DynamicLoader extends ImageLoader {
        private Texture texture;
        private String url;

        public DynamicLoader(String url) {
            this.url = url;
        }

        @Override
        public void setup(final Banner image) {
            if (CACHE.containsKey(url)) {
                setup = true;
                return;
            }
            Runnable r = () ->
            {
                try {
                    BufferedImage bufferedImage = ImageIO.read(new URL(url));
                    image.imageWidth = bufferedImage.getWidth();
                    image.imageHeight = bufferedImage.getHeight();
                    texture = new DynamicTexture(bufferedImage);
                    setup = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
            Thread thread = new Thread(r, "Image Loader");
            thread.start();
        }

        @Override
        public CachedImage load(Banner image) {
            /*if (CACHE.containsKey(url)) {
                CachedImage cachedImage = CACHE.get(url);
                image.imageWidth = cachedImage.width;
                image.imageHeight = cachedImage.height;
                return cachedImage;
            }

            try {
                texture.loadTexture(Minecraft.getInstance().getResourceManager());
                CachedImage cachedImage = new CachedImage(texture.getGlTextureId(), image.imageWidth, image.imageHeight);
                CACHE.put(url, cachedImage);
                return cachedImage;
            } catch (IOException e) {
                return new CachedImage(TextureUtil.MISSING_TEXTURE.getGlTextureId(), 0, 0);
            }*/
            return null;
        }
    }

    private static class DynamicTexture extends Texture {
        private BufferedImage image;

        private DynamicTexture(BufferedImage image) {
            this.image = image;
        }

        @Override
        public void loadTexture(IResourceManager resourceManager) throws IOException {
//            TextureUtil.uploadTextureImageAllocate(getGlTextureId(), image, false, true);
        }
    }

    private static class ImageCache extends LinkedHashMap<String, CachedImage> {
        private final int CAPACITY;

        private ImageCache(final int capacity) {
            super(capacity, 0.75F, true);
            this.CAPACITY = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<String, CachedImage> eldest) {
            if (size() > CAPACITY) {
                eldest.getValue().delete = true;
                return true;
            }
            return false;
        }
    }

    private static class CachedImage {
        private final int textureId;
        private final int width;
        private final int height;
        private boolean delete = false;

        private CachedImage(int textureId, int width, int height) {
            this.textureId = textureId;
            this.width = width;
            this.height = height;
        }
    }
}
