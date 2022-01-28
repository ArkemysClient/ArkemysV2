package com.rastiq.arkemys.config;

import com.rastiq.arkemys.Client;
import com.rastiq.arkemys.config.utils.Config;
import com.rastiq.arkemys.features.SettingsManager;
import com.rastiq.arkemys.utils.Setting;
import com.rastiq.arkemys.utils.SettingWrapper;
import com.rastiq.arkemys.config.utils.*;
import java.io.*;
import com.rastiq.arkemys.features.*;
import com.rastiq.arkemys.utils.*;
import java.util.*;
import com.rastiq.arkemys.*;
import com.google.gson.*;

public class GeneralConfig extends Config
{
    public static final GeneralConfig INSTANCE;
    
    public GeneralConfig() {
        super("general", "json", 0.1);
    }
    
    @Override
    public void saveConfig() {
        this.createStructure();
        final JsonObject configFileJson = new JsonObject();
        configFileJson.addProperty("version", (Number)this.version);
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(this.configFile));
            try {
                for (final Setting setting : SettingsManager.INSTANCE.settings) {
                    if (!setting.hasValue()) {
                        continue;
                    }
                    SettingWrapper.addSettingKey(configFileJson, setting, setting.getObject());
                }
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(configFileJson.toString())));
            }
            finally {
                if (Collections.singletonList(writer).get(0) != null) {
                    writer.close();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void loadConfig() {
        this.createStructure();
        final JsonObject configFileJson = this.loadJsonFile(this.configFile);
        this.getNonNull(configFileJson, "version", jsonElement -> Client.info("Detected " + this.name + " version: " + jsonElement.getAsDouble() + " => " + this.version, new Object[0]));
        for (final Setting setting : SettingsManager.INSTANCE.settings) {
            if (!setting.hasValue()) {
                continue;
            }
            this.getNonNull(configFileJson, setting.getKey(), jsonElement -> SettingWrapper.setValue(setting, jsonElement));
        }
    }
    
    static {
        INSTANCE = new GeneralConfig();
    }
}
