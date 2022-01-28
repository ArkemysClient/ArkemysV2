package com.rastiq.arkemys.mixins.client.renderer;

import com.rastiq.arkemys.config.ModuleConfig;
import com.rastiq.arkemys.features.modules.BlockOverlayModule;
import com.rastiq.arkemys.features.modules.OldAnimationsModule;
import com.rastiq.arkemys.mixins.client.multiplayer.MixinPlayerControllerMP;
import net.minecraft.client.renderer.*;
import net.minecraft.client.*;
import org.spongepowered.asm.mixin.*;
import com.rastiq.arkemys.config.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.rastiq.arkemys.features.modules.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;
import com.rastiq.arkemys.mixins.client.multiplayer.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;

@Mixin({EntityRenderer.class})
public abstract class MixinEntityRenderer {
    @Shadow
    private Minecraft mc;

    @ModifyVariable(method = "isDrawBlockOutline", at = @At(value = "STORE", ordinal = 2))
    private boolean isDrawBlockOutline(boolean flag) {
        return flag || (ModuleConfig.INSTANCE.isEnabled(BlockOverlayModule.INSTANCE) && BlockOverlayModule.INSTANCE.persistent.getBoolean());
    }

    @Inject(method = "renderHand", at = @At("TAIL"))
    private void renderHand(float partialTicks, int xOffset, CallbackInfo ci) {
        if (OldAnimationsModule.INSTANCE.build.getBoolean()) {
            if (this.mc.thePlayer.getItemInUseCount() != 0 && this.mc.gameSettings.keyBindAttack.isKeyDown() && this.mc.gameSettings.keyBindUseItem.isKeyDown() && this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit.equals(MovingObjectPosition.MovingObjectType.BLOCK)) {
                this.swingItem(this.mc.thePlayer);
            }
            if (this.mc.gameSettings.keyBindAttack.isKeyDown() && this.mc.gameSettings.keyBindUseItem.isKeyDown() && this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit.equals(MovingObjectPosition.MovingObjectType.BLOCK)) {
                ((MixinPlayerControllerMP) this.mc.playerController).setIsHittingBlock(false);
            }
        }
    }

    private void swingItem(EntityLivingBase entity) {
        ItemStack stack = entity.getHeldItem();
        if (stack != null && stack.getItem() != null && (!entity.isSwingInProgress || entity.swingProgressInt >= this.getArmSwingAnimationEnd(entity) / 2 || entity.swingProgressInt < 0)) {
            entity.swingProgressInt = -1;
            entity.isSwingInProgress = true;
        }
    }

    private int getArmSwingAnimationEnd(EntityLivingBase e) {
        return e.isPotionActive(Potion.digSpeed) ? (6 - (1 + e.getActivePotionEffect(Potion.digSpeed).getAmplifier())) : (e.isPotionActive(Potion.digSlowdown) ? (6 + (1 + e.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2) : 6);
    }
}
