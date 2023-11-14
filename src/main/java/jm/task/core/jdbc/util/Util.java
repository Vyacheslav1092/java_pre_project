package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/Users";
    private static final String USER_NAME = "root";
    private static final String USER_PASSWORD = "Ww159753@";
    private static final  SessionFactory sessionFactory;

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER_NAME, USER_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Соединение закрыто");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    static {
        try {
            Configuration configuration = new Configuration()
                    .setProperty("connection.driver_class","com.mysql.jdbc.Driver")
                    .setProperty("hibernate.connection.url", URL)
                    .setProperty("hibernate.connection.username", USER_NAME)
                    .setProperty("hibernate.connection.password", USER_PASSWORD)
                    .setProperty("hibernate.connection.characterEncoding", "utf8")
                    .setProperty("show_sql", "true")
                    .setProperty("format_sql", "true")
                    .addAnnotatedClass(User.class);
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            sessionFactory = configuration.buildSessionFactory(builder.build());
            System.out.println("Соединение установлено");
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Ошибка при настройке Hibernate");
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
            System.out.println("Соединение Hibernate закрыто");
        }
    }
}

