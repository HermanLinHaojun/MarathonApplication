package com.example.marathonapplication.db.user;

public class LoginUser {
    private String mId;
    private String mUserId;
    private String mEmail;
    private String mUserName;
    private String mUserAddress;
    private String mPassword;

    public LoginUser(String mId, String mUserId, String mEmail, String mUserName, String mUserAddress, String mPassword) {
        this.mId = mId;
        this.mUserId = mUserId;
        this.mEmail = mEmail;
        this.mUserName = mUserName;
        this.mUserAddress = mUserAddress;
        this.mPassword = mPassword;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getUserAddress() {
        return mUserAddress;
    }

    public void setUserAddress(String userAddress) {
        mUserAddress = userAddress;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }
}
