package com.memorynotfound.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Arrays;
import java.util.List;

public class App {

    public static void main (String...args){

        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();

        Address address = new Address("street", "city", "1234", "state");
        Person person = new Person("John Doe", address);

        session.save(person);
        tx.commit();

        List<Person> activities = (List<Person>)session.createQuery("from Person").list();
        System.out.println(Arrays.toString(activities.toArray()));

        session.close();
        HibernateUtil.shutdown();
    }
}
