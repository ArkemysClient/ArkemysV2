package com.rastiq.arkemys.features.modules;

import com.rastiq.arkemys.features.modules.utils.DefaultModuleRenderer;
import com.rastiq.arkemys.features.modules.utils.*;
import java.text.*;
import java.util.*;

public class TimeDisplayModule extends DefaultModuleRenderer
{
    public TimeDisplayModule() {
        super("Time Display", 14);
    }
    
    @Override
    public String getFormat() {
        return "[%value%]";
    }
    
    @Override
    public Object getValue() {
        return new SimpleDateFormat("kk:mm").format(new Date());
    }
}
