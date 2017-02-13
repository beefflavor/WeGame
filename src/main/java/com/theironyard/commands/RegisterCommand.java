package com.theironyard.commands;

import javax.validation.constraints.Size;

/**
 * Created by sparatan117 on 1/26/17.
 */
public class RegisterCommand {
    @Size(max = 25)
    private String nameFirst;
    @Size(max = 25)
    private String nameLast;
    @Size(max = 1000)
    private String pictureUrl;
    @Size(max = 1000)
    private String bio;
    @Size(max = 50)
    private String email;
    @Size(max = 25)
    private String phone;
    private String sex;
    @Size(min = 3, max = 25)
    private String username;
    @Size(min = 5, max = 30)
    private String password;
    @Size(max = 3)
    private String age;

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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
