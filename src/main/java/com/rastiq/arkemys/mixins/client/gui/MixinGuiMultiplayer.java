package com.rastiq.arkemys.mixins.client.gui;

import com.rastiq.arkemys.config.ModuleConfig;
import com.rastiq.arkemys.features.modules.PerspectiveModule;
import com.rastiq.arkemys.utils.HypixelDetector;
import org.spongepowered.asm.mixin.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.client.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ GuiMultiplayer.class })
public class MixinGuiMultiplayer
{
    @Inject(method = { "connectToServer" }, at = { @At("HEAD") })
    private void connectToServer(final ServerData server, final CallbackInfo ci) {
        if (Minecraft.getMinecraft().theWorld != null) {
            Minecraft.getMinecraft().theWorld.sendQuittingDisconnectingPacket();
        }
        if (HypixelDetector.INSTANCE.isHypixel(server)) {
            ModuleConfig.INSTANCE.setEnabled(PerspectiveModule.INSTANCE, false);
        }
    }
}
