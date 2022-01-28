package com.rastiq.arkemys.asm;

import com.rastiq.arkemys.launch.ArkemysTweaker;
import org.spongepowered.asm.lib.*;
import java.util.*;
import com.google.common.collect.*;

public class OrientCameraVisitor extends MethodVisitor implements Opcodes
{
    private final HashMap<String, String> obfList;
    
    public OrientCameraVisitor(final MethodVisitor mv) {
        super(262144, mv);
        (this.obfList = Maps.newHashMap()).put("y", "getCameraYaw");
        this.obfList.put("z", "getCameraPitch");
        this.obfList.put("A", "getCameraYaw");
        this.obfList.put("B", "getCameraPitch");
        this.obfList.put("rotationYaw", "getCameraYaw");
        this.obfList.put("rotationPitch", "getCameraPitch");
        this.obfList.put("prevRotationYaw", "getCameraYaw");
        this.obfList.put("prevRotationPitch", "getCameraPitch");
    }
    
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
        super.visitFieldInsn(opcode, owner, name, desc);
        if (((opcode == 180 && desc.equals("F") && owner.equals("pk")) || owner.equals("net/minecraft/entity/Entity")) && this.obfList.containsKey(name)) {
            ArkemysTweaker.logger.debug("Replaced {} with {}", new Object[] { name, this.obfList.get(name) });
            this.visitInsn(11);
            this.visitInsn(106);
            this.visitMethodInsn(184, "com/rastiq/arkemys/features/modules/PerspectiveModule", (String)this.obfList.get(name), "()F", false);
            this.visitInsn(98);
        }
    }
}
