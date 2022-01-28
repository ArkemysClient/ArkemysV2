package com.rastiq.arkemys.features.modules;

import com.rastiq.arkemys.Client;
import com.rastiq.arkemys.features.modules.utils.DefaultModuleRenderer;
import com.rastiq.arkemys.utils.Setting;
import com.rastiq.arkemys.features.modules.utils.*;
import com.rastiq.arkemys.utils.*;
import com.rastiq.arkemys.features.*;
import com.rastiq.arkemys.*;

public class CPSModule extends DefaultModuleRenderer
{
    private final Setting cpsMode;
    
    public CPSModule() {
        super("CPS Display", 14);
        new Setting(this, "General Options");
        this.cpsMode = new Setting(this, "Mode").setDefault(2).setRange("Left", "Right", "Both", "Higher");
    }
    
    @Override
    public Object getValue() {
        switch (this.cpsMode.getInt()) {
            case 0: {
                return Client.left.getCPS();
            }
            case 1: {
                return Client.right.getCPS();
            }
            case 2: {
                return Client.left.getCPS() + " | " + Client.right.getCPS();
            }
            case 3: {
                return Math.max(Client.left.getCPS(), Client.right.getCPS());
            }
            default: {
                return null;
            }
        }
    }
    
    @Override
    public String getFormat() {
        return "[%value% CPS]";
    }
}
