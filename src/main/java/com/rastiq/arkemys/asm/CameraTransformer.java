package com.rastiq.arkemys.asm;

import com.rastiq.arkemys.launch.ArkemysTweaker;
import net.minecraft.launchwrapper.*;
import org.spongepowered.asm.lib.*;

public class CameraTransformer implements IClassTransformer
{
    public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
        if (transformedName.equals("net.minecraft.client.renderer.EntityRenderer") || transformedName.equals("bfk")) {
            ArkemysTweaker.logger.info("Performing transformation...");
            final ClassReader reader = new ClassReader(bytes);
            final ClassWriter writer = new ClassWriter(2);
            final ClassVisitor visitor = new ClassVisitor(262144, writer) {
                public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] ex) {
                    final MethodVisitor mv = super.visitMethod(access, name, desc, signature, ex);
                    if ((desc.equals("(F)V") && name.equals("f")) || name.equals("orientCamera")) {
                        ArkemysTweaker.logger.info("Started OrientCamera patcher!");
                        return new OrientCameraVisitor(mv);
                    }
                    if ((desc.equals("(FJ)V") && name.equals("a")) || name.equals("updateCameraAndRender")) {
                        ArkemysTweaker.logger.info("Started Camera patcher!");
                        return new CameraVisitor(mv);
                    }
                    return mv;
                }
            };
            reader.accept(visitor, 0);
            return writer.toByteArray();
        }
        return bytes;
    }
}
