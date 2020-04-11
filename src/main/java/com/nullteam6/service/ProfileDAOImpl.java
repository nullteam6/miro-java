package com.nullteam6.service;

import com.nullteam6.models.Profile;
import com.nullteam6.utility.PaginatedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class ProfileDAOImpl implements ProfileDAO {

    private SessionFactory sessionFactory;
    private final Logger logger = LogManager.getLogger();

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Get a profile from a user by the user's uid
     *
     * @param uid uid of the user
     * @return the user's profile
     */
    @Override
    public Profile getProfileByUID(String uid) {
        String hql = "FROM Profile P WHERE P.uid = :uid";
        Profile p;
        try (Session s = sessionFactory.openSession()) {
            p = (Profile) s.createQuery(hql)
                    .setParameter("uid", uid)
                    .getSingleResult();
        }
        return p;
    }

    /**
     * Updates a user profile
     *
     * @param profile Profile to be updated
     * @return boolean value denoting success or failure
     */
    @Override
    public boolean updateProfile(Profile profile) {
        Session s = sessionFactory.getCurrentSession();
        s.saveOrUpdate(profile);
        return true;
    }

    /**
     * create a profile
     *
     * @param profile the profile to persist to the database
     * @return boolean value denoting success or failure
     */
    @Override
    public boolean createProfile(Profile profile) {
        Session s = sessionFactory.getCurrentSession();
        s.save(profile);
        return true;
    }

    @Override
    public PaginatedList<Profile> getAll() {
        String hql = "FROM Profile";
        Session s = sessionFactory.getCurrentSession();
        TypedQuery<Profile> query = s.createQuery(hql, Profile.class)
                .setFirstResult(0)
                .setMaxResults(10);
        List<Profile> p = query.getResultList();
        PaginatedList<Profile> profileList = new PaginatedList<>();
        profileList.setData(p);
        profileList.setNext(String.valueOf(10));
        profileList.setTotalCount(getProfilesCount());
        return profileList;
    }

    @Override
    public PaginatedList<Profile> getAllOffset(int offset) {
        String hql = "FROM Profile";
        Session s = sessionFactory.getCurrentSession();
        TypedQuery<Profile> query = s.createQuery(hql, Profile.class)
                .setFirstResult(offset)
                .setMaxResults(10);
        List<Profile> p = query.getResultList();
        PaginatedList<Profile> profileList = new PaginatedList<>();
        profileList.setData(p);
        profileList.setTotalCount(getProfilesCount());
        profileList.setNext(String.valueOf(offset + 11));
        return profileList;
    }

    private long getProfilesCount() {
        String hql = "SELECT COUNT(*) FROM Profile";
        Session s = sessionFactory.getCurrentSession();
        return (Long) s.createQuery(hql).list().get(0);
    }
}
