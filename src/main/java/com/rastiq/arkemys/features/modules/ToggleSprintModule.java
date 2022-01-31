package com.rastiq.arkemys.features.modules;

import com.rastiq.arkemys.config.ModuleConfig;
import com.rastiq.arkemys.features.modules.utils.DefaultModuleRenderer;
import com.rastiq.arkemys.features.modules.utils.*;
import com.rastiq.arkemys.config.*;
import com.rastiq.arkemys.features.*;

public class ToggleSprintModule extends DefaultModuleRenderer
{
    private boolean toggled;
    private int wasPressed;
    private int keyHoldTicks;
    private static ToggleSprintModule instance;
    
    public ToggleSprintModule() {
        super("Toggle Sprint", 16);
        this.keyHoldTicks = 7;
        ToggleSprintModule.instance = this;
    }
    
    @Override
    public String getFormat() {
        return "[%value%]";
    }
    
    @Override
    public Object getValue() {
        return this.getDisplayText();
    }
    
    @Override
    public Object getDummy() {
        return "Sprint (Toggle)";
    }
    
    public void updateMovement() {
        if (ModuleConfig.INSTANCE.isEnabled(this)) {
            if (this.mc.gameSettings.keyBindSprint.isKeyDown()) {
                if (this.wasPressed == 0) {
                    if (this.toggled) {
                        this.wasPressed = -1;
                    }
                    else if (this.mc.thePlayer.capabilities.isFlying) {
                        this.wasPressed = this.keyHoldTicks + 1;
                    }
                    else {
                        this.wasPressed = 1;
                    }
                    this.toggled = !this.toggled;
                }
                else if (this.wasPressed > 0) {
                    ++this.wasPressed;
                }
            }
            else {
                if (this.keyHoldTicks > 0 && this.wasPressed > this.keyHoldTicks) {
                    this.toggled = false;
                }
                this.wasPressed = 0;
            }
        }
        else {
            this.toggled = false;
        }
        if (this.toggled) {
            this.mc.thePlayer.setSprinting(true);
        }
    }
    
    private String getDisplayText() {
        String displayText = null;
        final boolean isFlying = this.mc.thePlayer.capabilities.isFlying;
        final boolean isSprintHeld = this.mc.gameSettings.keyBindSprint.isKeyDown();
        final boolean isSprinting = this.mc.thePlayer.isSprinting();
        if (isFlying) {
            displayText = "Flying";
        }
        else if (this.toggled) {
            if (isSprintHeld) {
                displayText = "Sprint (Touche maintenue)";
            }
            else {
                displayText = "Sprint (Toggle)";
            }
        }
        else if (isSprinting) {
            displayText = "Sprint (Vanilla)";
        }
        return displayText;
    }
    
    public static ToggleSprintModule getInstance() {
        return ToggleSprintModule.instance;
    }
}
