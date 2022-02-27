package com.rastiq.arkemys.mixins.client.gui;

import com.rastiq.arkemys.Client;
import com.rastiq.arkemys.discord.DiscordIPC;
import com.rastiq.arkemys.features.SettingsManager;
import net.minecraft.client.gui.GuiSelectWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiSelectWorld.class)
public class MixinGuiSelectWorld {
    @Inject(method = "initGui", at = @At("HEAD"))
    public void initGuiDiscord(CallbackInfo ci) {
        if (Client.INSTANCE.keepAliveTimer != null) {
            Client.INSTANCE.keepAliveTimer = null;
            Client.INSTANCE.hasSent = false;
        }
        Client.INSTANCE.lastMotionBlurValue = 0;
        Client.INSTANCE.processMotionBlur = false;
        DiscordIPC.INSTANCE.update("Dans les menus", "Menu solo");
    }
}
