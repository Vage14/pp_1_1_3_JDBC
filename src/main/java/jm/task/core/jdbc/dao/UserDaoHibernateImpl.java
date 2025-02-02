package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getSessionFactory;


public class UserDaoHibernateImpl implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoHibernateImpl.class);

    public UserDaoHibernateImpl() { // Noncompliant - method is empty
    }


    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50), " +
                "last_name VARCHAR(50), " +
                "age TINYINT)";
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            NativeQuery<?> query = session.createNativeQuery(sql);
            query.executeUpdate();
            transaction.commit();
            LOGGER.info("Таблица пользователей успешно создана.");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            LOGGER.error("Ошибка при создании таблицы пользователей", e);
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            NativeQuery<?> query = session.createNativeQuery(sql);
            query.executeUpdate();
            transaction.commit();
            LOGGER.info("Таблица пользователей успешно удалена.");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            LOGGER.error("Ошибка при удалении таблицы пользователей", e);
        }
    }

    @Override
    public void saveUser(String name, String last_name, byte age) {
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = new User(name, last_name, age);
            session.save(user);
            transaction.commit();
            LOGGER.info("Пользователь {} {} {} добавлен", name, last_name, age);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            LOGGER.error("Ошибка при сохранении", e);
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                LOGGER.info("Пользователь удалён.", id);
            } else {
                LOGGER.warn("Пользователь не найден.", id);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            LOGGER.error("Ошибка при удалении с id " + id, e);
        }

    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Session session = getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User", User.class);
            users = query.getResultList();
            LOGGER.info("Получено {} пользователей.", users.size());
        } catch (Exception e) {
            LOGGER.error("Ошибка при получении списка", e);
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE users";
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            NativeQuery<?> query = session.createNativeQuery(sql);
            query.executeUpdate();
            transaction.commit();
            LOGGER.info("Содержимое пользователей очищено.");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            LOGGER.error("Ошибка при очистке таблицы", e);
        }
    }
}