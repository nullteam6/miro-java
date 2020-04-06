package com.nullteam6.service;

import com.nullteam6.models.LoginTemplate;
import com.nullteam6.models.Profile;
import com.nullteam6.models.User;
import com.nullteam6.models.UserTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.support.LdapNameBuilder;

import javax.naming.Name;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

public class UserDAOImpl implements UserDAO {
    private static final String ACCOUNT = "accounts";
    private Logger logger = LogManager.getLogger();
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

    @Override
    public User findByUsername(String uid) {
        User u = ldapTemplate.findOne(
                query().where("objectclass").is("person").and("uid").is(uid),
                User.class);
        Profile p;
        try {
            p = profileDAO.getProfileByUID(uid);
        } catch (IllegalArgumentException ex) {
            p = new Profile();
            p.setUid(uid);
            profileDAO.createProfile(p);
        }
        u.setProfile(p);
        return u;
    }

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
            return true;
        } catch (Exception ex) {
            logger.info(ex.toString());
            return false;
        }
    }

    @Override
    public void updateUser(User user) {
        //TODO: yeet
    }

    private String digestMD5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

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

    private Name buildShortUserNameDn(String name) {
        return LdapNameBuilder.newInstance()
                .add("cn", ACCOUNT)
                .add("cn", "users")
                .add("uid", name)
                .build();
    }

    private Name buildFullUserNameDn(String name) {
        return LdapNameBuilder.newInstance("dc=miro,dc=5x5code,dc=com")
                .add("cn", ACCOUNT)
                .add("cn", "users")
                .add("uid", name)
                .build();
    }
}
