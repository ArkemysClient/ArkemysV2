package com.rastiq.arkemys.features.modules;

import com.rastiq.arkemys.features.Module;
import com.rastiq.arkemys.utils.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumParticleTypes;

public class ParticleMultiplierModule extends Module
{
    public static ParticleMultiplierModule INSTANCE;
    public final Setting multiplier;
    public final Setting sharpness;
    public final Setting snow;
    public final Setting slime;
    public final Setting flames;


    public ParticleMultiplierModule() {
        super("Particules", 14);
        new Setting(this, "Options");
        this.multiplier = new Setting(this, "Multiplieur").setDefault(4).setRange(1, 10, 1);
        this.sharpness = new Setting(this, "Afficher Sharpness").setDefault(true);
        this.snow = new Setting(this, "Neige").setDefault(false);
        this.slime = new Setting(this, "Slime").setDefault(false);
        this.flames = new Setting(this, "Flammes").setDefault(false);
        ParticleMultiplierModule.INSTANCE = this;
    }

    public void onAttack(Entity entity) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        if(!(entity instanceof EntityLivingBase)) {
            return;
        }

        boolean crit = player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater()
                && !player.isPotionActive(Potion.blindness) && player.ridingEntity == null;

        if(crit) {
            for(int i = 0; i < multiplier.getInt() - 1; i++) {
                Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
            }
        }

        boolean usuallySharpness = EnchantmentHelper.getModifierForCreature(player.getHeldItem(),
                ((EntityLivingBase) entity).getCreatureAttribute()) > 0;

        if(sharpness.getBoolean() || usuallySharpness) {
            for(int i = 0; i < (usuallySharpness ? multiplier.getInt() - 1 : multiplier.getInt()); i++) {
                Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT_MAGIC);
            }
        }

        if(snow.getBoolean()) {
            for(int i = 0; i < multiplier.getInt(); i++) {
                Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.SNOWBALL);
            }
        }

        if(slime.getBoolean()) {
            for(int i = 0; i < multiplier.getInt(); i++) {
                Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.SLIME);
            }
        }

        if(flames.getBoolean()) {
            for(int i = 0; i < multiplier.getInt(); i++) {
                Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.FLAME);
            }
        }
    }
}
