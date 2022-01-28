package com.rastiq.arkemys.config;

import java.util.*;

import com.rastiq.arkemys.Client;
import com.rastiq.arkemys.config.utils.Config;
import com.rastiq.arkemys.config.utils.*;
import com.google.common.collect.*;
import com.rastiq.arkemys.*;

public class ConfigManager
{
    public static final ConfigManager INSTANCE;
    private final List<Config> configs;
    
    public ConfigManager() {
        (this.configs = Lists.newArrayList()).add(ModuleConfig.INSTANCE);
        this.configs.add(GeneralConfig.INSTANCE);
    }

    public void saveAll() {
        this.configs.forEach((config) -> {
            long start = System.currentTimeMillis();
            Client.info("Saving " + config.name + "...");
            config.saveConfig();
            Client.info("Saved in " + (System.currentTimeMillis() - start) + "ms!");
        });
    }

    public void loadAll() {
        this.configs.forEach((config) -> {
            long start = System.currentTimeMillis();
            Client.info("Loading " + config.name + "...");
            config.loadConfig();
            Client.info("Loaded in " + (System.currentTimeMillis() - start) + "ms!");
        });
    }
    
    static {
        INSTANCE = new ConfigManager();
    }
}
