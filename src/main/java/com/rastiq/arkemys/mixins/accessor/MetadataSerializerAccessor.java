package com.rastiq.arkemys.mixins.accessor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface MetadataSerializerAccessor {

    @Accessor("metadataSerializer_")
    IMetadataSerializer getMetadataSerializer();

}