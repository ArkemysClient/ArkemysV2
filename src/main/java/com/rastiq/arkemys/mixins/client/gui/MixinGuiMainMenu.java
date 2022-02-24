package com.rastiq.arkemys.mixins.client.gui;

import com.rastiq.arkemys.Client;
import com.rastiq.arkemys.discord.DiscordIPC;
import com.rastiq.arkemys.features.SettingsManager;
import com.rastiq.arkemys.gui.GuiButtonIcon;
import com.rastiq.arkemys.gui.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;

import java.util.*;

import net.minecraft.client.gui.GuiScreen;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.client.renderer.*;
import org.spongepowered.asm.mixin.*;
import com.rastiq.arkemys.ias.Config;
import com.rastiq.arkemys.ias.utils.Expression;
import com.rastiq.arkemys.ias.gui.GuiAccountSelector;
import com.rastiq.arkemys.ias.gui.GuiButtonWithImage;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu extends GuiScreen implements GuiYesNoCallback {
    private static int textX, textY;
    @ModifyArg(method = "initGui", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0))
    private Object moveOptionsButton(Object buttonIn) {
        GuiButton guiButton = (GuiButton) buttonIn;
        guiButton.xPosition += 23;
        guiButton.setWidth(154);
        return guiButton;
    }

    @Redirect(method = "initGui", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1))
    private boolean moveQuitButton(List<GuiButton> list, Object e) {
        final GuiButton quit = (GuiButton) e;
        return list.add(new GuiButtonIcon(quit.id, quit.xPosition + 78, quit.yPosition, 20, 20, "close.png"));
    }

    @ModifyArg(method = "initGui", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 2))
    private Object moveLanguageButton(Object buttonIn) {
        final GuiButton guiButton = (GuiButton) buttonIn;
        guiButton.xPosition += 24;
        return guiButton;
    }

    @Inject(method = "initGui", at = @At(value = "HEAD"))
    public void initGuiDiscord(CallbackInfo ci) {
        DiscordIPC.INSTANCE.update("Dans les menus", "Menu principal");
    }

    /**
     * @author RASTIQ.
     */
    @Overwrite
    protected void actionPerformed(GuiButton button) {
        if (button.id == 69420) {
            mc.displayGuiScreen(new GuiAccountSelector(new GuiMainMenu()));
        }
        if (button.id == 0)
        {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        if (button.id == 5)
        {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }

        if (button.id == 1)
        {
            if (Minecraft.getMinecraft().getSession().getUsername() != "ArkemysClient") {
                this.mc.displayGuiScreen(new GuiSelectWorld(this));
            }else{
                this.mc.displayGuiScreen(new GuiAccountSelector(new GuiMainMenu()));
            }

        }

        if (button.id == 2)
        {
            if (Minecraft.getMinecraft().getSession().getUsername() != "ArkemysClient") {
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
            }else {
                this.mc.displayGuiScreen(new GuiAccountSelector(new GuiMainMenu()));
            }
        }

        if (button.id == 4)
        {
            this.mc.shutdown();
        }
    }

    /**
     * @author RASTIQ.
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(Client.BACKGROUND);
        GuiUtils.drawModalRectWithCustomSizedTexture(0, 0, (float) this.width, (float) this.height, this.width, this.height, (float) this.width, (float) this.height);
        this.mc.getTextureManager().bindTexture(Client.LOGO);
        GuiUtils.drawModalRectWithCustomSizedTexture(this.width / 2 - 118 + 2, 50, 0.0f, 0.0f, 113, 36, 242.0f, 36.0f);
        GuiUtils.setGlColor(Client.getMainColor(255));
        GuiUtils.drawModalRectWithCustomSizedTexture(this.width / 2, 50, 112.0f, 0.0f, 124, 36, 237.0f, 36.0f);
        GuiUtils.setGlColor(Client.getMainColor(255));
        GlStateManager.popMatrix();
        this.drawString(this.fontRendererObj, "Arkemys Client 1.8.9", 2, this.height - 10, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
        try {
            if (StringUtils.isNotBlank(Config.textX) && StringUtils.isNotBlank(Config.textY)) {
                textX = (int) new Expression(Config.textX.replace("%width%", Integer.toString(mc.currentScreen.width)).replace("%height%", Integer.toString(mc.currentScreen.height))).parse(0);
                textY = (int) new Expression(Config.textY.replace("%width%", Integer.toString(mc.currentScreen.width)).replace("%height%", Integer.toString(mc.currentScreen.height))).parse(0);
            } else {
                textX = mc.currentScreen.width / 2;
                textY = mc.currentScreen.height / 4 + 48 + 72 + 12 + 22;
            }
        } catch (Throwable t) {
            t.printStackTrace();
            textX = mc.currentScreen.width / 2;
            textY = mc.currentScreen.height / 4 + 48 + 72 + 12 + 22;
        }
        if (Config.showOnTitleScreen) {
            int btnX = mc.currentScreen.width / 2 + 104;
            int btnY = mc.currentScreen.height / 4 + 48 + 48;
            try {
                if (StringUtils.isNotBlank(Config.btnX) && StringUtils.isNotBlank(Config.btnY)) {
                    btnX = (int) new Expression(Config.btnX.replace("%width%", Integer.toString(mc.currentScreen.width)).replace("%height%", Integer.toString(mc.currentScreen.height))).parse(0);
                    btnY = (int) new Expression(Config.btnY.replace("%width%", Integer.toString(mc.currentScreen.width)).replace("%height%", Integer.toString(mc.currentScreen.height))).parse(0);
                }
            } catch (Throwable t) {
                t.printStackTrace();
                btnX = mc.currentScreen.width / 2 + 104;
                btnY = mc.currentScreen.height / 4 + 48 + 48;
            }
            buttonList.add(new GuiButtonWithImage(69420, btnX, btnY));
        }
        mc.currentScreen.drawCenteredString(mc.fontRendererObj, mc.getSession().getUsername() == "ArkemysClient" ? "Pas connecté" : "Connecté en tant que " + mc.getSession().getUsername(), textX, textY, 0xFFCC8888);
        if (mc.getSession().getToken().equals("0") || mc.getSession().getToken().equals("-")) {
            List<String> list = mc.fontRendererObj.listFormattedStringToWidth(mc.getSession().getUsername() == "ArkemysClient" ? "" : "Mode Cracké", mc.currentScreen.width);
            for (int i = 0; i < list.size(); i++) {
                mc.currentScreen.drawCenteredString(mc.fontRendererObj, list.get(i), mc.currentScreen.width / 2, i * 9 + 1, 16737380);
            }
        }
    }
}
