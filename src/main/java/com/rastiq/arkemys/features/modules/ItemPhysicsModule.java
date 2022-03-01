package com.rastiq.arkemys.features.modules;

import com.rastiq.arkemys.features.Module;
import com.rastiq.arkemys.mixins.accessor.EntityAccessor;
import com.rastiq.arkemys.utils.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import java.util.Map;
import java.util.WeakHashMap;

public class ItemPhysicsModule extends Module
{
    public static ItemPhysicsModule INSTANCE;
    public final Setting rotationSpeed;
    public int result = -1;
    private Map<EntityItem, ItemData> dataMap = new WeakHashMap<>();

    public ItemPhysicsModule() {
        super("Physiques", 14);
        new Setting(this, "Options");
        this.rotationSpeed = new Setting(this, "Vitesse de rotation").setDefault(100).setRange(1, 100, 1);
        ItemPhysicsModule.INSTANCE = this;
    }

    public void render(EntityItem entity, double x, double y, double z, float partialTicks, IBakedModel model) {
        result = -1;
        ItemStack itemstack = entity.getEntityItem();
        Item item = itemstack.getItem();

        if(item != null) {
            boolean is3d = model.isGui3d();
            int clumpSize = getClumpSize(itemstack.stackSize);
            float f = 0.25F;
            float f1 =
                    MathHelper.sin((entity.getAge() + partialTicks) / 10.0F
                            + entity.hoverStart) * 0.1F + 0.1F;
            float yScale =
                    model.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
            GlStateManager.translate((float) x, (float) y + 0.1, (float) z);

            float hover =
                    ((entity.getAge() + partialTicks) / 20.0F + entity.hoverStart)
                            * (180F / (float)Math.PI);

            long now = System.nanoTime();

            ItemData data = dataMap.computeIfAbsent(entity, (itemStack) -> new ItemData(System.nanoTime()));

            long since = now - data.lastUpdate;

            GlStateManager.rotate(180, 0, 1, 1);
            GlStateManager.rotate(entity.rotationYaw, 0, 0, 1);

            if(!Minecraft.getMinecraft().isGamePaused()) {
                if(!entity.onGround) {
                    int divisor = 2500000;
                    if(((EntityAccessor) entity).getIsInWeb()) {
                        divisor *= 10;
                    }
                    data.rotation += ((float) since) / ((float) divisor) * (rotationSpeed.getInt() / 100F);
                }
                else if(data.rotation != 0) {
                    data.rotation = 0;
                }
            }

            GlStateManager.rotate(data.rotation, 0, 1, 0);

            data.lastUpdate = now;

            if(!is3d) {
                float rotationXAndY = -0.0F * (clumpSize - 1) * 0.5F;
                float rotationZ = -0.046875F * (clumpSize - 1) * 0.5F;
                GlStateManager.translate(rotationXAndY, rotationXAndY, rotationZ);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            result = clumpSize;
        }
        else {
            result = 0;
        }
    }

    private int getClumpSize(int size) {
        if(size > 48) {
            return 5;
        }
        else if(size > 32) {
            return 4;
        }
        else if(size > 16) {
            return 3;
        }
        else if(size > 1) {
            return 2;
        }
        return 1;
    }

    public static class ItemData {

        public long lastUpdate;
        public float rotation;

        public ItemData(long lastUpdate) {
            this.lastUpdate = lastUpdate;
        }

    }
}
