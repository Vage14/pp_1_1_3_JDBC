package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Util {
    private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

    private  static  final  String DB_URL = "jdbc:mysql://127.0.0.1:3306/pp_1_1_3bd";
    private  static  final  String DB_USERNAME = "root";
    private  static  final  String DB_PASSWORD = "v14ag93Kh";

    public static Connection getConnection() {
        Connection connection = null ;
        try {
            connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
            LOGGER.info("Connection OK");
        } catch (SQLException e) {
            LOGGER.error("Ошибка подключения к БД");
        }
        return connection;
    }
    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration()
                    .setProperty("hibernate.connection.url", DB_URL)
                    .setProperty("hibernate.connection.username", DB_USERNAME)
                    .setProperty("hibernate.connection.password", DB_PASSWORD)
                    .setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
                    .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect")
                    .setProperty("hibernate.show_sql", "true")
                    .addAnnotatedClass(User.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            LOGGER.info("SessionFactory создан.");
            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            LOGGER.error("Ошибка при создании SessionFactory", e);
            throw new RuntimeException("Не удалось создать SessionFactory", e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}