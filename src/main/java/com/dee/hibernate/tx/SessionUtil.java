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
    
    private SessionUtil(String configPath) {
        Configuration configuration = new Configuration();
        configuration.configure(configPath);
        
        StandardServiceRegistryBuilder srBuilder = new StandardServiceRegistryBuilder();
        srBuilder.applySettings(configuration.getProperties());
        StandardServiceRegistry ssRegister = srBuilder.build();
        
        sessionFactory = configuration.buildSessionFactory(ssRegister);
    }
    
    private SessionUtil() {
        this("/config/mysql-hibernate.cfg.xml");
    }
    
    public synchronized static SessionUtil getInstance(String configPath) {
        if(instance == null) {
            instance = new SessionUtil(configPath);
        }
        return instance;
    }
    
    public synchronized static SessionUtil getInstance() {
        if(instance == null) {
            instance = new SessionUtil();
        }
        return instance;
    }
    
    public static Session getSession(String configPath) {
        return getInstance(configPath).sessionFactory.openSession();
    }
    
    public static Session getSession() {
        return getInstance().sessionFactory.openSession();
    }
    
    public static Statistics getStatistic(String configPath) {
        return getInstance(configPath).sessionFactory.getStatistics();
    }
    
    public static Statistics getStatistic() {
        return getInstance().sessionFactory.getStatistics();
    }
    
    public static SessionFactory getSessionFactory() {
        return getInstance().sessionFactory;
    }
    
    public static SessionFactory getSessionFactory(String configPath) {
        return getInstance(configPath).sessionFactory;
    }
    
}
