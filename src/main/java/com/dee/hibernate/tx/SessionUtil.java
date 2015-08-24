package com.dee.hibernate.tx;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;

/**
 * @author dien.nguyen
 **/

public final class SessionUtil {
    
    private final SessionFactory sessionFactory;
    private static SessionUtil instance;
    
    private SessionUtil() {
        Configuration configuration = new Configuration();
        configuration.configure("/config/mysql-hibernate.cfg.xml");
        
        StandardServiceRegistryBuilder srBuilder = new StandardServiceRegistryBuilder();
        srBuilder.applySettings(configuration.getProperties());
        StandardServiceRegistry ssRegister = srBuilder.build();
        
        sessionFactory = configuration.buildSessionFactory(ssRegister);
    }
    
    public synchronized static SessionUtil getInstance() {
        if(instance == null) {
            instance = new SessionUtil();
        }
        return instance;
    }
    
    public static Session getSession() {
        return getInstance().sessionFactory.openSession();
    }
    
    public static Statistics getStatistic() {
        return getInstance().sessionFactory.getStatistics();
    }
    
    public static SessionFactory getSessionFactory() {
        return getInstance().sessionFactory;
    }
    
}
