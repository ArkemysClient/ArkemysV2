package com.rastiq.arkemys.features.modules.keystrokes;

import com.rastiq.arkemys.features.modules.keystrokes.keys.FillerKey;
import com.rastiq.arkemys.features.modules.keystrokes.keys.Key;
import com.rastiq.arkemys.features.modules.keystrokes.keys.KeyMouse;
import com.rastiq.arkemys.features.modules.keystrokes.keys.KeySpacebar;
import com.rastiq.arkemys.features.modules.utils.IModuleRenderer;
import com.rastiq.arkemys.utils.Setting;
import com.rastiq.arkemys.features.modules.utils.*;
import com.rastiq.arkemys.utils.*;
import com.rastiq.arkemys.features.*;
import java.awt.*;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.opengl.*;
import com.rastiq.arkemys.features.modules.keystrokes.keys.*;

public class KeystrokesModule extends IModuleRenderer
{
    public final int gap = 2;
    public KeyLayoutBuilder builder;
    public final Setting boxColor;
    public final Setting boxPressedColor;
    public final Setting boxSize;
    public final Setting showCPS;
    public final Setting textColor;
    public final Setting pressedTextColor;
    public final Setting outline;
    public final Setting outlineColor;
    public float offset;
    
    public KeystrokesModule() {
        super("Keystrokes");
        new Setting(this, "Options de boîte");
        this.boxColor = new Setting(this, "Couleur de boîte").setDefault(new Color(0, 0, 0, 50).getRGB(), 0);
        this.boxPressedColor = new Setting(this, "Couleur de boîte appuyée").setDefault(new Color(255, 255, 255, 100).getRGB(), 0);
        this.boxSize = new Setting(this, "Taille de boîte").setDefault(20.0f).setRange(10.0f, 30.0f, 0.1f).onValueChanged(setting -> this.builder = this.createLayout());
        this.showCPS = new Setting(this, "Afficher les CPS").setDefault(true);
        this.textColor = new Setting(this, "Couleur de texte").setDefault(new Color(255, 255, 255, 255).getRGB(), 0);
        this.pressedTextColor = new Setting(this, "Couleur de texte appuyée").setDefault(new Color(0, 0, 0, 255).getRGB(), 0);
        this.outline = new Setting(this, "Contour").setDefault(false);
        this.outlineColor = new Setting(this, "Couleur de contour").setDefault(new Color(0, 0, 0, 100).getRGB(), 0);
    }
    
    @Override
    public int getWidth() {
        if (this.builder == null) {
            this.builder = this.createLayout();
        }
        return (int)this.builder.getWidth();
    }
    
    @Override
    public int getHeight() {
        return (int)this.builder.getHeight();
    }
    
    @Override
    public void render(final float x, final float y) {
        if (this.builder == null) {
            this.builder = this.createLayout();
        }
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0.0f);
        for (final Rows row : this.builder.rows) {
            GL11.glPushMatrix();
            for (final Key key : row.getKeys()) {
                key.render();
                final double offset = row.getWidth() + this.builder.getGapSize();
                this.offset += key.getHeight();
                GL11.glTranslated(offset, 0.0, 0.0);
            }
            GL11.glPopMatrix();
            GL11.glTranslated(0.0, row.getHeight() + this.builder.getGapSize(), 0.0);
            this.offset = 0.0f;
        }
        GL11.glPopMatrix();
    }
    
    private KeyLayoutBuilder createLayout() {
        final FillerKey filler = new FillerKey(2, this);
        final Key keyW = this.create(this.mc.gameSettings.keyBindForward, this);
        final Key keyA = this.create(this.mc.gameSettings.keyBindLeft, this);
        final Key keyS = this.create(this.mc.gameSettings.keyBindBack, this);
        final Key keyD = this.create(this.mc.gameSettings.keyBindRight, this);
        final Key keyLMB = new KeyMouse(2, this.mc.gameSettings.keyBindAttack, this).setLeft();
        final Key keyRMB = new KeyMouse(2, this.mc.gameSettings.keyBindUseItem, this).setRight();
        final Key keySpace = new KeySpacebar(2, this.mc.gameSettings.keyBindJump, this);
        return new KeyLayoutBuilder().setGapSize(2).setWidth(this.boxSize.getFloat() * 3.0f + 4.0f).addRow(filler, keyW, filler).addRow(keyA, keyS, keyD).addRow(keyLMB, keyRMB).addRow(keySpace).build();
    }
    
    private Key create(final KeyBinding keyBinding, final KeystrokesModule keystrokesModule) {
        return new Key(2, keyBinding, keystrokesModule);
    }
}
