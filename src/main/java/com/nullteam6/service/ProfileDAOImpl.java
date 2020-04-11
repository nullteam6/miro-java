package com.nullteam6.service;

import com.nullteam6.models.Profile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
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
        try (Session s = sessionFactory.openSession()) {
            Transaction tx = s.beginTransaction();
            s.saveOrUpdate(profile);
            s.flush();
            tx.commit();
            return true;
        } catch (HibernateException ex) {
            logger.info(ex.getMessage());
        }
        return false;
    }

    /**
     * create a profile
     *
     * @param profile the profile to persist to the database
     * @return boolean value denoting success or failure
     */
    @Override
    public boolean createProfile(Profile profile) {
        try (Session s = sessionFactory.openSession()) {
            Transaction tx = s.beginTransaction();
            s.save(profile);
            s.flush();
            tx.commit();
            return true;
        } catch (HibernateException ex) {
            logger.info(ex.getMessage());
        }
        return false;
    }
}
