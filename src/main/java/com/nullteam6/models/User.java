package com.nullteam6.models;

import javax.persistence.*;

@Entity
@Table(name = "miro_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean admin;

    public User() {
        super();
    }

    public User(String firstName, String lastName, String username, String password, boolean admin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.admin = admin;
    }

    public User(UserTemplate template) {
        this.firstName = template.getFirstName();
        this.lastName = template.getLastName();
        this.username = template.getUsername();
        this.password = template.getPassword();
        this.admin = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean getAdmin() {
        return this.admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
