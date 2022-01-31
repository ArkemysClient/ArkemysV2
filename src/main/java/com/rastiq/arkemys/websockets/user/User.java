package com.rastiq.arkemys.websockets.user;

public class User {

    public String mcName;
    public boolean isUser;

    public User(String mcName, boolean isUser) {;
        this.mcName = mcName;
        this.isUser = isUser;
    }

    public String getProperties() {
        return mcName + ":" + (isUser ? "true" : "false");
    }

    public String getMcName() {
        return mcName;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setMcName(String mcName) {
        this.mcName = mcName;
    }


    public void setUser(boolean user) {
        isUser = user;
    }
}
