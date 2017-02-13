package com.theironyard.entities;

import javax.persistence.*;

/**
 * Created by sparatan117 on 1/25/17.
 */
@Entity
public class Rating {
    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private int friendliness;

    @Column(nullable = false)
    private int skill;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    private User user;

    @ManyToOne
    private User creator;

    public Rating() {
    }

    public Rating(int friendliness, int skill, String comment, User user, User creator) {
        this.friendliness = friendliness;
        this.skill = skill;
        this.comment = comment;
        this.user = user;
        this.creator = creator;
    }

    public Rating(String friendliness, String skill, String comment, User user, User creator) {
        this.friendliness = Integer.parseInt(friendliness);
        this.skill = Integer.parseInt(skill);
        this.comment = comment;
        this.user = user;
        this.creator = creator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFriendliness() {
        return friendliness;
    }

    public void setFriendliness(int friendliness) {
        this.friendliness = friendliness;
    }

    public int getSkill() {
        return skill;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
