package com.rejnowski.bluemedia.db;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBDao {

    public Optional<User> getUser(String login){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("login", login));
        List<User> list = criteria.list();

        if(list.size()==1) return Optional.of(list.get(0));
       // User user = (User) session.get(User.class, 1);
     //   System.out.println(user.getLogin());
        return Optional.empty();

    }
}
