package com.nullteam6.service;

import com.nullteam6.models.*;
import com.nullteam6.utility.PaginatedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.support.LdapNameBuilder;

import javax.naming.Name;
import javax.persistence.NoResultException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

public class UserDAOImpl implements UserDAO {
    private static final String ACCOUNT = "accounts";
    private final Logger logger = LogManager.getLogger();
    private LdapTemplate ldapTemplate;
    private ProfileDAO profileDAO;

    @Autowired
    public void setLdapTemplate(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    @Autowired
    public void setProfileDAO(ProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }

    /**
     * returns a user based on their uid. Creates and persists a profile if one does not already exists
     *
     * @param uid the uid of the user to lookup
     * @return the User
     */
    @Override
    public User findByUsername(String uid) {
        User u = ldapTemplate.findOne(
                query().where("objectclass").is("person").and("uid").is(uid),
                User.class);
        Profile p;
        try {
            p = profileDAO.getProfileByUID(uid);
        } catch (NoResultException ex) {
            p = new Profile();
            p.setUid(uid);
            AnimeBacklog log = new AnimeBacklog();
            List<Anime> backList = new ArrayList<>();
            List<Anime> watchingList = new ArrayList<>();
            List<Anime> finishedList = new ArrayList<>();
            List<Anime> droppedList = new ArrayList<>();
            log.setBacklist(backList);
            log.setInProgList(watchingList);
            log.setFinishedList(finishedList);
            log.setDroppedList(droppedList);
            p.setAniBacklog(log);
            profileDAO.createProfile(p);
        }
        u.setProfile(p);
        return u;
    }

    public PaginatedList<User> getAll() {
        int start = 0;
        List<User> uList = ldapTemplate.findAll(User.class);
        PaginatedList<User> userList = new PaginatedList<>();
        userList.setTotalCount(uList.size());
        if (uList.size() > 10)
            userList.setData(uList.subList(start, start + 10));
        else
            userList.setData(uList);
        return userList;
    }

    /**
     * DEPRECATED - Use Keycloak to register now
     *
     * @param template the UserTemplate to register
     * @return boolean value denoting success or failure
     * @throws NoSuchAlgorithmException if the password encryption algorithm is not found
     */
    @Override
    public boolean registerUser(UserTemplate template) throws NoSuchAlgorithmException {
        Name dn = buildShortUserNameDn(template.getUsername());
        DirContextAdapter context = new DirContextAdapter(dn);
        context.setAttributeValues(
                "objectclass",
                new String[]{"top", "person", "organizationalPerson", "inetOrgPerson", "inetUser"}
        );
        context.setAttributeValue("mail", template.getEmail());
        context.setAttributeValue("givenName", template.getFirstName());
        context.setAttributeValue("cn", String.format("%s %s", template.getFirstName(), template.getLastName()));
        context.setAttributeValue("sn", template.getLastName());
        context.setAttributeValue("userPassword", digestMD5(template.getPassword()));
        ldapTemplate.bind(context);
        Name fullDn = buildFullUserNameDn(template.getUsername());
        setIpaUsersMembership(fullDn);
        setMiroUsersMembership(fullDn);
        Profile profile = new Profile();
        profile.setUid(template.getUsername());
        profileDAO.createProfile(profile);
        return true;
    }

    /**
     * DEPRECATED - authentication is now handled via Keycloak
     *
     * @param template the LoginTemplate to authenticate
     * @return boolean value denoting success or failure
     * @throws NoSuchAlgorithmException exception thrown if the password hashing algorithm is not found
     */
    @Override
    public boolean authenticate(LoginTemplate template) throws NoSuchAlgorithmException {
        LdapContextSource contextSource = new LdapContextSource();
        Name dn = buildFullUserNameDn(template.getUsername());
        contextSource.setUrl("ldap://miro.5x5code.com");
        contextSource.setBase("dc=miro,dc=5x5code,dc=com");
        contextSource.setUserDn(dn.toString());
        contextSource.setPassword(digestMD5(template.getPassword()));
        contextSource.afterPropertiesSet();
        LdapTemplate ldapTemplate1 = new LdapTemplate(contextSource);
        try {
            ldapTemplate1.afterPropertiesSet();
            ldapTemplate1.lookup(dn);
            return true;
        } catch (Exception ex) {
            logger.info(ex.toString());
            return false;
        }
    }

    /**
     * DEPRECATED - updating user details is now handled by Keycloak
     *
     * @param user
     */
    @Override
    public void updateUser(User user) {
        //TODO: Don't implement this
    }

    /**
     * Hashes a password according to MD5
     *
     * @param input the password
     * @return the hashed password
     * @throws NoSuchAlgorithmException if MD5 is not found
     */
    private String digestMD5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Adds user to cn=ipausers,cn=groups,cn=accounts
     *
     * @param fullUserDn
     */
    private void setIpaUsersMembership(Name fullUserDn) {
        Name dn = LdapNameBuilder.newInstance()
                .add("cn", ACCOUNT)
                .add("cn", "groups")
                .add("cn", "ipausers")
                .build();
        DirContextOperations context = ldapTemplate.lookupContext(dn);
        context.addAttributeValue("member", fullUserDn.toString());
        ldapTemplate.modifyAttributes(context);
    }

    /**
     * adds user to cn=miro_users,cn=groups,cn=accounts
     *
     * @param fullUserDn
     */
    private void setMiroUsersMembership(Name fullUserDn) {
        Name dn = LdapNameBuilder.newInstance()
                .add("cn", ACCOUNT)
                .add("cn", "groups")
                .add("cn", "miro_users")
                .build();
        DirContextOperations context = ldapTemplate.lookupContext(dn);
        context.addAttributeValue("member", fullUserDn.toString());
        ldapTemplate.modifyAttributes(context);
    }

    /**
     * Constructs the short hand Name of the user
     *
     * @param name the username
     * @return the shorthand Name
     */
    private Name buildShortUserNameDn(String name) {
        return LdapNameBuilder.newInstance()
                .add("cn", ACCOUNT)
                .add("cn", "users")
                .add("uid", name)
                .build();
    }

    /**
     * Constructss the full Name of the user
     *
     * @param name the username
     * @return the full Name
     */
    private Name buildFullUserNameDn(String name) {
        return LdapNameBuilder.newInstance("dc=miro,dc=5x5code,dc=com")
                .add("cn", ACCOUNT)
                .add("cn", "users")
                .add("uid", name)
                .build();
    }
}
