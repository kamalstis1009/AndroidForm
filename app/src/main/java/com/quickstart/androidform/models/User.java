package com.quickstart.androidform.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String userId;

    @ColumnInfo(name = "image_url")
    private String userImageUrl;

    @NonNull
    private String userName;
    private String userBirth;
    private String userGender;
    private String userLanguage;

    public User() {
    }

    public User(@NonNull String userId, String userImageUrl, @NonNull String userName, String userBirth, String userGender, String userLanguage) {
        this.userId = userId;
        this.userImageUrl = userImageUrl;
        this.userName = userName;
        this.userBirth = userBirth;
        this.userGender = userGender;
        this.userLanguage = userLanguage;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    public String getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(String userBirth) {
        this.userBirth = userBirth;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserLanguage() {
        return userLanguage;
    }

    public void setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
    }
}
