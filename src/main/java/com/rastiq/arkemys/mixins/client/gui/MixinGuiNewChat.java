package com.rastiq.arkemys.mixins.client.gui;

import com.rastiq.arkemys.config.ModuleConfig;
import com.rastiq.arkemys.features.modules.AutoFriendModule;
import com.rastiq.arkemys.features.modules.AutoGGModule;
import com.rastiq.arkemys.features.modules.AutoGLHFModule;
import com.rastiq.arkemys.gui.utils.FontUtils;
import com.rastiq.arkemys.gui.utils.GuiUtils;
import org.spongepowered.asm.mixin.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.rastiq.arkemys.features.modules.*;
import com.rastiq.arkemys.config.*;
import com.rastiq.arkemys.features.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.client.gui.*;
import com.rastiq.arkemys.gui.utils.*;

@Mixin(GuiNewChat.class)
public class MixinGuiNewChat {
    @Inject(method = "printChatMessage", at = @At("HEAD"))
    private void printChatMessage(final IChatComponent chatComponent, final CallbackInfo ci) {
        AutoGLHFModule.INSTANCE.onChat(chatComponent);
        AutoGGModule.INSTANCE.onChat(chatComponent);
        AutoFriendModule.INSTANCE.onChat(chatComponent);
    }

    @Redirect(method = "drawChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V", ordinal = 0))
    private void drawRect(final int left, final int top, final int right, final int bottom, final int color) {
        //final int alpha = ChatModule.INSTANCE.color.getColor() >> 24 & 0xFF;
        GuiUtils.drawRect(left, top, right, bottom, color);
    }

    @Redirect(method = "drawChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    private int drawString(final FontRenderer fontRenderer, final String text, final float x, final float y, final int color) {
        return FontUtils.drawString(text, x, y, color, true);
    }
}
