package com.rastiq.arkemys;

import com.rastiq.arkemys.config.ConfigManager;
import com.rastiq.arkemys.config.ModuleConfig;
import com.rastiq.arkemys.cosmetics.CosmeticManager;
import com.rastiq.arkemys.discord.DiscordIPC;
import com.rastiq.arkemys.features.ModuleManager;
import com.rastiq.arkemys.features.SettingsManager;
import com.rastiq.arkemys.features.modules.HotbarModule;
import com.rastiq.arkemys.features.modules.PerspectiveModule;
import com.rastiq.arkemys.features.modules.utils.CPSUtils;
import com.rastiq.arkemys.features.modules.utils.IModuleRenderer;
import com.rastiq.arkemys.gui.settings.GuiHUDEditor;
import com.rastiq.arkemys.gui.settings.GuiModules;
import com.rastiq.arkemys.gui.settings.GuiSettings;
import com.rastiq.arkemys.gui.settings.SettingsBase;
import com.rastiq.arkemys.gui.utils.GuiUtils;
import com.rastiq.arkemys.gui.utils.blur.BlurShader;
import com.rastiq.arkemys.splash.SplashProgress;
import com.rastiq.arkemys.utils.BoxUtils;
import com.rastiq.arkemys.utils.CustomFontRenderer;
import org.apache.logging.log4j.Logger;

import java.io.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import com.rastiq.arkemys.discord.*;
import com.rastiq.arkemys.cosmetics.*;
import net.minecraft.client.gui.*;
import com.rastiq.arkemys.features.modules.*;
import com.rastiq.arkemys.gui.utils.blur.*;
import com.rastiq.arkemys.gui.settings.*;
import com.rastiq.arkemys.config.*;
import com.rastiq.arkemys.features.modules.utils.*;
import com.rastiq.arkemys.utils.*;
import com.rastiq.arkemys.features.*;
import com.rastiq.arkemys.gui.utils.*;
import java.util.regex.*;
import org.apache.logging.log4j.*;

public class Client
{
    public static final Client INSTANCE;
    public static final Logger LOGGER;
    public static final String minecraftVersion = "1.8.9";
    public static final File dir;
    private final Minecraft mc;
    public static final ResourceLocation LOGO;
    public static final ResourceLocation BACKGROUND;
    public static final ResourceLocation TRANSPARENT;
    public static CustomFontRenderer titleRenderer;
    public static CustomFontRenderer titleRenderer2;
    public static CustomFontRenderer textRenderer;
    public static final CPSUtils left;
    public static final CPSUtils right;
    
    public Client() {
        this.mc = Minecraft.getMinecraft();
    }
    
    public void onPostInit() {
        Client.titleRenderer = new CustomFontRenderer("Lato Bold", 16.0f);
        Client.titleRenderer2 = new CustomFontRenderer("Lato Black", 16.0f);
        Client.textRenderer = new CustomFontRenderer("Lato Light", 16.0f);
        SplashProgress.setProgress(6, "Starting RPC...");
        DiscordIPC.INSTANCE.init();
        SplashProgress.setProgress(7, "Starting Modules...");
        ModuleManager.INSTANCE.init();
        SplashProgress.setProgress(8, "Initialising config...");
        ConfigManager.INSTANCE.loadAll();
        SplashProgress.setProgress(9, "Loading Cosmetics...");
        new Thread(CosmeticManager::init, "Cosmetic Fetcher").start();
    }
    
    public void onKeyPress(final int keycode) {
        if (Minecraft.getMinecraft().inGameHasFocus && keycode == 54) {
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen)((SettingsBase.lastWindow == null) ? new GuiModules(null) : SettingsBase.lastWindow));
        }
    }
    
    public void onRenderOverlay() {
        if (this.mc.gameSettings.showDebugInfo || this.mc.currentScreen instanceof GuiHUDEditor) {
            return;
        }
        if (ModuleConfig.INSTANCE.isEnabled(PerspectiveModule.INSTANCE)) {
            PerspectiveModule.INSTANCE.onTick();
        }
        HotbarModule.INSTANCE.onTick();
        BlurShader.INSTANCE.onRenderTick();
        if (this.mc.currentScreen instanceof GuiModules || this.mc.currentScreen instanceof GuiSettings) {
            return;
        }
        ModuleManager.INSTANCE.modules.stream().filter(module -> ModuleConfig.INSTANCE.isEnabled(module) && module.isRender()).forEach(module -> ((IModuleRenderer)module).render(BoxUtils.getBoxOffX(module, (int)ModuleConfig.INSTANCE.getActualX(module), ((IModuleRenderer)module).getWidth()), BoxUtils.getBoxOffY(module, (int)ModuleConfig.INSTANCE.getActualY(module), ((IModuleRenderer)module).getHeight())));
        Client.left.tick();
        Client.right.tick();
    }
    
    public void onShutdown() {
        DiscordIPC.INSTANCE.shutdown();
    }
    
    public static int getMainColor(final int alpha) {
        return GuiUtils.getRGB(SettingsManager.INSTANCE.mainColor.getColor(), alpha);
    }
    
    public static void info(final Object msg, final Object... objs) {
        Client.LOGGER.info("[Arkemys Client] " + msg, objs);
    }
    
    public static void debug(final Object msg, final Object... objs) {
        Client.LOGGER.debug("[Arkemys Client] " + msg, objs);
    }
    
    public static void warn(final Object msg, final Object... objs) {
        Client.LOGGER.warn("[Arkemys Client] " + msg, objs);
    }
    
    public static void error(final Object msg, final Object... objs) {
        Client.LOGGER.error("[Arkemys Client] " + msg, objs);
    }
    
    public static Pattern getSearchPattern(final String text) {
        return Pattern.compile("\\Q" + text.replace("*", "\\E.*\\Q") + "\\E", 2);
    }
    
    static {
        INSTANCE = new Client();
        LOGGER = LogManager.getLogger();
        dir = new File(Minecraft.getMinecraft().mcDataDir, "Arkemys Client");
        LOGO = new ResourceLocation("arkemys/logo.png");
        BACKGROUND = new ResourceLocation("arkemys/bg.png");
        TRANSPARENT = new ResourceLocation("arkemys/transparent.png");
        left = new CPSUtils(CPSUtils.Type.LEFT);
        right = new CPSUtils(CPSUtils.Type.RIGHT);
    }
}
