package com.rastiq.arkemys.gui.settings;

import com.rastiq.arkemys.Client;
import com.rastiq.arkemys.config.ModuleConfig;
import com.rastiq.arkemys.features.Module;
import com.rastiq.arkemys.features.ModuleManager;
import com.rastiq.arkemys.gui.GuiScrolling;
import com.rastiq.arkemys.gui.elements.ElementModule;
import com.rastiq.arkemys.gui.elements.ElementModuleSettings;
import com.rastiq.arkemys.gui.elements.ElementTextfield;
import com.rastiq.arkemys.gui.utils.GLRectUtils;
import com.rastiq.arkemys.gui.utils.LayoutBuilder;
import com.rastiq.arkemys.features.*;
import com.google.common.collect.*;
import com.rastiq.arkemys.*;
import java.util.*;
import com.rastiq.arkemys.gui.*;
import net.minecraft.server.MinecraftServer;
import org.lwjgl.input.*;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.*;
import com.rastiq.arkemys.config.*;
import java.io.*;
import com.rastiq.arkemys.gui.elements.*;
import com.rastiq.arkemys.utils.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import java.awt.*;
import com.rastiq.arkemys.gui.utils.*;

public class GuiModules extends SettingsBase
{
    private int row;
    private int column;
    private int columns;
    private int gap;
    public static int scrollState;
    private ElementTextfield search;
    
    public GuiModules(final net.minecraft.client.gui.GuiScreen parentScreen) {
        super(parentScreen);
        this.columns = 3;
    }
    
    @Override
    public void initGui() {
        this.initGui(false);
    }
    
    public void initGui(final boolean keepSearch) {
        this.row = 1;
        this.column = 1;
        this.gap = this.getLayoutWidth(this.getMainWidth() / 6) / 8;
        this.elements.clear();
        this.components.clear();
        if (keepSearch) {
            this.elements.add(this.search);
        }
        else {
            this.elements.add(this.search = new ElementTextfield(this.width / 2 + this.getWidth() / 2 - 104, this.height / 2 - this.getHeight() / 2 + 4, 100, 10, "Rechercher...", this));
        }
        final LinkedHashSet<Module> modules = new LinkedHashSet<Module>(ModuleManager.INSTANCE.modules);
        final Set<Module> toRemove = Sets.newLinkedHashSet();
        //final Set<Module> set;
        modules.forEach(module -> {
            if (Client.getSearchPattern(this.search.getText()).matcher(module.displayName).find() || module.settings.stream().anyMatch(setting -> Client.getSearchPattern(this.search.getText()).matcher(setting.getDescription()).find())) {
                this.addElement(module);
            }
            else {
                toRemove.add(module);
                //set.add(module);
            }
            return;
        });
        if (keepSearch) {
            modules.removeAll(toRemove);
        }
        super.initGui();
        this.registerScroll(new Scroll(modules, this.width, this.height, this.height / 2 - this.getHeight() / 2 + 20, this.height / 2 + this.getHeight() / 2 - 50, 40, this.width / 2 + this.getWidth() / 2 - 4, this.columns));
        this.setScrollState(GuiModules.scrollState);
        Keyboard.enableRepeatEvents(true);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground();
        GuiModules.scrollState = this.getScrollState();
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final int x = (this.width / 2 - this.getWidth() / 2) * sr.getScaleFactor();
        final int y = (this.height / 2 - this.getHeight() / 2 + 1) * sr.getScaleFactor();
        final int xWidth = (this.width / 2 + this.getWidth() / 2) * sr.getScaleFactor() - x;
        final int yHeight = (this.height / 2 + this.getHeight() / 2 - 20) * sr.getScaleFactor() - y;
        GL11.glEnable(3089);
        this.scissorFunc(sr, x, y, xWidth, yHeight);
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glDisable(3089);
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        ModuleConfig.INSTANCE.saveConfig();
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (this.search.isFocused()) {
            this.initGui(true);
        }
    }
    
    private void addElement(final Module module) {
        final int minWidth = 90;
        int margin = (this.getMainWidth() / 6 < 70) ? 10 : (this.getMainWidth() / 6);
        margin = 15;
        final LayoutBuilder lb = new LayoutBuilder(this.width / 2 - this.getWidth() / 2 + 18 + margin, this.getLayoutWidth(margin), this.gap, this.columns);
        final int boxWidth = Math.max(minWidth, lb.getSplittedWidth());
        final int boxHeight = 23;
        final int x = lb.getCoordinateForIndex(this.column - 1);
        final int y = (int)this.getRowHeight(this.row, boxHeight + 10);
        this.elements.add(new ElementModule(x + 1, y + 1, boxWidth, boxHeight, module, button -> {
            if (this.elements.stream().filter(element -> element != button).noneMatch(element -> element.hovered)) {
                if (!HypixelDetector.isSinglePlayer()) {
                    if (module.displayName == "Perspective") {
                        if (!HypixelDetector.INSTANCE.isHypixel(Minecraft.getMinecraft().getCurrentServerData())) {
                            ModuleConfig.INSTANCE.setEnabled(module, !ModuleConfig.INSTANCE.isEnabled(module));
                        }
                    }else{
                        ModuleConfig.INSTANCE.setEnabled(module, !ModuleConfig.INSTANCE.isEnabled(module));
                    }
                }else{
                    ModuleConfig.INSTANCE.setEnabled(module, !ModuleConfig.INSTANCE.isEnabled(module));
                }
            }
            return;
        }));
        if (module.settings.size() > 0) {
            this.elements.add(new ElementModuleSettings(x + boxWidth - 8, y + boxHeight - 8, 6, 6, "icons/settings.png", 14, true, module, element -> this.nextGui(new GuiModuleSettings(module, this))));
        }
        ++this.column;
        if (this.column > this.columns) {
            this.column = 1;
            ++this.row;
        }
    }
    
