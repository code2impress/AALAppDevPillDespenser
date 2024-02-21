package com.example.aal_appdev_pilldespenser;

public class User {
    private String username;
    private String password;
    private String userType; // New field for user type

    // Constructor to initialize user properties
    public User(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    // Getter method for retrieving the username
    public String getUsername() {
        return username;
    }

    // Getter method for retrieving the password
    public String getPassword() {
        return password;
    }

    // Getter method for retrieving the user type
    public String getUserType() {
        return userType;
    }
}
