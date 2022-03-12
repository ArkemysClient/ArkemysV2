package com.rastiq.arkemys.features.modules;

import com.rastiq.arkemys.Client;
import com.rastiq.arkemys.features.modules.utils.DefaultModuleRenderer;
import com.rastiq.arkemys.gui.settings.GuiHUDEditor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.text.DecimalFormat;

public class ReachDisplayModule extends DefaultModuleRenderer
{
    public static ReachDisplayModule INSTANCE;
    private double distance = 0;
    private long hitTime = -1;
    private static final DecimalFormat FORMAT = new DecimalFormat("0.##");

    public ReachDisplayModule() {
        super("ReachDisplay", 14);
        ReachDisplayModule.INSTANCE = this;
    }

    @Override
    public Object getValue() {
        if((System.currentTimeMillis() - hitTime) > 2000) {
            distance = 0;
            return "Aucune attaque";
        }else {
            return FORMAT.format(distance) + " block" + (distance != 1.0 ? "s" : "");
        }
    }

    @Override
    public Object getDummy() {
        return "Aucune attaque";
    }

    @Override
    public String getFormat() {
        return "%value%";
    }

    public void onAttack(Entity entity) {
        if (entity instanceof EntityPlayer) {
            if(mc.objectMouseOver != null && mc.objectMouseOver.hitVec != null) {
                distance = mc.objectMouseOver.hitVec.distanceTo(mc.thePlayer.getPositionEyes(1.0F));
                hitTime = System.currentTimeMillis();
            }

            hitTime = System.currentTimeMillis();
        }
    }
}
