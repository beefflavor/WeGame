package com.theironyard.commands;

/**
 * Created by sparatan117 on 1/28/17.
 */
public class GameCommand {
    private String name;
    private String platform;
    private String playerCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(String playerCount) {
        this.playerCount = playerCount;
    }
}
