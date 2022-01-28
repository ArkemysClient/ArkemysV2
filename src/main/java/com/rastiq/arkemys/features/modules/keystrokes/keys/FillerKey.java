package com.rastiq.arkemys.features.modules.keystrokes.keys;

import com.rastiq.arkemys.features.modules.keystrokes.KeystrokesModule;
import com.rastiq.arkemys.features.modules.keystrokes.*;

public class FillerKey extends Key
{
    public FillerKey(final int gapSize, final KeystrokesModule keystrokesModule) {
        super(gapSize, null, keystrokesModule);
    }
    
    @Override
    public void render() {
    }
}
