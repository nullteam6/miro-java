package com.nullteam6.models;


public class UserDTO {
    private String firstName;
    private String lastName;
    private String entryId;
    private String uid;
    private String mail;
    private ProfileDTO profile;

    public UserDTO(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.entryId = user.getEntryId();
        this.uid = user.getUid();
        this.mail = user.getMail();
        this.profile = new ProfileDTO(user.getProfile());
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

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public ProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(ProfileDTO profile) {
        this.profile = profile;
    }
}

