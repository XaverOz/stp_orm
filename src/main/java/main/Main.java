package main;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Scanner;


public class Main {

    public static SessionFactory sessionFactory = null;

    public static SessionFactory createSessionFactory() {
        StandardServiceRegistry registry = null;
        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            MetadataSources sources = new MetadataSources(registry);
            Metadata metadata = sources.getMetadataBuilder().build();
            sessionFactory = metadata.getSessionFactoryBuilder().build();
        } catch (Exception e) {
            e.printStackTrace();
            if (registry != null) {
                StandardServiceRegistryBuilder.destroy(registry);
            }
        }
        return sessionFactory;
    }

    public static void main(String[] args) {
        Car car = new Car();
        car.setColor("red");
        car.setName("LADA 2110");
        Transaction transaction = null;
        System.out.println("Выберите действие");
        System.out.println("1 просмотр");
        System.out.println("2 редактирование");
        System.out.println("3 удаление");
        System.out.println("4 добавление");
        Scanner keyboard = new Scanner(System.in);
        int myint = keyboard.nextInt();
        if(myint == 4) {
            try (Session session = createSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                session.persist(car);
                transaction.commit();
                System.out.println("добавление успешно");
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }


    }
}


/*
При работе с HQL возникли проблемы для исправления надо:
1) использовать full class name в конфиге
2) использовать full class name при получении
<mapping class="entity.House"/>
        <mapping class="entity.Room"/>
        <mapping class="entity.Sensor"/>
        <mapping class="entity.Value"/>
        // получали через класс посредник
        houses.get_content("from House", entity.House.class);
        
здесь я отобразил основные типы API для работы с таблицами в Hibernate
        
        //1 HQL
        session.createQuery("from House").list();

            //2 JPA Criteria API
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<House> cr = cb.createQuery(House.class);
            Root<House> root = cr.from(House.class);
            cr.select(root);
            Query<House> query = session.createQuery(cr);
            List<House> results = query.getResultList();

            //3 Hibernate Criteria API
            session.createCriteria(House.class).list();

            //4 native queries
            session.createNativeQuery("select * from House");
*/*
