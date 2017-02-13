package com.theironyard.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 * Created by sparatan117 on 1/25/17.
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String nameFirst;

    @Column(nullable = false)
    private String nameLast;

    @Column(nullable = false)
    private String pictureUrl;

    @Column(nullable = false)
    private String bio;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String sex;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int age;

    private String gamerTag;

    private double friendAvg;

    private double skillAvg;

    @ManyToMany
    private List<Game> games;

    @OneToMany
    private List<Rating> ratings;



    public User() {
    }
    public User(String nameFirst, String nameLast, String pictureUrl, String bio, String email, String phone, String sex,
                String username, String password, String age) {
        this.nameFirst = nameFirst;
        this.nameLast = nameLast;
        this.pictureUrl = pictureUrl;
        this.bio = bio;
        this.email = email;
        this.phone =phone;
        this.sex = sex;
        this.username = username;
        this.password = password;
        this.age = Integer.parseInt(age);

    }

    public User(String nameFirst, String nameLast, String pictureUrl, String bio, String email, String phone, String sex,
                String username, String password, int age) {
        this.nameFirst = nameFirst;
        this.nameLast = nameLast;
        this.pictureUrl = pictureUrl;
        this.bio = bio;
        this.email = email;
        this.phone = phone;
        this.sex = sex;
        this.username = username;
        this.password = password;
        this.age = age;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameFirst() {
        return nameFirst;
    }

    public void setNameFirst(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    public String getNameLast() {
        return nameLast;
    }

    public void setNameLast(String nameLast) {
        this.nameLast = nameLast;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Collection<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public String getGamerTag() {
        return gamerTag;
    }

    public void setGamerTag(String gamerTag) {
        this.gamerTag = gamerTag;
    }

    public double getFriendAvg() {
        return friendAvg;
    }

    public void setFriendAvg(double friendAvg) {
        this.friendAvg = friendAvg;
    }

    public double getSkillAvg() {
        return skillAvg;
    }

    public void setSkillAvg(double skillAvg) {
        this.skillAvg = skillAvg;
    }
}
