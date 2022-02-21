package com.rastiq.arkemys.utils;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.server.MinecraftServer;

import java.util.regex.Pattern;

public class HypixelDetector {

    public static final HypixelDetector INSTANCE = new HypixelDetector();

    public boolean isHypixel(ServerData server) {
        Pattern HYPIXEL_PATTERN = Pattern.compile("^(?:(?:(?:.+\\.)?hypixel\\.net)|(?:209\\.222\\.115\\.\\d{1,3})|(?:99\\.198\\.123\\.[123]?\\d?))\\.?(?::\\d{1,5}\\.?)?$", Pattern.CASE_INSENSITIVE);
        boolean hypixel = HYPIXEL_PATTERN.matcher(server.serverIP).find();
        if (hypixel) return true;
        else return false;
    }

    public static boolean isSinglePlayer()
    {
        try
        {
            if( MinecraftServer.getServer().isServerRunning() )
            {
                return MinecraftServer.getServer().isSinglePlayer();
            }
            return false;
        }
        catch( Exception e ) // Server is null, not started
        {
            return false;
        }
    }

}
