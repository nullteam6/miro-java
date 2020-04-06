package com.nullteam6.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Transient;

import javax.naming.Name;
import java.util.Objects;

@Entry(objectClasses = {"person", "top"})
public class User {
    @Id
    @JsonIgnore
    private Name dn;

    @Attribute(name = "givenName")
    private String firstName;

    @Attribute(name = "sn")
    private String lastName;

    @Attribute
    private String entryId;

    @Attribute
    private String uid;

    @Attribute
    private String mail;

    @Transient
    private Profile profile;


    public User() {
        super();
    }


    public User(Name dn, String firstName, String lastName, String entryId, String uid, String mail, Profile profile) {
        this.dn = dn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.entryId = entryId;
        this.uid = uid;
        this.mail = mail;
        this.profile = profile;

    }

    public Name getDn() {
        return dn;
    }

    public void setDn(Name dn) {
        this.dn = dn;
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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return dn.equals(user.dn) &&
                firstName.equals(user.firstName) &&
                lastName.equals(user.lastName) &&
                entryId.equals(user.entryId) &&
                uid.equals(user.uid) &&
                mail.equals(user.mail) &&
                profile.equals(user.profile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dn, firstName, lastName, entryId, uid, mail, profile);
    }

    @Override
    public String toString() {
        return "User{" +
                "dn=" + dn +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", entryId='" + entryId + '\'' +
                ", uid='" + uid + '\'' +
                ", mail='" + mail + '\'' +
                ", profile=" + profile +
                '}';
    }
}