    private double getRowHeight(double row, final int buttonHeight) {
        --row;
        return this.height / 2 - this.getHeight() / 2 + 20 + row * (buttonHeight + 10);
    }
    
    private int getLayoutWidth(final int margin) {
        return this.width / 2 + this.getWidth() / 2 - margin - (this.width / 2 - this.getWidth() / 2 + 18 + margin);
    }
    
    private int getLayoutHeight(final int margin) {
        return this.height / 2 + this.getHeight() / 2 + margin - (this.height / 2 - this.getHeight() / 2 - margin);
    }
    
    private int getMainWidth() {
        return this.width / 2 + this.getWidth() / 2 - (this.width / 2 - this.getWidth() / 2 + 16);
    }
    
    static {
        GuiModules.scrollState = 0;
    }
    
    public static class Scroll extends GuiScrolling
    {
        private final Set<?> list;
        private final int scrollbarX;
        private final int columns;
        private int expandedWidth;
        private int scrollTop;
        private int scrollBottom;
        
        public Scroll(final Set<?> list, final int width, final int height, final int topIn, final int bottomIn, final int slotHeightIn, final int scrollbarX, final int columns) {
            super(Minecraft.getMinecraft(), width, height, topIn, bottomIn, slotHeightIn);
            this.list = list;
            this.scrollbarX = scrollbarX;
            this.columns = columns;
            if (Minecraft.getMinecraft().currentScreen instanceof GuiModules) {
                this.scrollTop = this.top;
                this.scrollBottom = this.bottom + 45;
            }
            if (Minecraft.getMinecraft().currentScreen instanceof GuiSettings || Minecraft.getMinecraft().currentScreen instanceof GuiModuleSettings) {
                this.scrollTop = this.top + 10;
                this.scrollBottom = this.bottom - 10;
            }
        }
        
        public void expandBy(final int expandBy) {
            this.expandedWidth += expandBy;
        }
        
        protected int getContentHeight() {
            return super.getContentHeight() + this.expandedWidth;
        }
        
        public int getScrollBarX() {
            return this.scrollbarX;
        }
        
        @Override
        public void drawScroll(final int i, final int j) {
            final int j2 = this.func_148135_f();

            if (j2 > 0) {
                int height = (this.scrollBottom - this.scrollTop) * (this.scrollBottom - this.scrollTop) / this.getContentHeight();
                height = MathHelper.clamp_int(height, 32, this.scrollBottom - this.scrollTop - 8);
                height -= (int)Math.min((this.amountScrolled < 0.0) ? ((double)(int)(-this.amountScrolled)) : ((double)((this.amountScrolled > this.func_148135_f()) ? ((int)this.amountScrolled - this.func_148135_f()) : 0)), height * 0.75);
                final int minY = Math.min(Math.max(this.getAmountScrolled() * (this.scrollBottom - this.scrollTop - height) / this.func_148135_f() + this.scrollTop, this.scrollTop), this.scrollBottom - height);
                final Color c = new Color(255, 255, 255, 255);
                GLRectUtils.drawRoundedRect(j - 5, this.scrollTop, j - 5 + 3, this.scrollBottom - 1, 1.5f, new Color(0, 0, 0, 100).getRGB());
                GLRectUtils.drawRoundedRect(j - 5, minY, j - 5 + 3, minY + height - 1, 1.5f, Client.getMainColor(150));
                GLRectUtils.drawRoundedRect(j - 5, minY, j - 5 + 2.75f, minY + height - 1.25f, 1.5f, Client.getMainColor(255));
            }
            this.func_148142_b(this.mouseX, this.mouseY);
        }
        
        @Override
        public void drawScreen(final int mouseXIn, final int mouseYIn, final float p_148128_3_) {
            super.drawScreen(mouseXIn, mouseYIn, p_148128_3_);
            this.mouseX = mouseXIn;
            this.mouseY = mouseYIn;
        }
        
        protected int getSize() {
            if (this.list.size() % this.columns != 0) {
                return (int)Math.ceil(this.list.size() / this.columns);
            }
            return this.list.size() / this.columns;
        }
        
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
        }
        
        protected boolean isSelected(final int slotIndex) {
            return false;
        }
        
        protected void drawBackground() {
        }
        
        protected void drawSlot(final int entryID, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int mouseXIn, final int mouseYIn) {
        }
    }
}
