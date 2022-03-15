package com.rastiq.arkemys.mixins.client.renderer.entity;

import com.rastiq.arkemys.Client;
import com.rastiq.arkemys.config.ModuleConfig;
import com.rastiq.arkemys.features.SettingsManager;
import com.rastiq.arkemys.features.modules.PerspectiveModule;
import com.rastiq.arkemys.utils.NametagRenderer;
import com.rastiq.arkemys.websockets.SocketClient;
import net.minecraft.entity.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.*;
import com.rastiq.arkemys.features.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.client.entity.*;
import com.rastiq.arkemys.utils.*;
import com.rastiq.arkemys.features.modules.*;
import net.minecraft.client.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ Render.class })
public abstract class MixinRender<T extends Entity>
{
    @Shadow
    @Final
    protected RenderManager renderManager;
    
    @Shadow
    public abstract FontRenderer getFontRendererFromRenderManager();
    
    /**
     * @author RASTIQ
     */
    @Overwrite
    protected void renderLivingLabel(final T entityIn, final String str, final double x, final double y, final double z, final int maxDistance) {
        final double d0 = entityIn.getDistanceSqToEntity(this.renderManager.livingPlayer);
        if (d0 <= maxDistance * maxDistance) {
            final FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
            final float f = 1.6f;
            final float f2 = 0.016666668f * f;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x + 0.0f, (float)y + entityIn.height + 0.5f, (float)z);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
            GlStateManager.scale(-f2, -f2, f2);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int i = 0;

            if (entityIn instanceof AbstractClientPlayer) {
                if (entityIn.ticksExisted > 20) {
                    if (str == entityIn.getDisplayName().getUnformattedText()) {
                        if (NameIconRenderer.INSTANCE.hasRenderedIcons.containsKey(((AbstractClientPlayer) entityIn).getGameProfile().getName())) {
                            if (NameIconRenderer.INSTANCE.isUsingArkemys.get(((AbstractClientPlayer) entityIn).getGameProfile().getName()) == true) {
                                Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("arkemys/nametags/icon.png"));
                                Gui.drawModalRectWithCustomSizedTexture(-fontrenderer.getStringWidth(entityIn.getDisplayName().getFormattedText()) / 2 - 12, -2, 10, 10, 10, 10, 10, 10);
                            }
                        }else{
                            if (SocketClient.isUser(((AbstractClientPlayer) entityIn).getGameProfile().getName())) {
                                Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("arkemys/nametags/icon.png"));
                                Gui.drawModalRectWithCustomSizedTexture(-fontrenderer.getStringWidth(entityIn.getDisplayName().getFormattedText()) / 2 - 12, -2, 10, 10, 10, 10, 10, 10);
                                NameIconRenderer.INSTANCE.hasRenderedIcons.put(((AbstractClientPlayer) entityIn).getGameProfile().getName(), true);
                                NameIconRenderer.INSTANCE.isUsingArkemys.put(((AbstractClientPlayer) entityIn).getGameProfile().getName(), true);
                            }else{
                                NameIconRenderer.INSTANCE.hasRenderedIcons.put(((AbstractClientPlayer) entityIn).getGameProfile().getName(), true);
                                NameIconRenderer.INSTANCE.isUsingArkemys.put(((AbstractClientPlayer) entityIn).getGameProfile().getName(), false);
                            }
                        }
                    }
                }
            }

            if (NameIconRenderer.INSTANCE.hasFinished() == true) {
                NameIconRenderer.INSTANCE.hasRenderedIcons.clear();
            }

            if (str.equals("deadmau5")) {
                i = -10;
            }
            if (!SettingsManager.INSTANCE.transparentNametags.getBoolean()) {
                final int j = fontrenderer.getStringWidth(str) / 2;
                GlStateManager.disableTexture2D();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                worldrenderer.pos((double)(-j - 1), (double)(-1 + i), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                worldrenderer.pos((double)(-j - 1), (double)(8 + i), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                worldrenderer.pos((double)(j + 1), (double)(8 + i), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                worldrenderer.pos((double)(j + 1), (double)(-1 + i), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                tessellator.draw();
                GlStateManager.enableTexture2D();
            }
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }
    
    @Inject(method = { "doRender" }, at = { @At("TAIL") })
    private void doRender(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo ci) {
        if (SettingsManager.INSTANCE.showName.getBoolean() && entity instanceof EntityPlayerSP) {
            NametagRenderer.render(0.0, 0.0, 0.0);
        }
    }
    
    @ModifyArg(method = { "renderLivingLabel" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V", ordinal = 1), index = 0)
    private float getViewX(final float viewXIn) {
        final float viewX = PerspectiveModule.INSTANCE.isHeld() ? PerspectiveModule.getCameraPitch() : viewXIn;
        return (SettingsManager.INSTANCE.fixNametagRot.getBoolean() && Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) ? (-viewX) : viewX;
    }
    
    @ModifyArg(method = { "renderLivingLabel" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V", ordinal = 0), index = 0)
    private float getViewY(final float viewY) {
        return PerspectiveModule.INSTANCE.isHeld() ? (-PerspectiveModule.getCameraYaw()) : viewY;
    }
}
