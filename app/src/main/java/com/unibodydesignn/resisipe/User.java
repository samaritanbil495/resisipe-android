package com.unibodydesignn.resisipe;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("user_nickname")
    @Expose
    private String nickname;

    @SerializedName("user_email")
    @Expose
    private String email;

    @SerializedName("user_password")
    @Expose
    private String password;

    public User() {
        nickname = "";
        email = "";
        password = "";
    }

    public User(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
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
}
