package com.rastiq.arkemys.mixins.util;

import com.rastiq.arkemys.features.modules.ToggleSprintModule;
import org.spongepowered.asm.mixin.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.rastiq.arkemys.features.modules.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ MovementInputFromOptions.class })
public class MixinMovementInput extends MovementInput
{
    @Inject(method = { "updatePlayerMoveState" }, at = { @At("TAIL") })
    private void updatePlayerMoveState(final CallbackInfo ci) {
        ToggleSprintModule.getInstance().updateMovement();
    }
}
