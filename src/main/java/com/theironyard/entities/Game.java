package com.theironyard.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 * Created by sparatan117 on 1/25/17.
 */
@Entity
public class Game {
    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String platform;

    @ManyToMany
    private List<Mode> modes;

    public Game() {
    }

    public Game(String name, String platform) {
        this.name = name;
        this.platform = platform;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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


    public Collection<Mode> getModes() {
        return modes;
    }

    public void setModes(List<Mode> modes) {
        this.modes = modes;
    }
}
