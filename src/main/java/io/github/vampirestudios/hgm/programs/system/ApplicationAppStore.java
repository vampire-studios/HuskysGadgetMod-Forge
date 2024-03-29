package io.github.vampirestudios.hgm.programs.system;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import io.github.vampirestudios.hgm.api.AppInfo;
import io.github.vampirestudios.hgm.api.ApplicationManager;
import io.github.vampirestudios.hgm.api.app.Component;
import io.github.vampirestudios.hgm.api.app.Layout;
import io.github.vampirestudios.hgm.api.app.ScrollableLayout;
import io.github.vampirestudios.hgm.api.app.component.Button;
import io.github.vampirestudios.hgm.api.app.component.Image;
import io.github.vampirestudios.hgm.api.app.component.Label;
import io.github.vampirestudios.hgm.api.app.component.Spinner;
import io.github.vampirestudios.hgm.api.app.emojies.Icons;
import io.github.vampirestudios.hgm.api.utils.OnlineRequest;
import io.github.vampirestudios.hgm.core.BaseDevice;
import io.github.vampirestudios.hgm.object.TrayItem;
import io.github.vampirestudios.hgm.programs.system.component.AppGrid;
import io.github.vampirestudios.hgm.programs.system.layout.LayoutAppPage;
import io.github.vampirestudios.hgm.programs.system.layout.LayoutSearchApps;
import io.github.vampirestudios.hgm.programs.system.object.AppEntry;
import io.github.vampirestudios.hgm.programs.system.object.RemoteAppEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.vampirestudios.hgm.HuskysGadgetMod.MOD_ID;

//@DeviceApplication(modId = MOD_ID, appId = "app_store")
public class ApplicationAppStore extends SystemApplication {

    public static final String CERTIFIED_APPS_URL = "https://raw.githubusercontent.com/sindrefag/GadgetMod-CertifiedApps/master";

    public static final int LAYOUT_WIDTH = 250;
    public static final int LAYOUT_HEIGHT = 150;

    private Layout layoutMain;

    int offset;

    public List<AppEntry> certifiedApps = new ArrayList<>();

