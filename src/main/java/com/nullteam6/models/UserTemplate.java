package com.nullteam6.models;

import java.io.Serializable;

public class UserTemplate implements Serializable {
    private static final long serialVersionUID = 2923742724056556068L;

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private List<User> friend;

    public UserTemplate() {
        super();
    }

    public UserTemplate(String firstName, String lastName, String username, String password, List<User> friend) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.friend=friend;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public List<User> getFriend()
    {
        return friend;
    }
    public void setFriend(List<User> friend)
    {
        this.friend=friend;
    }
}
