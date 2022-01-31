package com.rastiq.arkemys.features;

import com.rastiq.arkemys.utils.Setting;
import com.rastiq.arkemys.utils.*;
import java.awt.*;

public class SettingsManager extends Module
{
    public static final SettingsManager INSTANCE;
    public final Setting showName;
    public final Setting fixNametagRot;
    public final Setting borderlessWindow;
    public final Setting generalPerformance;
    public final Setting chunkUpdates;
    public final Setting transparentNametags;
    public final Setting mainColor;
    public final Setting buttonFont;
    
    public SettingsManager() {
        super("Paramètres Généraux", -1, false);
        new Setting(this, "Options Générales");
        this.showName = new Setting(this, "Montrer le nom en F5").setDefault(false);
        this.fixNametagRot = new Setting(this, "Fixer la rotation des nametags").setDefault(true);
        this.borderlessWindow = new Setting(this, "Fenêtre sans bords").setDefault(false);
        new Setting(this, "Options de Performances");
        this.generalPerformance = new Setting(this, "Activer...").setDefault(true);
        this.chunkUpdates = new Setting(this, "Lazy Chunk Loading").setDefault(5).setRange("Éteint (Vanilla)", "Plus bas", "Bas", "Moyen", "Haut", "Plus haut");
        this.transparentNametags = new Setting(this, "Nametags transparents").setDefault(false);
        new Setting(this, "Style");
        this.mainColor = new Setting(this, "Couleur de l'UI principale").setDefault(new Color(255,223,0).getRGB(), 0);
        this.buttonFont = new Setting(this, "Police de caractère personalisée").setDefault(true);
    }
    
    static {
        INSTANCE = new SettingsManager();
    }
}
