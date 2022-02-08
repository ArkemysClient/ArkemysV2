package com.rastiq.arkemys.ias.gui;

import com.rastiq.arkemys.Client;
import com.rastiq.arkemys.gui.utils.GLRectUtils;
import com.rastiq.arkemys.gui.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

/**
 * The button with the image on it.
 * @author The_Fireplace
 */
public class GuiButtonWithImage extends GuiButton {
	private final ResourceLocation language = new ResourceLocation("arkemys/icons/modules/autofriend.png");
	public GuiButtonWithImage(int id, int x, int y) {
		super(id, x, y, 20, 20, "ButterDog");
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (this.visible) {
			this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
			GLRectUtils.drawRoundedOutline(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 3.0f, 2.0f, this.enabled ? (this.hovered ? Client.getMainColor(255) : Client.getMainColor(150)) : Client.getMainColor(100));
			GLRectUtils.drawRoundedRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 3.0f, this.enabled ? (this.hovered ? new Color(0, 0, 0, 100).getRGB() : new Color(30, 30, 30, 100).getRGB()) : new Color(70, 70, 70, 50).getRGB());
			mc.getTextureManager().bindTexture(this.language);
			GuiUtils.setGlColor(Client.getMainColor(255));
			GlStateManager.enableBlend();
			int b = 12;
			GuiUtils.drawModalRectWithCustomSizedTexture(this.xPosition + b / 3f, this.yPosition + b / 3f - 0.5f, 0.0f, 0.0f, b, b, b, b);
		}
	}
}
