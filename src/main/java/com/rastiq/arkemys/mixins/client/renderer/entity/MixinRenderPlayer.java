package com.rastiq.arkemys.mixins.client.renderer.entity;

import com.rastiq.arkemys.cosmetics.LayerCape;
import net.minecraft.client.entity.*;
import org.spongepowered.asm.mixin.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.model.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.rastiq.arkemys.cosmetics.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ RenderPlayer.class })
public abstract class MixinRenderPlayer extends RendererLivingEntity<AbstractClientPlayer>
{
    public MixinRenderPlayer(final RenderManager renderManagerIn, final ModelBase modelBaseIn, final float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }
    
    @Inject(method = { "<init>(Lnet/minecraft/client/renderer/entity/RenderManager;Z)V" }, at = { @At("RETURN") })
    private void constructor(final RenderManager renderManager, final boolean useSmallArms, final CallbackInfo ci) {
        this.addLayer(new LayerCape());
    }
}
