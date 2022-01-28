package com.rastiq.arkemys.event.events;

import com.rastiq.arkemys.event.Event;
import com.rastiq.arkemys.event.*;
import net.minecraft.client.gui.*;

public class GuiScreenEvent extends Event
{
    public final GuiScreen screen;
    
    public GuiScreenEvent(final GuiScreen screen) {
        this.screen = screen;
    }
    
    public static class Open extends GuiScreenEvent
    {
        public Open(final GuiScreen screen) {
            super(screen);
        }
    }
}
