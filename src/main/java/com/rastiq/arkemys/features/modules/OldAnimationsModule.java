package com.rastiq.arkemys.features.modules;

import com.rastiq.arkemys.features.Module;
import com.rastiq.arkemys.gui.utils.GuiUtils;
import com.rastiq.arkemys.utils.Setting;
import com.rastiq.arkemys.features.*;
import com.rastiq.arkemys.utils.*;
import com.rastiq.arkemys.gui.utils.*;

public class OldAnimationsModule extends Module
{
    public static OldAnimationsModule INSTANCE;
    public final Setting blockHit;
    public final Setting eating;
    public final Setting bow;
    public final Setting build;
    public final Setting rod;
    public final Setting swing;
    public final Setting block;
    public final Setting damage;
    public final Setting hitColor;
    
    public OldAnimationsModule() {
        super("Old Animations", 16);
        new Setting(this, "Animations");
        this.blockHit = new Setting(this, "Blockhit").setDefault(false);
        this.eating = new Setting(this, "Manger").setDefault(false);
        this.bow = new Setting(this, "Arc").setDefault(false);
        this.build = new Setting(this, "Build").setDefault(false);
        this.rod = new Setting(this, "Canne à pêche").setDefault(false);
        this.swing = new Setting(this, "Swing").setDefault(false);
        this.block = new Setting(this, "Bloquer").setDefault(false);
        this.damage = new Setting(this, "Dégâts").setDefault(false);
        new Setting(this, "Options de couleur");
        this.hitColor = new Setting(this, "Couleur de dégâts").setDefault(GuiUtils.glToRGB(1.0f, 0.0f, 0.0f, 0.3f), 0);
        OldAnimationsModule.INSTANCE = this;
    }
}
