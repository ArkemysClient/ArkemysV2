package com.rastiq.arkemys.features.modules;

import com.rastiq.arkemys.Client;
import com.rastiq.arkemys.features.modules.utils.DefaultModuleRenderer;
import com.rastiq.arkemys.gui.settings.GuiHUDEditor;
import net.minecraft.client.Minecraft;

public class ReachDisplayModule extends DefaultModuleRenderer
{
    public ReachDisplayModule() {
        super("ReachDisplay", 14);
    }

    @Override
    public Object getValue() {
        return Client.INSTANCE.rangeText;
    }

    @Override
    public Object getDummy() {
        return "Aucune attaque";
    }

    @Override
    public String getFormat() {
        return "%value%";
    }
}
