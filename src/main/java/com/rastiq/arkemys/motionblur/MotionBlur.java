package com.rastiq.arkemys.motionblur;

import com.rastiq.arkemys.mixins.accessor.MetadataSerializerAccessor;
import com.rastiq.arkemys.mixins.accessor.SimpleReloadableResourceManagerAccessor;
import com.rastiq.arkemys.motionblur.resource.MotionBlurResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.FallbackResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;

public class MotionBlur {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final Map<String, FallbackResourceManager> domainResourceManagers = ((SimpleReloadableResourceManagerAccessor) mc.getResourceManager()).getDomainResourceManagers();
    private Field cachedFastRender;
    private int ticks;

    public static MotionBlur instance = new MotionBlur();

    public MotionBlur() {
        try {
            //noinspection JavaReflectionMemberAccess
            cachedFastRender = GameSettings.class.getDeclaredField("ofFastRender");
        } catch (Exception ignored) {
        }
    }

    public void tick() {
        if (domainResourceManagers != null) {
            if (!domainResourceManagers.containsKey("motionblur")) {
                domainResourceManagers.put("motionblur", new MotionBlurResourceManager(((MetadataSerializerAccessor)mc).getMetadataSerializer()));
            }
        }
    }

    public boolean isFastRenderEnabled() {
        try {
            return cachedFastRender.getBoolean(mc.gameSettings);
        } catch (Exception ignored) {
            return false;
        }
    }
}
