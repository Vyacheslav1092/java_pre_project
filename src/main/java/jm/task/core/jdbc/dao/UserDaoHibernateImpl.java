package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

import static jm.task.core.jdbc.util.Util.configHibernate;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = configHibernate();
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            String createTableSQL = "CREATE TABLE IF NOT EXISTS `Users`.`users` (" +
                    "`id` BIGINT NOT NULL AUTO_INCREMENT, " +
                    "`name` VARCHAR(45) NOT NULL, " +
                    "`lastName` VARCHAR(45) NOT NULL, " +
                    "`age` TINYINT NOT NULL, PRIMARY KEY (`id`))";
            session.createSQLQuery(createTableSQL).executeUpdate();
            transaction.commit();
            System.out.println("Таблица создана");
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUsersTable() {
        String deleteTableSQL = "DROP TABLE IF EXISTS Users";
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery(deleteTableSQL).executeUpdate();
            transaction.commit();

            System.out.println("База данных успешно удалена");
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                session.save(user);
                transaction.commit();
                System.out.println("Пользователь с именем " + name + " добавлен");
            } catch (HibernateException e) {
                transaction.rollback();
                throw new RuntimeException(e);
            }
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            User userId = session.get(User.class, id);
            try {
                session.delete(userId);
                transaction.commit();

                System.out.println("Пользователь с id № " + id + " удален");
            } catch (HibernateException e) {
                transaction.rollback();
                throw new RuntimeException(e);
            }
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList;
        try (Session session = sessionFactory.openSession()) {
            String selectSQL = "FROM User";
            userList = session.createQuery(selectSQL, User.class).getResultList();
            userList.forEach(user -> System.out.println(user.toString()));
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            String selectSQL = "DELETE FROM Users";
            try {
                session.createSQLQuery(selectSQL).executeUpdate();
                transaction.commit();
                System.out.println("Таблица Users очищена");
            } catch (HibernateException e) {
                transaction.rollback();
                throw new RuntimeException(e);
            }
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }
}
