package com.rastiq.arkemys.discord;

import com.rastiq.arkemys.Client;
import com.rastiq.arkemys.features.SettingsManager;
import com.rastiq.arkemys.utils.HypixelDetector;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.callbacks.ReadyCallback;
import net.minecraft.client.Minecraft;

public class DiscordIPC
{
    public static DiscordIPC INSTANCE = new DiscordIPC();
    public boolean running = false;
    public long created = 0;

    public void start() {
        this.created = System.currentTimeMillis();

        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(new ReadyCallback() {
            @Override
            public void apply(DiscordUser user) {
                Client.info("RPC is launcher with " + user.username +  "#" + user.discriminator);
                update("Chargement...", "");
            }
        }).build();

        DiscordRPC.discordInitialize("860474326851911700", handlers, true);

        running = true;

        new Thread("Discord RPC Callback") {

            @Override
            public void run() {

                while(running) {
                    DiscordRPC.discordRunCallbacks();
                }
            }
        }.start();
    }

    public void restart() {
        this.created = System.currentTimeMillis();

        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(new ReadyCallback() {
            @Override
            public void apply(DiscordUser user) {
                Client.info("RPC is launcher with " + user.username +  "#" + user.discriminator);
                update("Chargement...", "");
            }
        }).build();

        DiscordRPC.discordInitialize("860474326851911700", handlers, true);

        running = true;

        new Thread("Discord RPC Callback") {

            @Override
            public void run() {

                while(running) {
                    DiscordRPC.discordRunCallbacks();
                }
            }
        }.start();

        if (HypixelDetector.isSinglePlayer()) {
            DiscordIPC.INSTANCE.update("En monde solo", "En jeu");
        }else{
            if (HypixelDetector.INSTANCE.isHypixel(Minecraft.getMinecraft().getCurrentServerData())) {
                if (SettingsManager.INSTANCE.discordRPC.getBoolean() == true) {DiscordIPC.INSTANCE.update("Dans un serveur", "Hypixel Network");}
            } else {
                if (SettingsManager.INSTANCE.discordRPC.getBoolean() == true) {DiscordIPC.INSTANCE.update("Dans un serveur", (Minecraft.getMinecraft().getCurrentServerData().serverIP));}
            }
        }
    }

    public void shutdown() {
        running = false;
        DiscordRPC.discordShutdown();
    }

    public void update(String firstLine, String secondLine) {
        DiscordRichPresence.Builder b = new DiscordRichPresence.Builder(secondLine);
        b.setBigImage("large", "");
        b.setDetails(firstLine);
        b.setStartTimestamps(created);

        DiscordRPC.discordUpdatePresence(b.build());
    }
}
