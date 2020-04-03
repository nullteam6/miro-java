package com.nullteam6.models;

import java.io.Serializable;

public class UserTemplate implements Serializable {

    private static final long serialVersionUID = 2923742724056556068L;
    private String username;
    private String firstName;
    private String lastName;
    private String password;

    public UserTemplate() {
        super();
    }

    public UserTemplate(String userName, String firstName, String lastName, String password) {
        this.username = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }
    
}