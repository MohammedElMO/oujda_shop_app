package com.example.oujda_shop.entities;


public class User {

    private String firstName;
    private String lastName;
    private String password;
    private String profilePath;
    private String email;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }
    public User(String firstName, String lastName, String email, String password,String profilePath) {
        this.firstName = firstName;
        this.profilePath = profilePath;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    public User(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}
