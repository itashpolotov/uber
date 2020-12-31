package com.example.ubertaxi;

public class User {
    String username;
    String email;
    String userType;
    String password;
    String phone;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String email, String userType, String password, String phone) {
        this.username = username;
        this.email = email;
        this.userType = userType;
        this.password = password;
        this.phone = phone;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public User(String username, String email, String userType, String password) {
        this.username = username;
        this.email = email;
        this.userType = userType;
        this.password = password;
    }
}
