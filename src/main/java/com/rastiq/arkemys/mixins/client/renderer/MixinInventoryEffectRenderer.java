package com.rastiq.arkemys.mixins.client.renderer;

import com.rastiq.arkemys.features.SettingsManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InventoryEffectRenderer.class)
public class MixinInventoryEffectRenderer {

    @Redirect(method = "drawActivePotionEffects", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/resources/I18n;format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;"))
    public String overrideLevel(String translateKey, Object[] parameters) {
        if(SettingsManager.INSTANCE.arabicNumerals.getBoolean() == true && translateKey.startsWith("enchantment.level.")) {
            return Integer.toString(Integer.parseInt(translateKey.substring(18)));
        }

        return I18n.format(translateKey, parameters);
    }

}