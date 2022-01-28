package com.rastiq.arkemys.features.modules;

import com.rastiq.arkemys.features.Module;
import com.rastiq.arkemys.features.*;
import com.rastiq.arkemys.features.modules.utils.IModuleRenderer;
import com.rastiq.arkemys.gui.utils.FontUtils;
import com.rastiq.arkemys.utils.Setting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ArmorHUDModule extends IModuleRenderer
{
    private final Setting hor;

    public ArmorHUDModule() {
        super("Armor HUD", 14);
        new Setting(this, "General Options");
        this.hor = new Setting(this, "Horizontal").setDefault(false);
    }

    @Override
    public int getWidth() {
        if(hor.getBoolean() == true) {
            return 78;
        } else {
            return 17;
        }
    }

    @Override
    public int getHeight() {
        if(hor.getBoolean() == true) {
            return 20;
        } else {
            return 63;
        }
    }

    @Override
    public void render(float p0, float p1) {
        if(hor.getBoolean() == true) {
            mc.getRenderItem().renderItemAndEffectIntoGUI(mc.thePlayer.getCurrentArmor(3), (int)p0, (int)p1 + 2);
            mc.getRenderItem().renderItemAndEffectIntoGUI(mc.thePlayer.getCurrentArmor(2), (int)p0 + 20, (int)p1 + 2);
            mc.getRenderItem().renderItemAndEffectIntoGUI(mc.thePlayer.getCurrentArmor(1), (int)p0 + 40, (int)p1 + 2);
            mc.getRenderItem().renderItemAndEffectIntoGUI(mc.thePlayer.getCurrentArmor(0), (int)p0 + 60, (int)p1 + 2);
        } else {
            for(int i = 0; i < mc.thePlayer.inventory.armorInventory.length; i++) {
                ItemStack itemStack = mc.thePlayer.inventory.armorInventory[i];
                renderItemStack((int)p0, (int)p1, i, itemStack);
            }
        }
    }

    @Override
    public void renderDummy(float p0, float p1) {
        if(hor.getBoolean()) {
            mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.diamond_helmet), (int)p0, (int)p1 + 2);
            mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.diamond_chestplate), (int)p0 + 20, (int)p1 + 2);
            mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.diamond_leggings), (int)p0 + 40, (int)p1 + 2);
            mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.diamond_boots), (int)p0 + 60, (int)p1 + 2);
        } else {
            renderItemStack((int)p0, (int)p1, 3, new ItemStack(Items.diamond_helmet));
            renderItemStack((int)p0, (int)p1, 2, new ItemStack(Items.diamond_chestplate));
            renderItemStack((int)p0, (int)p1, 1, new ItemStack(Items.diamond_leggings));
            renderItemStack((int)p0, (int)p1, 0, new ItemStack(Items.diamond_boots));
        }

    }

    private void renderItemStack(int x, int y, int i, ItemStack is) {

        if(is == null) {
            return;
        }

        GL11.glPushMatrix();
        int yAdd = (-16 * i) + 48;

		if(is.getItem().isDamageable()) {
			double damage = ((is.getMaxDamage() - is.getItemDamage()) / (double) is.getMaxDamage() * 100);
			FontUtils.drawString(String.format("%.0f", damage), x + 20, y + yAdd + 5, 0x00ff00);
		}

        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI(is, x, y + yAdd);
        GL11.glPopMatrix();

    }
}
