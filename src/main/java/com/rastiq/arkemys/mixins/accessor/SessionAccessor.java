package com.rastiq.arkemys.mixins.accessor;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface SessionAccessor {

    @Accessor("session")
    Session getSession();

    @Accessor("session")
    void setSession(Session value);

}
