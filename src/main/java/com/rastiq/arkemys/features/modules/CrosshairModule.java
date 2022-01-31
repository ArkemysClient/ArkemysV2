package com.rastiq.arkemys.features.modules;

import com.rastiq.arkemys.features.Module;
import com.rastiq.arkemys.gui.utils.GLUtils;
import com.rastiq.arkemys.gui.utils.GuiUtils;
import com.rastiq.arkemys.utils.Setting;
import com.rastiq.arkemys.features.*;
import com.rastiq.arkemys.utils.*;
import java.awt.*;
import net.minecraft.client.gui.*;
import com.rastiq.arkemys.gui.utils.*;
import net.minecraft.client.renderer.*;

public class CrosshairModule extends Module
{
    public static CrosshairModule INSTANCE;
    private final Setting crosshair;
    private final Setting color;
    private final Setting size;
    private final Setting gap;
    private final Setting thickness;
    private final Setting dot;
    private final Setting dotColor;
    public final Setting showInThird;
    
    public CrosshairModule() {
        super("Crosshair", 14);
        new Setting(this, "Options de style");
        this.crosshair = new Setting(this, "Mode").setDefault(0).setRange("Vanilla", "Croix", "Cercle", "Flèche");
        this.color = new Setting(this, "Couleur").setDefault(new Color(255, 255, 255, 255).getRGB(), 0);
        this.size = new Setting(this, "Taille").setDefault(16).setRange(2, 24, 1);
        this.gap = new Setting(this, "Bord").setDefault(4).setRange(0, 32, 1);
        this.thickness = new Setting(this, "Épaisseur").setDefault(2.0f).setRange(0.5f, 5.0f, 0.5f);
        new Setting(this, "Options de point");
        this.dot = new Setting(this, "Point").setDefault(false);
        this.dotColor = new Setting(this, "Couleur de point").setDefault(new Color(255, 255, 255, 255).getRGB(), 0);
        new Setting(this, "Autre");
        this.showInThird = new Setting(this, "Montrer à la 3ème personne").setDefault(false);
        CrosshairModule.INSTANCE = this;
    }
    
    public void render(final GuiIngame gui, final int x, final int y, final int i, final int j) {
        final int color = this.color.getColor();
        final int size = this.size.getInt();
        final int gap = this.gap.getInt();
        float thickness = this.thickness.getFloat();
        if (this.dot.getBoolean()) {
            GLUtils.drawDot(x - 0.1f, y, 3.0f, this.dotColor.getColor());
        }
        switch (this.crosshair.getInt()) {
            case 0: {
                GuiUtils.setGlColor(color);
                gui.drawTexturedModalRect(i, j, 0, 0, 16, 16);
                break;
            }
            case 1: {
                thickness /= 2.0f;
                GLUtils.drawFilledRectangle(x - thickness, y - gap - size, x + thickness, y - gap, color, true);
                GLUtils.drawFilledRectangle(x - thickness, y + gap, x + thickness, y + gap + size, color, true);
                GLUtils.drawFilledRectangle(x - gap - size, y - thickness, x - gap, y + thickness, color, true);
                GLUtils.drawFilledRectangle(x + gap, y - thickness, x + gap + size, y + thickness, color, true);
                break;
            }
            case 2: {
                GLUtils.drawTorus(x, y, gap, gap + thickness, color, true);
                break;
            }
            case 3: {
                GLUtils.drawLines(new float[] { x - size, y + size, x, y, x, y, x + size, y + size }, thickness, color, true);
                break;
            }
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
