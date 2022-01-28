package com.rastiq.arkemys.features;

import com.google.common.collect.*;
import com.rastiq.arkemys.features.modules.utils.DefaultModuleRenderer;
import com.rastiq.arkemys.features.modules.utils.IModuleRenderer;
import org.reflections.*;
import com.rastiq.arkemys.features.modules.utils.*;

import java.util.stream.*;
import java.util.*;

public class ModuleManager
{
    public static final ModuleManager INSTANCE;
    public LinkedHashSet<Module> modules;
    
    public ModuleManager() {
        this.modules = Sets.newLinkedHashSet();
    }
    
    public void init() {
        final Set<Class<? extends Module>> modules = new Reflections("com.rastiq.arkemys.features.modules").getSubTypesOf(Module.class);
        modules.forEach(module -> {
            if (module == IModuleRenderer.class || module == DefaultModuleRenderer.class) {
                return;
            }
            else {
                try {
                    module.newInstance();
                }
                catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                return;
            }
        });
        this.modules = this.modules.stream().sorted((o1, o2) -> {
            if (o1.isRender() && !o2.isRender()) {
                return -1;
            }
            else if (!o1.isRender() && o2.isRender()) {
                return 1;
            }
            else {
                return o1.getKey().compareTo(o2.getKey());
            }
        }).collect(Collectors.toCollection(LinkedHashSet::new));
    }
    
    static {
        INSTANCE = new ModuleManager();
    }
}
