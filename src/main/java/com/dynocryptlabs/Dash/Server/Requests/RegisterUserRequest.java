package com.dynocryptlabs.Dash.Server.Requests;

/**
 * Created by rohanpanchal on 1/31/17.
 *
 * A Request model for User Registration parameters.
 */
public class RegisterUserRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    //================================================================================
    // Instance Methods
    //================================================================================

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
