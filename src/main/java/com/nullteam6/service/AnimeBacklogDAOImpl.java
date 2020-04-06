package com.nullteam6.service;

import com.nullteam6.models.AnimeBacklog;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public class AnimeBacklogDAOImpl implements AnimeBacklogDAO {

    SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public AnimeBacklog getById(int id) {
        AnimeBacklog backlog;
        try (Session s = sessionFactory.openSession()) {

            backlog = s.get(AnimeBacklog.class, id);
        }
        return backlog;
    }

    @Override
    public void createBacklog(AnimeBacklog backlog) {
        try (Session s = sessionFactory.openSession()) {
            Transaction tx = s.beginTransaction();
            s.persist(backlog);
            tx.commit();
        }
    }

    @Override
    public void updateBacklog(AnimeBacklog backlog) {
        try (Session s = sessionFactory.openSession()) {
            Transaction tx = s.beginTransaction();
            s.update(backlog);
            tx.commit();
        }
    }
}
