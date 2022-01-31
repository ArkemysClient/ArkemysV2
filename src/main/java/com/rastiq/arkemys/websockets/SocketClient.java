package com.rastiq.arkemys.websockets;

import java.io.IOException;
import co.gongzh.procbridge.Client;

import java.net.ServerSocket;

public class SocketClient {

    public static final Client client = new Client("localhost", 80);

    public static void main(String[] args) {

        while(true) {

        }
    }
    
    public static boolean isUser(String username) {
    	String[] arguments = client.request("isUser", username).toString().split(":");
        if(arguments[0].equals("true")) {
            //System.out.println("returned as true for user " + username);
        	return true;
        } else if (arguments[0].equals("false")) {
        	//System.out.println("returned as false for user " + username);
        	return false;
        } else {
        	System.out.println("there was an error for " + username);
        	return false;
        }
    }

    public static double randomNumber(double max, double min) {
        return (Math.random() * (max - min)) + min;
    }
}