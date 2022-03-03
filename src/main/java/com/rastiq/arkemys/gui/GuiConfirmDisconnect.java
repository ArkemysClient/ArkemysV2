package com.rastiq.arkemys.gui;

import com.mojang.util.UUIDTypeAdapter;
import com.rastiq.arkemys.Client;
import com.rastiq.arkemys.ias.Config;
import com.rastiq.arkemys.ias.account.Account;
import com.rastiq.arkemys.ias.account.AuthException;
import com.rastiq.arkemys.ias.gui.AbstractAccountGui;
import com.rastiq.arkemys.ias.gui.SuggestorTextField;
import com.rastiq.arkemys.ias.tools.Tools;
import com.rastiq.arkemys.ias.utils.SkinRenderer;
import com.rastiq.arkemys.mixins.accessor.SessionAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.UUID;

public class GuiConfirmDisconnect extends GuiScreen {
	public final GuiScreen prev;
	
	public GuiConfirmDisconnect(GuiScreen prev) {
		this.prev = prev;
	}

	@Override
	public void initGui() {
		buttonList.add(new GuiButton(5, this.width / 2 + 2, this.height / 2 + 10, 100, 20, EnumChatFormatting.GREEN + "Annuler"));
		buttonList.add(new GuiButton(6, this.width / 2 - 102, this.height / 2 + 10, 100, 20, EnumChatFormatting.RED + "Confirmer"));
	}
	
	@Override
	public void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 5) {
			mc.displayGuiScreen(prev);
		}
		if (button.id == 6) {
			boolean flag = this.mc.isIntegratedServerRunning();
			boolean flag1 = this.mc.isConnectedToRealms();
			button.enabled = false;
			this.mc.theWorld.sendQuittingDisconnectingPacket();
			this.mc.loadWorld((WorldClient)null);

			if (flag)
			{
				this.mc.displayGuiScreen(new GuiMainMenu());
			}
			else if (flag1)
			{
				RealmsBridge realmsbridge = new RealmsBridge();
				realmsbridge.switchToRealms(new GuiMainMenu());
			}
			else
			{
				this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
			}
		}
	}

	@Override
	public void onGuiClosed() {
		Config.save(mc);
	}
	
	@Override
	public void drawScreen(int mx, int my, float delta) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, "Confirmer la déconnection ?", this.width / 2, this.height / 2 - 10, -1);
		super.drawScreen(mx, my, delta);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(prev);
			return;
		}
		super.keyTyped(typedChar, keyCode);
	}
}
