package com.rastiq.arkemys.network;

import com.rastiq.arkemys.Client;
import org.java_websocket.client.*;
import java.net.*;
import org.java_websocket.handshake.*;
import com.rastiq.arkemys.*;

public class ClientConnection extends WebSocketClient
{
    public ClientConnection(final URI serverUri) {
        super(serverUri);
    }
    
    public void onOpen(final ServerHandshake handshakedata) {
        this.send("I am connected!");
        Client.info("Connected to server successfully!", new Object[0]);
    }
    
    public void onMessage(final String message) {
        Client.info("I got this: " + message, new Object[0]);
    }
    
    public void onClose(final int code, final String reason, final boolean remote) {
    }
    
    public void onError(final Exception ex) {
    }
}
