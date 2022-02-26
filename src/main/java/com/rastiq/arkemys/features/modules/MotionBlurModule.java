package com.rastiq.arkemys.features.modules;

import com.rastiq.arkemys.features.Module;
import com.rastiq.arkemys.utils.Setting;

public class MotionBlurModule extends Module
{
    public static MotionBlurModule INSTANCE;
    public final Setting intensity;

    public MotionBlurModule() {
        super("Motion Blur", 14);
        new Setting(this, "Options");
        this.intensity = new Setting(this, "Intensité").setDefault(2).setRange(1, 7, 1);
        MotionBlurModule.INSTANCE = this;
    }
}
