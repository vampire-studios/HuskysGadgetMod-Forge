package io.github.vampirestudios.hgm.programs.system.layout;

import com.google.common.collect.Lists;
import io.github.vampirestudios.hgm.HuskysGadgetMod;
import io.github.vampirestudios.hgm.api.AppInfo;
import io.github.vampirestudios.hgm.api.app.Layout;
import io.github.vampirestudios.hgm.api.app.component.Button;
import io.github.vampirestudios.hgm.api.app.component.Image;
import io.github.vampirestudios.hgm.api.app.component.Label;
import io.github.vampirestudios.hgm.api.app.component.*;
import io.github.vampirestudios.hgm.api.app.emojies.Icons;
import io.github.vampirestudios.hgm.api.utils.RenderUtil;
import io.github.vampirestudios.hgm.core.BaseDevice;
import io.github.vampirestudios.hgm.programs.system.ApplicationAppStore;
import io.github.vampirestudios.hgm.programs.system.object.AppEntry;
import io.github.vampirestudios.hgm.programs.system.object.LocalAppEntry;
import io.github.vampirestudios.hgm.programs.system.object.RemoteAppEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.net.URI;
import java.net.URL;

public class LayoutAppPage extends Layout {

    private BaseDevice laptop;
    private AppEntry entry;
    private ApplicationAppStore store;

    private Image imageBanner;
    private Image imageIcon;
    private Label labelTitle;
    private Label labelVersion;

    private boolean installed;

    public LayoutAppPage(BaseDevice laptop, AppEntry entry, ApplicationAppStore store)
    {
        super(250, 150);
        this.laptop = laptop;
        this.entry = entry;
        this.store = store;
    }

