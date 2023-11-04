package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static void main(String[] args) {

    }


    public void createTable(String url, String userName, String userPassword) {
        try (Connection connection = DriverManager.getConnection(url, userName, userPassword);
             Statement statement = connection.createStatement()) {
            String createDataBaseSQL = "CREATE DATABASE IF NOT EXISTS Users";
            statement.execute(createDataBaseSQL);

            String useDataBaseSQL = "USE Users";
            statement.execute(useDataBaseSQL);

            String createTableSQL = "CREATE TABLE IF NOT EXISTS `Users`.`users` (" +
                    "`id` INT NOT NULL AUTO_INCREMENT, " +
                    "`name` VARCHAR(45) NOT NULL, " +
                    "`lastName` VARCHAR(45) NOT NULL, " +
                    "`age` TINYINT NOT NULL, PRIMARY KEY (`id`))";
            statement.execute(createTableSQL);

            System.out.println("Таблица создана");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUsersToTable(String url, String userName, String userPassword, List<User> man) {
        try (Connection connection = DriverManager.getConnection(url, userName, userPassword)) {
            String useDataBaseSQL = "USE Users";

            try (Statement useStatement = connection.createStatement()) {
                useStatement.execute(useDataBaseSQL);
            }

            String insertSQL = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";

            for (User user : man) {
                try (PreparedStatement statement = connection.prepareStatement(insertSQL)) {
                    statement.setString(1, user.getName());
                    statement.setString(2, user.getLastName());
                    statement.setByte(3, user.getAge());

                    int rowsAffect = statement.executeUpdate();
                    if (rowsAffect > 0) {
                        System.out.println("Пользователь с именем " + user.getName() + " добавлен");

                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getAllUsers(String url, String userName, String userPassword) {
        List<User> userList = new ArrayList<>(10);

        try (Connection connection = DriverManager.getConnection(url, userName, userPassword);
            Statement statement = connection.createStatement()) {
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
            throw new RuntimeException(e);
        }
    }

    public void clearUsersTable(String url, String userName, String userPassword) {
        try (Connection connection = DriverManager.getConnection(url, userName, userPassword);
             Statement statement = connection.createStatement()) {
            String clearTableSQL = "DELETE FROM Users";
            statement.executeUpdate(clearTableSQL);
            System.out.println("Таблица Users очищена");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTable(String url, String userName, String userPassword) {
        try (Connection connection = DriverManager.getConnection(url, userName, userPassword);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = connection.getMetaData().getTables(
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
}

