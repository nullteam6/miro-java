package com.nullteam6.utility;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionUtility {
    private static Session session;
    private static SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

    // Default Constructor to shut SonarLint Up
    private SessionUtility() {
        super();
    }

    public static Session getSession() {
        if (session == null)
            session = sessionFactory.openSession();
        return session;
    }

    public static void closeSession() {
        session.close();
        session = null;
    }
}