    @Override
    public void init()
    {
        if(entry instanceof LocalAppEntry)
        {
        	installed = BaseDevice.getSystem().getInstalledApplications().contains(((LocalAppEntry) entry).getInfo());
        }

        this.setBackground((gui, mc, x, y, width, height, mouseX, mouseY, windowActive) ->
        {
            Color color = new Color(BaseDevice.getSystem().getSettings().getColourScheme().getBackgroundColour());
            fill(x, y + 40, x + width, y + 41, color.brighter().getRGB());
            fill(x, y + 41, x + width, y + 60, color.getRGB());
            fill(x, y + 60, x + width, y + 61, color.darker().getRGB());
        });

        ResourceLocation resource = new ResourceLocation(entry.getId());

        imageBanner = new Image(0, 0, 250, 40);
        imageBanner.setDrawFull(true);
        if(entry instanceof LocalAppEntry)
        {
            imageBanner.setImage(new ResourceLocation(resource.getNamespace(), "textures/app/banner/banner_" + resource.getPath() + ".png"));
        }
        else if(entry instanceof RemoteAppEntry)
        {
            imageBanner.setImage(ApplicationAppStore.CERTIFIED_APPS_URL + "/assets/" + resource.getNamespace() + "/" + resource.getPath() + "/banner.png");
        }
        this.addComponent(imageBanner);

        if(entry instanceof LocalAppEntry)
        {
            LocalAppEntry localEntry = (LocalAppEntry) entry;
            AppInfo info = localEntry.getInfo();
            imageIcon = new Image(5, 26, 28, 28, info.getIconU(), info.getIconV(), 14, 14, 224, 224, BaseDevice.ICON_TEXTURES);
        }
        else if(entry instanceof RemoteAppEntry)
        {
            imageIcon = new Image(5, 26, 28, 28, ApplicationAppStore.CERTIFIED_APPS_URL + "/assets/" + resource.getNamespace() + "/" + resource.getPath() + "/icon.png");
        }
        this.addComponent(imageIcon);

        if(store.certifiedApps.contains(entry))
        {
            int width = BaseDevice.fontRenderer.getStringWidth(entry.getName()) * 2;
            Image certifiedIcon = new Image(38 + width + 3, 29, 20, 20, Icons.VERIFIED);
            this.addComponent(certifiedIcon);
        }
        labelTitle = new Label(entry.getName(), 38, 32);
        labelTitle.setScale(2);
        this.addComponent(labelTitle);

        String version = entry instanceof LocalAppEntry ? "v" + entry.getVersion() + " - " + entry.getAuthor() : entry.getAuthor();
        labelVersion = new Label(version, 38, 50);
        this.addComponent(labelVersion);

        /*String description = GitWebFrame.parseFormatting(entry.getDescription());
        ScrollableLayout descriptionLayout = ScrollableLayout.create(130, 67, 115, 78, description);
        this.addComponent(descriptionLayout);*/

        SlideShow slideShow = new SlideShow(5, 67, 120, 78);
        if(entry instanceof LocalAppEntry)
        {
            if(entry.getScreenshots() != null)
            {
                for(String image : entry.getScreenshots())
                {
                    if(image.startsWith("http://") || image.startsWith("https://"))
                    {
                        slideShow.addImage(image);
                    }
                    else
                    {
                        slideShow.addImage(new ResourceLocation(image));
                    }
                }
            }
        }
        else if(entry instanceof RemoteAppEntry) {
            RemoteAppEntry remoteEntry = (RemoteAppEntry) entry;
            String screenshotUrl = ApplicationAppStore.CERTIFIED_APPS_URL + "/assets/" + resource.getNamespace() + "/" + resource.getPath() + "/screenshots/screenshot_%d.png";
            for(int i = 0; i < remoteEntry.app_screenshots; i++) {
                slideShow.addImage(String.format(screenshotUrl, i));
            }
        }
        this.addComponent(slideShow);

        if(entry instanceof LocalAppEntry) {
            AppInfo info = ((LocalAppEntry) entry).getInfo();
            Button btnInstall = new Button(174, 44, installed ? "Delete" : "Install", installed ? Icons.CROSS : Icons.PLUS);
            btnInstall.setSize(55, 14);
            btnInstall.setClickListener((mouseX, mouseY, mouseButton) -> {
                if(mouseButton == 0) {
                    if(installed) {
                        laptop.removeApplication(info, (o, success) -> {
                        	if(success) {
	                            btnInstall.setText("Install");
	                            btnInstall.setIcon(Icons.PLUS);
	                            installed = false;
                        	}
                        });
                    }
                    else {
                        laptop.installApplication(info, (o, success) -> {
                        	if(success) {
	                            btnInstall.setText("Delete");
	                            btnInstall.setIcon(Icons.CROSS);
	                            installed = true;
                        	}
                        });
                    }
                }
            });
            this.addComponent(btnInstall);

            if(laptop.isApplicationInstalled(info)) {
                Button btnOpen = new Button(234, 44, Icons.PLAY);
                btnOpen.setToolTip("Start", "Starts the application");
                btnOpen.setClickListener((mouseX, mouseY, mouseButton) -> laptop.openApplication(info));
                this.addComponent(btnOpen);
            }

            if (info.getSupport() != null)
            {
                AppInfo.Support support = info.getSupport();
                int xOffset = 0;
                if (support.getPaypal() != null)
                {
                    Button btnDonate = new Button(174, 22, Icons.CREDIT_CARD);
                    btnDonate.setBackground(false);
                    btnDonate.setToolTip("PayPal", "Opens a link to donate to author of the application on paypal");
                    btnDonate.setSize(14, 14);
                    btnDonate.setClickListener((mouseX, mouseY, mouseButton) -> {
                        if (mouseButton == 0)
                        {
                            openWebLink(info.getSupport().getPaypal());
                        }
                    });
                    this.addComponent(btnDonate);
                    xOffset++;
                }

                if (support.getPatreon() != null)
                {
                    Button btnDonate = new Button(174 - xOffset * 15, 77, String.format("%s's Patreon", info.getAuthor()), Icons.COIN);
                    btnDonate.setBackground(false);
                    btnDonate.setToolTip("Patreon", "Opens a link to donate to author of the application on patreon");
                    btnDonate.setSize(14, 14);
                    btnDonate.setClickListener((mouseX, mouseY, mouseButton) -> {
                        if (mouseButton == 0)
                        {
                            openWebLink(info.getSupport().getPatreon());
                        }
                    });
                    this.addComponent(btnDonate);
                    xOffset++;
                }

                if (support.getTwitter() != null)
                {
                    Button btnTwitter = new Button(174 - xOffset * 15, 22, Icons.EARTH);
                    btnTwitter.setBackground(false);
                    btnTwitter.setToolTip("Twitter", "Opens a link to follow the author of the application on twitter");
                    btnTwitter.setSize(14, 14);
                    btnTwitter.setClickListener((mouseX, mouseY, mouseButton) -> {
                        if (mouseButton == 0)
                        {
                            openWebLink(info.getSupport().getTwitter());
                        }
                    });
                    this.addComponent(btnTwitter);
                    xOffset++;
                }

                if (support.getYoutube() != null)
                {
                    Button btnYoutube = new Button(174 - xOffset * 15, 22, Icons.VIDEO_CAMERA);
                    btnYoutube.setBackground(false);
                    btnYoutube.setToolTip("Youtube", "Opens a link to subscribe to the author on youtube");
                    btnYoutube.setSize(14, 14);
                    btnYoutube.setClickListener((mouseX, mouseY, mouseButton) -> {
                        if (mouseButton == 0)
                        {
                            openWebLink(info.getSupport().getYoutube());
                        }
                    });

                    this.addComponent(btnYoutube);
                }

                LinkedLabel paypal = new LinkedLabel("Patreon", 200, 22, info.getSupport().getPatreon());
                paypal.setClickListener((mouseX, mouseY, mouseButton) -> {
                    if(mouseButton == 0) {
                        openWebLink(info.getSupport().getPatreon());
                    }
                });
                this.addComponent(paypal);

            }

        }
        else if(entry instanceof RemoteAppEntry) {
            Button btnDownload = new Button(20, 2, "Download", Icons.IMPORT);
            btnDownload.setSize(66, 16);
            btnDownload.setClickListener((mouseX, mouseY, mouseButton) -> this.openWebLink("https://minecraft.curseforge.com/projects/" + ((RemoteAppEntry) entry).project_id));
            this.addComponent(btnDownload);
        }
    }

    @Override
    public void renderOverlay(BaseDevice laptop, Minecraft mc, int mouseX, int mouseY, boolean windowActive)
    {
        super.renderOverlay(laptop, mc, mouseX, mouseY, windowActive);
        int width = BaseDevice.fontRenderer.getStringWidth(entry.getName()) * 2;
        if(RenderUtil.isMouseInside(mouseX, mouseY, xPosition + 38 + width + 3, yPosition + 29, 20, 20))
        {
            if(store.certifiedApps.contains(entry)) {
                laptop.renderTooltip(Lists.newArrayList(TextFormatting.GREEN + "Certified App"), mouseX, mouseY);
            }
        }
    }

    private void openWebLink(String url)
    {
        try
        {
            URI uri = new URL(url).toURI();
            Class<?> class_ = Class.forName("java.awt.Desktop");
            Object object = class_.getMethod("getDesktop").invoke(null);
            class_.getMethod("browse", URI.class).invoke(object, uri);
        }
        catch (Throwable throwable1)
        {
            Throwable throwable = throwable1.getCause();
            HuskysGadgetMod.LOGGER.error("Couldn't open link: {}", throwable == null ? "<UNKNOWN>" : throwable.getMessage());
        }
    }
}