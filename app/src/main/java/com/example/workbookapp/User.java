package com.example.workbookapp;

import java.util.Date;

public class User {
    private int id;
    private String fullName;
    private String username;
    private String password;
    private String avatar;
    private Date birthDay;
    private Date createdAt;
    private Date updatedAt;

    public User() {
        // Khởi tạo một user mới
    }

    public User(String fullName, String username, String password, String avatar, Date birthDay) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.birthDay = birthDay;
    }

    // Các phương thức getter và setter cho các trường của User
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}