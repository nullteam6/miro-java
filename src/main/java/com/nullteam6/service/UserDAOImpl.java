package com.nullteam6.service;

import com.nullteam6.models.User;
import com.nullteam6.models.UserTemplate;
import com.nullteam6.utility.PBKDF2Hasher;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserDAOImpl implements UserDAO {
    @Autowired
    private SessionFactory sf;

    @Override
    public User findByUsername(String username) {
        String hql = "FROM User U WHERE U.username = :username";
        User u = null;
        try (Session s = sf.openSession()) {
            u = (User) s.createQuery(hql)
                        .setParameter("username", username)
                        .getSingleResult();
        } catch (HibernateException ex) {
            // TODO: Log this
            ex.printStackTrace();
        }
        return u;
    }

    @Override
    @Transactional
    public boolean registerUser(UserTemplate template) {
        User u = new User(template);

        // Hash incoming user password
        PBKDF2Hasher hasher = new PBKDF2Hasher();
        char[] passwordCharArr = u.getPassword().toCharArray();
        u.setPassword(hasher.hash(passwordCharArr));

        Session s = sf.getCurrentSession();
        try {
            s.save(u);
            return true;
        } catch (HibernateException ex) {
            // TODO: Log it real good
            ex.printStackTrace();
        }
        // TODO: throw exception
        return false;
    }
}
