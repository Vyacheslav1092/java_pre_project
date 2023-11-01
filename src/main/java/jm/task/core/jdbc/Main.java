package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/";
        String userName = "root";
        String userPassword = "Ww159753@";

        List<User> usersList = List.of(
                new User("Petr", "Petrov", (byte) 18),
                new User("Ivan", "Ivanov", (byte) 20),
                new User("Oksana", "Oksanovna", (byte) 21),
                new User("Tixon", "Tixonov", (byte) 24)
        );

        Util util = new Util();

        util.createTable(url, userName, userPassword);
        util.addUsersToTable(url, userName, userPassword, usersList);
        util.getAllUsers(url, userName, userPassword);
        util.clearUsersTable(url, userName, userPassword);
        util.deleteTable(url, userName, userPassword);
    }
}
