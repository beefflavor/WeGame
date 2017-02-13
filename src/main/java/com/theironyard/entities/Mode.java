package com.theironyard.entities;

import javax.persistence.*;

/**
 * Created by sparatan117 on 1/26/17.
 */
@Entity
public class Mode {
    @Id
    @GeneratedValue
    private int Id;

    @Column(nullable = false)
    private String mode;

    @ManyToOne
    private Game game;

    public Mode() {
    }

    public Mode(String mode, Game game) {
        this.mode = mode;
        this.game = game;
    }

    public Mode(String mode) {
        this.mode = mode;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
