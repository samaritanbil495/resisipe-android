package com.unibodydesignn.resisipe;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("user_nickname")
    @Expose
    private String nickname;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("passwor")
    @Expose
    private String password;

    @SerializedName("id")
    @Expose
    private String userID;

    public User() {
        this.nickname = "";
        this.email = "";
        this.password = "";
        this.userID = "";

    }

    public User(String nickname, String email, String password, String userID) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.userID = userID;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
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

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return this.userID;
    }
}
