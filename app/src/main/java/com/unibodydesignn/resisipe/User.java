package com.unibodydesignn.resisipe;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("nickname")
    @Expose
    private String nickname;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("remainingTime")
    private Integer remainingTime;

    public User() {
        this.email = "";
        this.password = "";
        this.remainingTime = 0;
        this.nickname = "";
    }

    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public String getId() {
        return this.id;
    }

    public void setRemainingTime(Integer remainingTime) {
        this.remainingTime = remainingTime;
    }

    public Integer getRemainingTime() {
        return this.remainingTime;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }
}
