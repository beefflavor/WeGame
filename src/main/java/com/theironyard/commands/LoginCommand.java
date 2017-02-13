package com.theironyard.commands;

import javax.validation.constraints.Size;

/**
 * Created by sparatan117 on 1/26/17.
 */
public class LoginCommand {
    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
