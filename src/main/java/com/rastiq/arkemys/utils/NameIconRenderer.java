package com.rastiq.arkemys.utils;

import java.util.HashMap;

public class NameIconRenderer {
    public static NameIconRenderer INSTANCE = new NameIconRenderer();
    public Timer renderIconsTimer = new Timer();
    public HashMap<String, Boolean> hasRenderedIcons = new HashMap();
    public HashMap<String, Boolean> isUsingArkemys = new HashMap();

    public void reset() {
        renderIconsTimer.reset();
    }

    public boolean hasFinished() {
        return renderIconsTimer.hasTimeElapsed(30000, true);
    }

}
