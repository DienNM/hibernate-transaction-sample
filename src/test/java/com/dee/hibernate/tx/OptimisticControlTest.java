package com.dee.hibernate.tx;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;
import org.junit.Assert;

import com.dee.hibernate.tx.model.StudentVersionModel;

/**
 * @author dien.nguyen
 */
public class OptimisticControlTest extends TestCase{
    
    final String  config = "/config/mysql-hibernate-optimistic.cfg.xml";
    
    public void testVersioning() {
        SessionUtil.getStatistic(config).clear();
        Session session = SessionUtil.getSession(config);
        session.beginTransaction();
        StudentVersionModel student1 = new StudentVersionModel();
        session.persist(student1);
        session.getTransaction().commit();
        session.close();
        
        session = SessionUtil.getSession(config);
        session.beginTransaction();
        StudentVersionModel pStudent = (StudentVersionModel) session.get(StudentVersionModel.class, student1.getId());
        Assert.assertEquals(0, pStudent.getVersion());
        pStudent.setName("update name");
        session.getTransaction().commit();
        session.close();
    
        session = SessionUtil.getSession(config);
        session.beginTransaction();
        pStudent = (StudentVersionModel) session.get(StudentVersionModel.class, student1.getId());
        Assert.assertEquals(1, pStudent.getVersion());
        session.getTransaction().commit();
        session.close();
    }
    
    public void testStaleObject() {
        Session session = SessionUtil.getSession(config);
        session.beginTransaction();
        StudentVersionModel student1 = new StudentVersionModel();
        session.persist(student1);
        session.getTransaction().commit();
        session.close();
        
        Session session1 = SessionUtil.getSession(config);
        session1.beginTransaction();
        StudentVersionModel pStudent1= (StudentVersionModel) session1.get(StudentVersionModel.class, student1.getId());
        
        Session session2 = SessionUtil.getSession(config);
        session2.beginTransaction();
        StudentVersionModel pStudent2 = (StudentVersionModel) session2.get(StudentVersionModel.class, student1.getId());
        pStudent2.setName("Update second time");
        session2.getTransaction().commit();
        session2.close();
        
        try {
            pStudent1.setName("Update third time");
            session1.getTransaction().commit();
            Assert.fail();
        } catch(StaleObjectStateException ex) {
            session1.getTransaction().rollback();
            Assert.assertNotNull(ex.getMessage());
        }finally {
            session1.close();
        }
    }
    
}
