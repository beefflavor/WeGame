package com.theironyard.entities;

import javax.persistence.*;
import java.util.List;

/**
 * Created by sparatan117 on 1/28/17.
 */
@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private User playerOne;

    @ManyToOne
    private User playerTwo;

    @ManyToOne
    private Game pickedGame;

    @ManyToOne
    private Mode pickedMode;

    private boolean isMatched;

    private boolean isAccepted;


    public Match(){}


    public Match(User playerOne, Game pickedGame) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.pickedGame = pickedGame;
        this.pickedMode = pickedMode;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(User playerOne) {
        this.playerOne = playerOne;
    }

    public User getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(User playerTwo) {
        this.playerTwo = playerTwo;
    }

    public Game getPickedGame() {
        return pickedGame;
    }

    public void setPickedGame(Game pickedGame) {
        this.pickedGame = pickedGame;
    }

    public Mode getPickedMode() {
        return pickedMode;
    }

    public void setPickedMode(Mode pickedMode) {
        this.pickedMode = pickedMode;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
