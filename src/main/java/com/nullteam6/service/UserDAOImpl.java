package com.nullteam6.service;

import com.nullteam6.models.*;
import com.nullteam6.utility.PaginatedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.support.LdapNameBuilder;

import javax.naming.Name;
import javax.persistence.NoResultException;
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
    public UserDTO findByUsername(String uid) {
        User u = ldapTemplate.findOne(
                query().where("objectclass").is("person").and("uid").is(uid),
                User.class);
        ProfileDTO pDTO;
        try {
            pDTO = profileDAO.getProfileByUID(uid);
        } catch (NoResultException ex) {
            Profile p = new Profile();
            p.setUid(uid);
            List<Profile> followingList = new ArrayList<>();
            p.setFollowingList(followingList);
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
            pDTO = new ProfileDTO(p);
        }
        u.setProfile(new Profile(pDTO));
        return new UserDTO(u);
    }

    public PaginatedList<User> getAll() {
        int start = 0;
        return buildUserPage(ldapTemplate.findAll(User.class), start);
    }

    public PaginatedList<User> getAllOffset(int offset) {
        return buildUserPage(ldapTemplate.findAll(User.class), offset);
    }

    /**
     * DEPRECATED - authentication is now handled via Keycloak
     *
     * @param template the LoginTemplate to authenticate
     * @return boolean value denoting success or failure
     */
    @Override
    public boolean authenticate(LoginTemplate template) {
        LdapContextSource contextSource = new LdapContextSource();
        Name dn = buildFullUserNameDn(template.getUsername());
        contextSource.setUrl("ldap://miro.5x5code.com");
        contextSource.setBase("dc=miro,dc=5x5code,dc=com");
        contextSource.setUserDn(dn.toString());
        contextSource.setPassword(template.getPassword());
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
     * Constructs the full Name of the user
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

    private PaginatedList<User> buildUserPage(List<User> userList, int offset) {
        PaginatedList<User> userPage = new PaginatedList<>();
        userPage.setTotalCount(userList.size());
        if ((offset + 10) < userList.size())
            userPage.setData(userList.subList(offset, offset + 10));
        else if (userList.size() - offset < 10)
            userPage.setData(userList.subList(offset, userList.size() - 1));
        else
            userPage.setData(userList.subList(offset, offset + 10));
        return userPage;
    }
}
