package com.rastiq.arkemys.mixins.enchantment;

import com.rastiq.arkemys.features.SettingsManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.StatCollector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class MixinEnchantment {

    @Inject(method = "getTranslatedName", at = @At("HEAD"), cancellable = true)
    public void overrideName(int level, CallbackInfoReturnable<String> callback) {
        if(SettingsManager.INSTANCE.arabicNumerals.getBoolean() == true) {
            callback.setReturnValue(StatCollector.translateToLocal(getName()) + " " + level);
        }
    }

    @Shadow
    public abstract String getName();

}