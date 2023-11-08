package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getConnection;

public class UserDaoJDBCImpl implements UserDao {
    private static final Connection connect = getConnection();
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Statement statement = connect.createStatement()) {

            String useDataBaseSQL = "USE Users";
            statement.execute(useDataBaseSQL);

            String createTableSQL = "CREATE TABLE IF NOT EXISTS `Users`.`users` (" +
                    "`id` BIGINT NOT NULL AUTO_INCREMENT, " +
                    "`name` VARCHAR(45) NOT NULL, " +
                    "`lastName` VARCHAR(45) NOT NULL, " +
                    "`age` TINYINT NOT NULL, PRIMARY KEY (`id`))";
            statement.execute(createTableSQL);

            System.out.println("Таблица создана");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connect.createStatement()) {
            ResultSet resultSet = connect.getMetaData().getTables(
                    null,
                    null,
                    "users",
                    null
            );

            if (resultSet.next()) {
                String deleteTableSQL = "DROP TABLE IF EXISTS Users";
                statement.executeUpdate(deleteTableSQL);
                System.out.println("База данных успешно удалена");
            } else {
                System.out.println("База данных не существует");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Statement useStatement = connect.createStatement()) {
            String useDataBaseSQL = "USE Users";
            useStatement.execute(useDataBaseSQL);

            String insertSQL = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";

            try (PreparedStatement statement = connect.prepareStatement(insertSQL)) {
                statement.setString(1, name);
                statement.setString(2, lastName);
                statement.setByte(3, age);

                int rowsAffect = statement.executeUpdate();
                if (rowsAffect > 0) {
                    System.out.println("Пользователь с именем " + name + " добавлен");
                }
            }
        } catch (SQLException e) {
            try {
                connect.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try (Statement statement = connect.createStatement()) {
            String useDataBaseSQL = "USE Users";
            statement.execute(useDataBaseSQL);

            String deleteSQL = "DELETE FROM users WHERE id = ?";

            try (PreparedStatement preparedStatement = connect.prepareStatement(deleteSQL)) {
                preparedStatement.setLong(1, id);

                int rowsAffect = preparedStatement.executeUpdate();
                if (rowsAffect > 0) {
                    System.out.println("Пользователь с id № " + id + " удален");
                }
            }
        } catch (SQLException e) {
            try {
                connect.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>(10);

        try (Statement statement = connect.createStatement()) {
            String selectSQL = "SELECT * FROM Users";

            try (ResultSet resultSet = statement.executeQuery(selectSQL)) {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String lastName = resultSet.getString("lastName");
                    byte age = resultSet.getByte("age");

                    User user = new User(name, lastName, age);
                    userList.add(user);
                }
                userList.forEach(user -> System.out.println(user.toString()));
            }
        } catch (SQLException e) {
            try {
                connect.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
        return userList;
    }

    public void cleanUsersTable() {
        try (Statement statement = connect.createStatement()) {
            String clearTableSQL = "DELETE FROM Users";
            statement.executeUpdate(clearTableSQL);
            System.out.println("Таблица Users очищена");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
