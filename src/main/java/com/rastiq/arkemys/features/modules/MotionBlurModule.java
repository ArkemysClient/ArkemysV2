package com.rastiq.arkemys.features.modules;

import com.rastiq.arkemys.features.Module;
import com.rastiq.arkemys.utils.Setting;

public class MotionBlurModule extends Module
{
    public static MotionBlurModule INSTANCE = new MotionBlurModule();
    public final Setting intensity;

    public MotionBlurModule() {
        super("Motion Blur", 14);
        new Setting(this, "Options");
        this.intensity = new Setting(this, "Intensité").setDefault(2.0f).setRange(1.0f, 7.0f, 1.0f);
    }
}
