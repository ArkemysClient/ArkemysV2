package com.rastiq.arkemys.mixins.client.network;

import com.rastiq.arkemys.Client;
import com.rastiq.arkemys.config.ModuleConfig;
import com.rastiq.arkemys.features.modules.TimeChangerModule;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.play.*;
import net.minecraft.client.network.*;
import net.minecraft.client.*;
import org.spongepowered.asm.mixin.*;
import net.minecraft.network.play.server.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.rastiq.arkemys.config.*;
import com.rastiq.arkemys.features.modules.*;
import net.minecraft.network.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ NetHandlerPlayClient.class })
public abstract class MixinNetHandlerPlayClient implements INetHandlerPlayClient
{
    @Shadow
    private Minecraft gameController;

    @Shadow
    private WorldClient clientWorldController;

    @Inject(method = "handleEntityStatus", at = @At("RETURN"))
    public void handleEntityStatus(S19PacketEntityStatus packetIn, CallbackInfo callback) {
        if(packetIn.getOpCode() == 2) {
            Client.INSTANCE.onDamage(packetIn.getEntity(clientWorldController));
        }
    }
    
    @Inject(method = { "handleTimeUpdate" }, at = { @At("HEAD") }, cancellable = true)
    private void handleTimeUpdate(final S03PacketTimeUpdate packetIn, final CallbackInfo ci) {
        S03PacketTimeUpdate packet = packetIn;
        if (ModuleConfig.INSTANCE.isEnabled(TimeChangerModule.INSTANCE)) {
            switch (TimeChangerModule.INSTANCE.time.getInt()) {
                case 1: {
                    packet = new S03PacketTimeUpdate(packet.getWorldTime(), -6000L, true);
                    break;
                }
                case 2: {
                    packet = new S03PacketTimeUpdate(packet.getWorldTime(), -22880L, true);
                    break;
                }
                case 3: {
                    packet = new S03PacketTimeUpdate(packet.getWorldTime(), -18000L, true);
                    break;
                }
            }
            PacketThreadUtil.checkThreadAndEnqueue(packet, this, this.gameController);
            this.gameController.theWorld.setTotalWorldTime(packetIn.getTotalWorldTime());
            this.gameController.theWorld.setWorldTime(packetIn.getWorldTime());
            ci.cancel();
        }
    }
}
