package com.example.aal_appdev_pilldespenser;

public class User {
    private String username;
    private String password;
    private String userType; // New field for user type

    // Updated constructor
    public User(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    // Getter methods
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }


}
