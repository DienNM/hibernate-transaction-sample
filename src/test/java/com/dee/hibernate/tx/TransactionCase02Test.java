package com.dee.hibernate.tx;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.junit.Assert;

import com.dee.hibernate.tx.model.StudentModel;

/**
 * @author dien.nguyen
 **/

public class TransactionCase02Test extends TestCase{
    
    public void testOpenMultipleConnection_ConnectionPool_C3P0() {
        int count = 100;
        final String  config = "/config/mysql-hibernate-standalone.cfg.xml";
        SessionUtil.getStatistic(config).clear();
        while((count--) > 0) {
            Runnable runn = new Runnable() {
                @Override
                public void run() {
                    Session session = SessionUtil.getSession(config);
                    session.beginTransaction();
                    StudentModel student1 = new StudentModel();
                    session.persist(student1);
                    session.getTransaction().commit();
                }
            };
            new Thread(runn).start();
        }
        try {
            Thread.sleep(5000);
            Assert.assertEquals(5, SessionUtil.getStatistic(config).getConnectCount());
            Assert.assertEquals(5, SessionUtil.getStatistic(config).getTransactionCount());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
