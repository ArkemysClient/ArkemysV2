package com.rastiq.arkemys.event.events;

import com.rastiq.arkemys.event.Event;
import com.rastiq.arkemys.event.*;

public class KeyInputEvent extends Event
{
    public final int keycode;
    
    public KeyInputEvent(final int keycode) {
        this.keycode = keycode;
    }
}
