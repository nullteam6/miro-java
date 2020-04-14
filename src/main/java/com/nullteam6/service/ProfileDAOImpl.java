package com.nullteam6.service;

import com.nullteam6.models.Profile;
import com.nullteam6.utility.PaginatedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.NonUniqueObjectException;
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
        Session s = sessionFactory.getCurrentSession();
        String hql = "FROM Profile P WHERE P.uid = :uid";
        return (Profile) s.createQuery(hql)
                .setParameter("uid", uid)
                .getSingleResult();
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
        try {
            s.saveOrUpdate(profile);
        } catch (NonUniqueObjectException ex) {
            Profile p = s.get(Profile.class, profile.getId());
            s.evict(p);
            s.merge(profile);
        }

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
    public PaginatedList<Profile> search(String uid) {
        Session s = sessionFactory.getCurrentSession();
        String hql = "FROM Profile P WHERE P.uid LIKE CONCAT('%',:uid,'%')";
        TypedQuery<Profile> query = s.createQuery(hql, Profile.class)
                .setParameter("uid", uid)
                .setFirstResult(0)
                .setMaxResults(10);
        return buildProfilePage(query.getResultList(), 0, getProfileSearchCount(uid));
    }

    @Override
    public PaginatedList<Profile> searchOffset(String uid, int offset) {
        Session s = sessionFactory.getCurrentSession();
        String hql = "FROM Profile P WHERE P.uid LIKE CONCAT('%',:uid,'%')";
        TypedQuery<Profile> query = s.createQuery(hql, Profile.class)
                .setParameter("uid", uid)
                .setFirstResult(offset)
                .setMaxResults(10);
        return buildProfilePage(query.getResultList(), offset, getProfileSearchCount(uid));
    }

    @Override
    public PaginatedList<Profile> getAll() {
        String hql = "FROM Profile";
        Session s = sessionFactory.getCurrentSession();
        TypedQuery<Profile> query = s.createQuery(hql, Profile.class)
                .setFirstResult(0)
                .setMaxResults(10);
        return buildProfilePage(query.getResultList(), 0, getProfilesCount());
    }

    @Override
    public PaginatedList<Profile> getAllOffset(int offset) {
        String hql = "FROM Profile";
        Session s = sessionFactory.getCurrentSession();
        TypedQuery<Profile> query = s.createQuery(hql, Profile.class)
                .setFirstResult(offset)
                .setMaxResults(10);
        return buildProfilePage(query.getResultList(), offset, getProfilesCount());
    }

    private long getProfilesCount() {
        String hql = "SELECT COUNT(*) FROM Profile";
        Session s = sessionFactory.getCurrentSession();
        return (Long) s.createQuery(hql).list().get(0);
    }

    private long getProfileSearchCount(String uid) {
        String hql = "SELECT COUNT(*) FROM Profile P WHERE P.uid LIKE CONCAT('%',:uid,'%')";
        Session s = sessionFactory.getCurrentSession();
        return (Long) s.createQuery(hql).setParameter("uid", uid).list().get(0);
    }

    private PaginatedList<Profile> buildProfilePage(List<Profile> profileList, int offset, long totalSize) {
        PaginatedList<Profile> profilePage = new PaginatedList<>();
        profilePage.setData(profileList);
        profilePage.setTotalCount(totalSize);
        profilePage.setNext(String.valueOf(offset + 10));
        return profilePage;
    }
}
