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
    public String rangeText = "Aucune attaque";
    public long lastAttack;

    public ReachDisplayModule() {
        super("ReachDisplay", 14);
        ReachDisplayModule.INSTANCE = this;
    }

    @Override
    public Object getValue() {
        return this.rangeText;
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
            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && this.mc.objectMouseOver.entityHit.getEntityId() == entity.getEntityId()) {
                Vec3 vec3 = this.mc.getRenderViewEntity().getPositionEyes(1.0F);
                double range = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
                this.rangeText = (new DecimalFormat("#.##")).format(range) + " blocks";
            } else {
                this.rangeText = "Pas sur la cible ?";
            }

            this.lastAttack = System.currentTimeMillis();
        }
    }
}
