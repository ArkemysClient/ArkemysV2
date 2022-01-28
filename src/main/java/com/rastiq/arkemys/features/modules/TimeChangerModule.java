package com.rastiq.arkemys.features.modules;

import com.rastiq.arkemys.features.Module;
import com.rastiq.arkemys.utils.Setting;
import com.rastiq.arkemys.features.*;
import com.rastiq.arkemys.utils.*;

public class TimeChangerModule extends Module
{
    public static TimeChangerModule INSTANCE;
    public final Setting time;
    
    public TimeChangerModule() {
        super("Time Changer");
        new Setting(this, "Time Options");
        this.time = new Setting(this, "Time").setDefault(0).setRange("Vanilla", "Day", "Sunset", "Night");
        TimeChangerModule.INSTANCE = this;
    }
}
