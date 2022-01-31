package com.rastiq.arkemys.features.modules;

import com.rastiq.arkemys.features.modules.utils.DefaultModuleRenderer;
import com.rastiq.arkemys.utils.Setting;
import com.rastiq.arkemys.features.modules.utils.*;
import com.rastiq.arkemys.utils.*;
import com.rastiq.arkemys.features.*;
import net.minecraft.util.*;

public class SpeedometerModule extends DefaultModuleRenderer
{
    private final Setting decimalPoints;
    private final Setting unit;
    
    public SpeedometerModule() {
        super("Speedometer", 18);
        new Setting(this, "Options de nombre");
        this.decimalPoints = new Setting(this, "Points décimaux").setDefault(2).setRange(0, 8, 1);
        this.unit = new Setting(this, "Unité").setDefault(0).setRange("blocks/s", "m/s");
    }
    
    @Override
    public Object getValue() {
        final double distX = this.mc.thePlayer.posX - this.mc.thePlayer.lastTickPosX;
        final double distZ = this.mc.thePlayer.posZ - this.mc.thePlayer.lastTickPosZ;
        return String.format("%." + this.decimalPoints.getInt() + "f", MathHelper.sqrt_double(distX * distX + distZ * distZ) / 0.05f);
    }
    
    @Override
    public String getFormat() {
        return "[%value% " + this.unit.getValue().get(this.unit.getInt() + 1) + "]";
    }
}
