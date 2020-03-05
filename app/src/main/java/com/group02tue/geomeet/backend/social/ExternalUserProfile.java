package com.group02tue.geomeet.backend.social;

/**
 * User profile of a user different from the user logged in.
 */
public class ExternalUserProfile extends UserProfile {
    public ExternalUserProfile(String firstName, String lastName, String email, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.description = description;
    }
}
