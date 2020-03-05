package com.group02tue.geomeet.backend.social;

public abstract class UserProfile {
    protected String firstName = "";
    protected String lastName = "";
    protected String email = "";
    protected String description = "";

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public String getDescription() {
        return description;
    }
}
