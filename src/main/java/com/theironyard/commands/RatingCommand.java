package com.theironyard.commands;

import javax.validation.constraints.Size;

/**
 * Created by sparatan117 on 2/7/17.
 */
public class RatingCommand {
    @Size(max = 2)
    private String friendliness;
    @Size(max = 2)
    private String skill;
    @Size(max = 500)
    private String comment;

    public String getFriendliness() {
        return friendliness;
    }

    public void setFriendliness(String friendliness) {
        this.friendliness = friendliness;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
