package com.nullteam6.service;

import com.nullteam6.models.Anime;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public class AnimeDAOImpl implements AnimeDAO {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Persist an anime in the database
     *
     * @param a The anime to persist
     */
    @Override
    public void add(Anime a) {
        try (Session s = sessionFactory.openSession()) {
            Transaction tx = s.beginTransaction();
            s.persist(a);
            tx.commit();
        }
    }

    /**
     * Get an anime by id from the database
     *
     * @param id the id of the Anime to lookup
     * @return The anime requested
     */
    @Override
    public Anime get(int id) {
        Anime a = null;
        try (Session s = sessionFactory.openSession()) {
            a = s.get(Anime.class, id);
        }
        return a;
    }
}
