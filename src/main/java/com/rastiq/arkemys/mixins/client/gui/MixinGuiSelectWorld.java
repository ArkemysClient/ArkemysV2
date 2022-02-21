package com.rastiq.arkemys.mixins.client.gui;

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
        if (SettingsManager.INSTANCE.discordRPC.getBoolean() == true) {DiscordIPC.INSTANCE.update("Dans les menus", "Menu solo");}
    }
}
