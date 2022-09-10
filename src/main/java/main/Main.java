package main;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Random;
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
        Transaction transaction = null;
        System.out.println("Выберите действие");
        System.out.println("1 просмотр");
        System.out.println("2 редактирование");
        System.out.println("3 удаление");
        System.out.println("4 добавление");
        System.out.println("5 выход");
        Scanner keyboard = new Scanner(System.in);
        Random rn = new Random();
        try (Session session = createSessionFactory().openSession()) {
            while (true) {
                int myint = keyboard.nextInt();
                if (myint == 4) {
                    transaction = session.beginTransaction();
                    Car car = new Car();
                    car.setColor("red");
                    car.setName("LADA 211"+ rn.nextInt());
                    session.persist(car);
                    transaction.commit();
                    System.out.println("добавление успешно");
                } else if (myint == 1) {
                    List<Car> cars = session.createCriteria(Car.class).add(Restrictions.ge("id", 1L)).list();
                    for (Car carV : cars) {
                        System.out.println(carV);
                    }
                }
                if (myint == 5) {
                    return;
                }
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
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
*/
