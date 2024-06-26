package com.rastiq.arkemys.features.modules;

import com.rastiq.arkemys.config.ModuleConfig;
import com.rastiq.arkemys.features.modules.utils.DefaultModuleRenderer;
import com.rastiq.arkemys.gui.utils.FontUtils;
import com.rastiq.arkemys.features.modules.utils.*;
import com.rastiq.arkemys.config.*;
import com.rastiq.arkemys.features.*;
import com.rastiq.arkemys.gui.utils.*;

public class CoordinatesModule extends DefaultModuleRenderer
{
    private int width;
    private int height;
    
    public CoordinatesModule() {
        super("Coordonnées");
    }
    
    @Override
    public int getWidth() {
        return this.width;
    }
    
    @Override
    public int getHeight() {
        return this.height;
    }
    
    @Override
    public void render(final float x, final float y) {
        int i = 0;
        this.width = 0;
        for (final String s : (String[])this.getValue()) {
            this.width = Math.max(this.width, this.mc.fontRendererObj.getStringWidth(s));
            float coordX = x;
            if (ModuleConfig.INSTANCE.getPosition(this).getAnchorPoint().isRightSide()) {
                coordX += this.width - this.mc.fontRendererObj.getStringWidth(s);
            }
            FontUtils.drawString(s, coordX, y + 1.0f + i * (this.mc.fontRendererObj.FONT_HEIGHT + 1));
            ++i;
        }
        this.height = (this.mc.fontRendererObj.FONT_HEIGHT + 1) * 3;
    }
    
    @Override
    public Object getValue() {
        return new String[] { "X: " + this.mc.thePlayer.getPosition().getX(), "Y: " + this.mc.thePlayer.getPosition().getY(), "Z: " + this.mc.thePlayer.getPosition().getZ() };
    }
}
