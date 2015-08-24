package com.dee.hibernate.tx;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;
import org.junit.Assert;

import com.dee.hibernate.tx.model.StudentModel;

/**
 * @author dien.nguyen
 */
public class ReadUncommittedIsolationTest extends TestCase {
    
    public void testReadUnrepeatable() {
        final String  config = "/config/mysql-hibernate-read-uncommitted-isolation.cfg.xml";
        
        Session session = SessionUtil.getSession(config);
        session.beginTransaction();
        StudentModel student1 = new StudentModel();
        session.persist(student1);
        session.getTransaction().commit();
        session.close();
        
        Session session1 = SessionUtil.getSession(config);
        session1.beginTransaction();
        StudentModel pStudent1= (StudentModel) session1.get(StudentModel.class, student1.getId());
        
        Session session2 = SessionUtil.getSession(config);
        session2.beginTransaction();
        StudentModel pStudent2 = (StudentModel) session2.get(StudentModel.class, student1.getId());
        pStudent2.setName("Update second time");
        session2.getTransaction().commit();
        session2.close();
        
        try {
            pStudent1.setName("Update third time");
            session1.getTransaction().commit();
        } catch(StaleObjectStateException ex) {
            Assert.fail();
        }finally {
            session1.close();
        }
        
        session1 = SessionUtil.getSession(config);
        pStudent1= (StudentModel) session1.get(StudentModel.class, student1.getId());
        Assert.assertEquals("Update third time", pStudent1.getName());
        session1.close();
    }
}
