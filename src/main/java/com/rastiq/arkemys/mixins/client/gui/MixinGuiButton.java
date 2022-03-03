package com.rastiq.arkemys.mixins.client.gui;

import com.rastiq.arkemys.Client;
import com.rastiq.arkemys.features.SettingsManager;
import com.rastiq.arkemys.gui.utils.FontUtils;
import com.rastiq.arkemys.gui.utils.GLRectUtils;
import net.minecraft.client.gui.*;
import org.spongepowered.asm.mixin.*;
import net.minecraft.client.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.rastiq.arkemys.*;
import java.awt.*;
import com.rastiq.arkemys.features.*;
import com.rastiq.arkemys.gui.utils.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ GuiButton.class })
public abstract class MixinGuiButton
{
    @Shadow
    public int xPosition;
    @Shadow
    public int yPosition;
    @Shadow
    protected int width;
    @Shadow
    protected int height;
    @Shadow
    public boolean visible;
    @Shadow
    public boolean enabled;
    @Shadow
    protected boolean hovered;
    @Shadow
    public String displayString;
    
    @Shadow
    protected abstract int getHoverState(boolean p0);
    
    @Shadow
    protected abstract void mouseDragged(Minecraft mc, int mouseX, int mouseY);
    
    @Inject(method = { "drawButton" }, at = { @At("HEAD") }, cancellable = true)
    public void drawButton(Minecraft mc, int mouseX, int mouseY, CallbackInfo ci) {
        if (this.visible) {
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            GLRectUtils.drawRoundedOutline(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 3.0f, 2.0f, this.enabled ? (this.hovered ? Client.getMainColor(255) : Client.getMainColor(150)) : Client.getMainColor(100));
            GLRectUtils.drawRoundedRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 3.0f, this.enabled ? (this.hovered ? new Color(0, 0, 0, 100).getRGB() : new Color(30, 30, 30, 100).getRGB()) : new Color(70, 70, 70, 50).getRGB());
            if (SettingsManager.INSTANCE.buttonFont.getBoolean()) {
                Client.textRenderer.drawCenteredString(this.displayString.replace("§c", "").replace("§e", "").replace("§r", "").replace("§f", "").replace("§a", "").toUpperCase(), this.xPosition + this.width / 2, this.yPosition + (this.height - 10) / 2, this.enabled ? (this.hovered ? 16777120 : this.displayString.contains("§c") ? 16733525 : this.displayString.contains("§e") ? 16777045 : this.displayString.contains("§a") ? 5635925 : 14737632) : 10526880);
            }
            else {
                FontUtils.drawCenteredString(this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, this.enabled ? (this.hovered ? 16777120 : 14737632) : 10526880);
            }
            this.mouseDragged(mc, mouseX, mouseY);
        }
        ci.cancel();
    }
}
