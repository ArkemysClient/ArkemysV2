package com.rastiq.arkemys.mixins.item;

import com.rastiq.arkemys.features.SettingsManager;
import net.minecraft.item.ItemPotion;
import net.minecraft.util.StatCollector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemPotion.class)
public class MixinItemPotion {

    @Redirect(method = "addInformation", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/StatCollector;translateToLocal(Ljava/lang/String;)Ljava/lang/String;",
            ordinal = 1))
    public String overrideAmplifier(String key) {
        if(SettingsManager.INSTANCE.arabicNumerals.getBoolean() == true && key.startsWith("potion.potency.")) {
            return Integer.toString(Integer.parseInt(key.substring(15)) + 1);
        }
        return StatCollector.translateToLocal(key);
    }

}
