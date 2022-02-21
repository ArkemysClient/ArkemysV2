package com.rastiq.arkemys.launch;

import com.google.common.collect.*;
import java.io.*;
import java.util.*;
import net.minecraft.launchwrapper.*;
import org.spongepowered.asm.launch.*;
import org.spongepowered.asm.mixin.*;
import org.apache.logging.log4j.*;

public class ArkemysTweaker implements ITweaker
{
    private final List<String> launchArguments;
    public static final Logger logger;
    
    public ArkemysTweaker() {
        this.launchArguments = Lists.newArrayList();
    }

    private static final boolean isOptifineLoaded = isClassLoaded("net.optifine.Lang");
    
    public void acceptOptions(final List<String> args, final File gameDir, final File assetsDir, final String profile) {
        if (!isOptifineLoaded) {
            this.launchArguments.addAll(args);
            if (!args.contains("--version") && profile != null) {
                this.launchArguments.add("--version");
                this.launchArguments.add(profile);
            }

            if (!args.contains("--assetsDir") && assetsDir != null) {
                this.launchArguments.add("--assetsDir");
                this.launchArguments.add(assetsDir.getAbsolutePath());
            }

            if (!args.contains("--gameDir") && gameDir != null) {
                this.launchArguments.add("--gameDir");
                this.launchArguments.add(gameDir.getAbsolutePath());
            }

        }

    }
    
    public void injectIntoClassLoader(final LaunchClassLoader classLoader) {
        MixinBootstrap.init();
        final MixinEnvironment env = MixinEnvironment.getDefaultEnvironment();
        Mixins.addConfiguration("mixins.arkemys.json");
        if (env.getObfuscationContext() == null) {
            env.setObfuscationContext("notch");
        }
        env.setSide(MixinEnvironment.Side.CLIENT);
        ArkemysTweaker.logger.info("Registering transformers");
        classLoader.registerTransformer("com.rastiq.arkemys.asm.CameraTransformer");
    }
    
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }
    
    public String[] getLaunchArguments() {
        return (String[])this.launchArguments.toArray(new String[0]);

    }

    private static boolean isClassLoaded(String var0) {
        try {
            Class.forName(var0);
            return true;
        } catch (ClassNotFoundException var2) {
            return false;
        }
    }


    static {
        logger = LogManager.getLogger();
    }
}
