package com.rastiq.arkemys.utils;

import net.minecraft.client.multiplayer.ServerData;

import java.util.regex.Pattern;

public class HypixelDetector {

    public static final HypixelDetector INSTANCE = new HypixelDetector();

    public boolean isHypixel(ServerData server) {
        Pattern HYPIXEL_PATTERN = Pattern.compile("^(?:(?:(?:.+\\.)?hypixel\\.net)|(?:209\\.222\\.115\\.\\d{1,3})|(?:99\\.198\\.123\\.[123]?\\d?))\\.?(?::\\d{1,5}\\.?)?$", Pattern.CASE_INSENSITIVE);
        boolean hypixel = HYPIXEL_PATTERN.matcher(server.serverIP).find();
        if (hypixel) return true;
        else return false;
    }

}
