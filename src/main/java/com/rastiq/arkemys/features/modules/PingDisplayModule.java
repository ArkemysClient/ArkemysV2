package com.rastiq.arkemys.features.modules;

import com.rastiq.arkemys.features.modules.utils.DefaultModuleRenderer;
import com.rastiq.arkemys.features.modules.utils.*;

public class PingDisplayModule extends DefaultModuleRenderer
{
    public PingDisplayModule() {
        super("Ping Display", 18);
    }
    
    @Override
    public Object getValue() {
        return (this.mc.getNetHandler() != null && this.mc.getNetHandler().getPlayerInfo(this.mc.thePlayer.getUniqueID()) != null) ? this.mc.getNetHandler().getPlayerInfo(this.mc.thePlayer.getUniqueID()).getResponseTime() : -1;
    }
    
    @Override
    public String getFormat() {
        return "[%value% ms]";
    }
}
