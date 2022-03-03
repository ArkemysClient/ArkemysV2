package com.rastiq.arkemys.mixins.client.gui;

import com.rastiq.arkemys.discord.DiscordIPC;
import com.rastiq.arkemys.features.SettingsManager;
import com.rastiq.arkemys.gui.GuiConfirmDisconnect;
import com.rastiq.arkemys.utils.WatermarkRenderer;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.client.gui.*;
import com.rastiq.arkemys.utils.*;

@Mixin(GuiIngameMenu.class)
public class MixinGuiIngameMenu extends GuiScreen {

    @Inject(method = "initGui", at = @At("TAIL"))
    private void initGui(CallbackInfo ci) {
        this.buttonList.add(new GuiButton(-1, this.width / 2 - 100, this.height / 4 + 56, 200, 20, "Serverlist"));
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    private void actionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id == -1) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if(button.id == 1 && SettingsManager.INSTANCE.confirmDisconnect.getBoolean()) {
            ci.cancel();
            this.mc.displayGuiScreen(new GuiConfirmDisconnect(new GuiIngameMenu()));
        }
    }

    @Inject(method = "drawScreen", at = @At("TAIL"))
    private void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        WatermarkRenderer.render(this.width, this.height);
    }
}
