package com.rastiq.arkemys.features.modules;

import com.rastiq.arkemys.features.modules.utils.DefaultModuleRenderer;
import com.rastiq.arkemys.features.modules.utils.*;
import net.minecraft.client.*;

public class FPSModule extends DefaultModuleRenderer
{
    public FPSModule() {
        super("FPS");
    }
    
    @Override
    public Object getValue() {
        return Minecraft.getDebugFPS();
    }
}
