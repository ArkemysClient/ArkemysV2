package com.rastiq.arkemys.mixins.client.gui;

import com.rastiq.arkemys.Client;
import com.rastiq.arkemys.config.ModuleConfig;
import com.rastiq.arkemys.discord.DiscordIPC;
import com.rastiq.arkemys.features.SettingsManager;
import com.rastiq.arkemys.features.modules.PerspectiveModule;
import com.rastiq.arkemys.utils.HypixelDetector;
import com.rastiq.arkemys.utils.NameIconRenderer;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.client.*;
import org.spongepowered.asm.mixin.injection.*;

import java.util.List;

@Mixin({ GuiMultiplayer.class })
public class MixinGuiMultiplayer
{

    @Inject(method = "initGui", at = @At(value = "HEAD"))
    public void initGuiDiscord(CallbackInfo ci) {
        if (Client.INSTANCE.keepAliveTimer != null) {
            Client.INSTANCE.keepAliveTimer = null;
            Client.INSTANCE.hasSent = false;
        }
        Client.INSTANCE.lastMotionBlurValue = 0;
        Client.INSTANCE.processMotionBlur = false;
        DiscordIPC.INSTANCE.update("Dans les menus", "Menu multijoueur");
    }

    @Inject(method = { "connectToServer" }, at = { @At("HEAD") })
    private void connectToServer(final ServerData server, final CallbackInfo ci) {
        if (Minecraft.getMinecraft().theWorld != null) {
            Minecraft.getMinecraft().theWorld.sendQuittingDisconnectingPacket();
        }
        if (HypixelDetector.INSTANCE.isHypixel(server)) {
            ModuleConfig.INSTANCE.setEnabled(PerspectiveModule.INSTANCE, false);
        }
        if (HypixelDetector.INSTANCE.isHypixel(server)) {
            DiscordIPC.INSTANCE.update("Dans un serveur", "Hypixel Network");
        } else {
            DiscordIPC.INSTANCE.update("Dans un serveur", server.serverIP);
        }
        NameIconRenderer.INSTANCE.reset();
    }

    @Inject(method = { "drawScreen" }, at = { @At("TAIL")})
    private void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.getSession().getToken().equals("0") || mc.getSession().getToken().equals("-")) {
            List<String> list = mc.fontRendererObj.listFormattedStringToWidth("Vous êtes en mode crack.", mc.currentScreen.width);
            for (int i = 0; i < list.size(); i++) {
                mc.currentScreen.drawCenteredString(mc.fontRendererObj, list.get(i), mc.currentScreen.width / 2, i * 9 + 1, 16737380);
            }
        }
    }
}
