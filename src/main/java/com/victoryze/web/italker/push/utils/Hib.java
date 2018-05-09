package com.victoryze.web.italker.push.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Created by dsz on 2018/2/12.
 */
public class Hib {
    private static SessionFactory sessionFactory;

    static {
        init();
    }

    private static void init() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            sessionFactory = new MetadataSources(registry)
                    .buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static Session session() {
        return sessionFactory.getCurrentSession();
    }

    public interface Query<T> {
        T query(Session session);
    }

    /**
     * 简化Session操作的工具方法
     * 具有一个返回值
     *
     * @param query
     * @param <T>
     * @return
     */
    public static <T> T query(Query<T> query) {
        Session session = sessionFactory.openSession();
        //开启事务
        final Transaction transaction = session.beginTransaction();
        T t = null;
        try {
            t = query.query(session);
            //提交
            transaction.commit();
        } catch (Exception e) {
            //回滚
            transaction.rollback();
        } finally {
            //无论成功失败都需要关闭Session
            session.close();
        }
        return t;
    }

    public interface QueryOnly {
        void query(Session session);
    }

    public static void queryOnly(QueryOnly query) {
        Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        try {
            query.query(session);

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();

        } finally {
            session.close();
        }


    }


}
