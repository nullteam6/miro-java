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
    private Logger logger = LogManager.getLogger();

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

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
