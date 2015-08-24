package com.dee.hibernate.tx;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import junit.framework.TestCase;

import org.hibernate.Session;

import com.dee.hibernate.tx.model.StudentModel;

/**
 * @author dien.nguyen
 **/

public class JTATransactionCase01Test extends TestCase{
    public void testOpenMultipleConnection() throws Exception {
        final String config = "/config/mysql-hibernate-jta.cfg.xml";
        UserTransaction tx = null;
        Session session = null;
        try {
            Properties props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.as.naming.InitialContextFactory");
            props.put(Context.PROVIDER_URL, "remote://127.0.0.1:4447");
            InitialContext initialContext = new InitialContext(props);
            tx = (UserTransaction) initialContext.lookup("java:jboss/datasources/ExampleDS");
            tx.begin();
            
            session = SessionUtil.getSession(config);
            StudentModel student1 = new StudentModel();
            student1.setName("Test User");
            session.persist(student1);
            session.flush(); // Explicitly Flush
            
            tx.commit();
        } catch(Exception e) {
            e.printStackTrace();
            tx.rollback();
        }finally {
            session.close();
        }
    }
}