    @Override
    public void init(@Nullable CompoundNBT intent) {

        layoutMain = new Layout(LAYOUT_WIDTH, LAYOUT_HEIGHT);

        ScrollableLayout homePageLayout = new ScrollableLayout(0, 0, LAYOUT_WIDTH, 368, LAYOUT_HEIGHT);
        homePageLayout.setScrollSpeed(10);
        homePageLayout.setBackground((gui, mc, x, y, width, height, mouseX, mouseY, windowActive) ->
        {
            Color color = new Color(BaseDevice.getSystem().getSettings().getColourScheme().getBackgroundColour());
            offset = 60;
            Screen.fill(x, y + offset, x + LAYOUT_WIDTH, y + offset + 1, color.brighter().getRGB());
            Screen.fill(x, y + offset + 1, x + LAYOUT_WIDTH, y + offset + 19, color.getRGB());
            Screen.fill(x, y + offset + 19, x + LAYOUT_WIDTH, y + offset + 20, color.darker().getRGB());

            offset = 172;
            Screen.fill(x, y + offset, x + LAYOUT_WIDTH, y + offset + 1, color.brighter().getRGB());
            Screen.fill(x, y + offset + 1, x + LAYOUT_WIDTH, y + offset + 19, color.getRGB());
            Screen.fill(x, y + offset + 19, x + LAYOUT_WIDTH, y + offset + 20, color.darker().getRGB());
        });

        Image imageBanner = new Image(0, 0, LAYOUT_WIDTH, 60);
        imageBanner.setImage(new ResourceLocation(MOD_ID, "textures/gui/app_market_background.png"));
        imageBanner.setDrawFull(true);
        homePageLayout.addComponent(imageBanner);

        Button btnSearch = new Button(214, 2, Icons.SEARCH);
        btnSearch.setToolTip("Search", "Find a specific application");
        btnSearch.setClickListener((mouseX, mouseY, mouseButton) ->
        {
            if (mouseButton == 0) {
                this.setCurrentLayout(new LayoutSearchApps(this, getCurrentLayout()));
            }
        });
        homePageLayout.addComponent(btnSearch);

        Button btnManageApps = new Button(232, 2, Icons.HAMMER);
        btnManageApps.setToolTip("Manage Apps", "Manage your installed applications");
        homePageLayout.addComponent(btnManageApps);

        Image image = new Image(5, 33, 20, 20, Icons.SHOP);
        homePageLayout.addComponent(image);

        Label labelBanner = new Label("App Market", 32, 35);
        labelBanner.setScale(2);
        homePageLayout.addComponent(labelBanner);

        Label labelCertified = new Label(TextFormatting.WHITE + TextFormatting.BOLD.toString() + "Certified Apps", 10, 66);
        homePageLayout.addComponent(labelCertified);

        Label labelCertifiedDesc = new Label(TextFormatting.GRAY + "Verified by HuskyTheArtist", LAYOUT_WIDTH - 10, 66);
        labelCertifiedDesc.setAlignment(Component.ALIGN_RIGHT);
        labelCertifiedDesc.setScale(1.0);
        labelCertifiedDesc.setShadow(false);
        homePageLayout.addComponent(labelCertifiedDesc);

        Spinner spinner = new Spinner((LAYOUT_WIDTH - 12) / 2, 120);
        homePageLayout.addComponent(spinner);

        OnlineRequest.getInstance().make(CERTIFIED_APPS_URL + "/certified_apps.json", (success, response) ->
        {
            certifiedApps.clear();
            spinner.setVisible(false);
            if(success)
            {
                Minecraft.getInstance().execute(() ->
                {
                    AppGrid grid = new AppGrid(0, 81, 3, 1, this);
                    certifiedApps.addAll(parseJson(response));
                    shuffleAndShrink(certifiedApps, 3).forEach(grid::addEntry);
                    homePageLayout.addComponent(grid);
                    grid.reloadIcons();
                });
            }
            //TODO error handling
        });

        Label labelOther = new Label(TextFormatting.WHITE + TextFormatting.BOLD.toString() + "Other Apps", 10, 178);
        homePageLayout.addComponent(labelOther);

        Label labelOtherDesc = new Label(TextFormatting.GRAY + "Community Created", LAYOUT_WIDTH - 10, 178);
        labelOtherDesc.setAlignment(Component.ALIGN_RIGHT);
        labelOtherDesc.setScale(1.0);
        labelOtherDesc.setShadow(false);
        homePageLayout.addComponent(labelOtherDesc);

        AppGrid other = new AppGrid(0, 192, 3, 2, this);
        shuffleAndShrink(ApplicationManager.getAvailableApplications(), 6).forEach(other::addEntry);
        homePageLayout.addComponent(other);

        layoutMain.addComponent(homePageLayout);

        setCurrentLayout(layoutMain);
    }

    @Override
    public void load(CompoundNBT tagCompound) {

    }

    @Override
    public void save(CompoundNBT tagCompound) {

    }

    public List<RemoteAppEntry> parseJson(String json)
    {
        List<RemoteAppEntry> entries = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        Gson gson = new Gson();
        array.forEach(element -> entries.add(gson.fromJson(element, new TypeToken<RemoteAppEntry>(){}.getType())));
        return entries;
    }

    public void openApplication(AppEntry entry)
    {
        Layout layout = new LayoutAppPage(getLaptop(), entry, this);
        this.setCurrentLayout(layout);
        Button btnPrevious = new Button(2, 2, Icons.ARROW_LEFT);
        btnPrevious.setClickListener((mouseX1, mouseY1, mouseButton1) -> this.setCurrentLayout(layoutMain));
        layout.addComponent(btnPrevious);
    }

    private <T> List<T> shuffleAndShrink(List<T> list, int newSize)
    {
        Collections.shuffle(list);
        return list.subList(0, Math.min(list.size(), newSize));
    }

    public static class StoreTrayItem extends TrayItem
    {
        public StoreTrayItem()
        {
            super(Icons.SHOP);
        }

        @Override
        public void handleClick(int mouseX, int mouseY, int mouseButton)
        {
            AppInfo info = ApplicationManager.getApplication("hgm:app_store");
            if(info != null)
            {
                BaseDevice.getSystem().openApplication(info);
            }
        }
    }
}
