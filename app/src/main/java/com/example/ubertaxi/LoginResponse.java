package com.example.ubertaxi;

public class LoginResponse {
 String    username;
 String    email;
 String    userType;
 String    sessionToken;
 String    phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public LoginResponse(String username, String email, String userType, String sessionToken) {
        this.username = username;
        this.email = email;
        this.userType = userType;
        this.sessionToken = sessionToken;
    }
}
