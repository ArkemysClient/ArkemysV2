package com.rastiq.arkemys;

import com.google.gson.Gson;
import com.rastiq.arkemys.config.ConfigManager;
import com.rastiq.arkemys.config.ModuleConfig;
import com.rastiq.arkemys.discord.DiscordIPC;
import com.rastiq.arkemys.features.ModuleManager;
import com.rastiq.arkemys.features.SettingsManager;
import com.rastiq.arkemys.features.modules.HotbarModule;
import com.rastiq.arkemys.features.modules.MotionBlurModule;
import com.rastiq.arkemys.features.modules.PerspectiveModule;
import com.rastiq.arkemys.features.modules.utils.CPSUtils;
import com.rastiq.arkemys.features.modules.utils.IModuleRenderer;
import com.rastiq.arkemys.gui.settings.*;
import com.rastiq.arkemys.gui.utils.GuiUtils;
import com.rastiq.arkemys.gui.utils.blur.BlurShader;
import com.rastiq.arkemys.mixins.client.renderer.IMixinEntityRenderer;
import com.rastiq.arkemys.motionblur.MotionBlur;
import com.rastiq.arkemys.splash.SplashProgress;
import com.rastiq.arkemys.utils.*;
import com.rastiq.arkemys.utils.Timer;
import com.rastiq.arkemys.websockets.SocketClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.logging.log4j.Logger;

import java.io.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import net.minecraft.client.gui.*;

import java.text.DecimalFormat;
import java.util.regex.*;
import org.apache.logging.log4j.*;
import com.rastiq.arkemys.ias.Config;
import com.rastiq.arkemys.ias.utils.Converter;
import com.rastiq.arkemys.ias.utils.SkinRenderer;

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
    public boolean hasSent = false;
    public Timer keepAliveTimer;
    public static final Gson GSON = new Gson();
    public String rangeText = "Aucune attaque";
    public long lastAttack;
    public boolean processMotionBlur = false;
    public float lastMotionBlurValue = 0;
    public boolean hasLoadedConfig = false;
    public boolean hasBlurredGui = false;
    public KeyBinding guiKeyBind;
    
    public Client() {
        this.mc = Minecraft.getMinecraft();
    }
    
    public void onPostInit() {
        Client.titleRenderer = new CustomFontRenderer("Lato Bold", 16.0f);
        Client.titleRenderer2 = new CustomFontRenderer("Lato Black", 16.0f);
        Client.textRenderer = new CustomFontRenderer("Lato Light", 16.0f);
        SplashProgress.setProgress(6, "Initialisation de la config...");
        ConfigManager.INSTANCE.loadAll();
        File serverResources = new File("server-resource-packs");
        if (!serverResources.exists()) {
            serverResources.mkdir();
        }
        SplashProgress.setProgress(7, "Démarrage des modules...");
        ModuleManager.INSTANCE.init();
        SplashProgress.setProgress(8, "Démarrage de la RPC...");
        DiscordIPC.INSTANCE.start();
        SplashProgress.setProgress(9, "Initialisation des comptes...");
        Config.load(mc);
        Converter.convert(mc);
        mc.addScheduledTask(() -> {
            SkinRenderer.loadAllAsync(mc, false, () -> {});
        });
    }
    
    public void onKeyPress(final int keycode) {
        guiKeyBind = (KeyBinding) SettingsManager.INSTANCE.guiKeyBind.getObject();
        if (Minecraft.getMinecraft().inGameHasFocus && keycode == guiKeyBind.getKeyCode()) {
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen)((SettingsBase.lastWindow == null) ? new GuiModules(null) : SettingsBase.lastWindow));
        }
    }

    public void onTick() {
        if (mc.thePlayer != null && mc.theWorld != null) {
            if (hasLoadedConfig == false) {
                ConfigManager.INSTANCE.loadAll();
                hasLoadedConfig = true;
            }
            if (!hasSent) {
                try {
                    info(SocketClient.client.request("start", mc.getSession().getUsername() + ":true"));
                }catch (Exception e){

                }
                keepAliveTimer = new Timer();
                keepAliveTimer.reset();
                hasSent = true;
            }
            if(keepAliveTimer.hasTimeElapsed(30000, true)) {
                try {
                    info(SocketClient.client.request("keepAlive", mc.getSession().getUsername()));
                }catch (Exception e){

                }
            }
        }
    }

    public void onAttack(Entity entity) {
        if (entity instanceof EntityPlayer) {
            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && this.mc.objectMouseOver.entityHit.getEntityId() == entity.getEntityId()) {
                Vec3 vec3 = this.mc.getRenderViewEntity().getPositionEyes(1.0F);
                double range = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
                this.rangeText = (new DecimalFormat("#.##")).format(range) + " blocks";
            } else {
                this.rangeText = "Pas sur la cible ?";
            }

            this.lastAttack = System.currentTimeMillis();
        }
    }
    
    public void onRenderOverlay() {
        if (System.currentTimeMillis() - this.lastAttack > 2000L) {
            this.rangeText = "Aucune attaque";
        }
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
        if (this.mc.currentScreen instanceof GuiModuleSettings) {
            return;
        }
        if (processMotionBlur == false) {
            if (ModuleConfig.INSTANCE.isEnabled(MotionBlurModule.INSTANCE)) {
                processMotionBlur = true;
                MotionBlur.instance.tick();
                if (MotionBlurModule.INSTANCE.intensity.getInt() != lastMotionBlurValue || hasBlurredGui == true) {
                    lastMotionBlurValue = MotionBlurModule.INSTANCE.intensity.getInt();
                    hasBlurredGui = false;
                    ((IMixinEntityRenderer)this.mc.entityRenderer).callLoadShader(new ResourceLocation("motionblur", "motionblur"));
                    mc.entityRenderer.getShaderGroup().createBindFramebuffers(mc.displayWidth, mc.displayHeight);
                }
            }else{
                processMotionBlur = false;
                if (mc.entityRenderer.getShaderGroup() != null) {
                    mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                }
            }
        }else{
            if (ModuleConfig.INSTANCE.isEnabled(MotionBlurModule.INSTANCE)) {
                processMotionBlur = true;
                MotionBlur.instance.tick();
                if (MotionBlurModule.INSTANCE.intensity.getInt() != lastMotionBlurValue || hasBlurredGui == true) {
                    lastMotionBlurValue = MotionBlurModule.INSTANCE.intensity.getInt();
                    hasBlurredGui = false;
                    ((IMixinEntityRenderer)this.mc.entityRenderer).callLoadShader(new ResourceLocation("motionblur", "motionblur"));
                    mc.entityRenderer.getShaderGroup().createBindFramebuffers(mc.displayWidth, mc.displayHeight);
                }
            }else{
                processMotionBlur = false;
                if (mc.entityRenderer.getShaderGroup() != null) {
                    mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                }
            }
        }
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
