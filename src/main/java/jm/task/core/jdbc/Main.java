package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();

        userService.createUsersTable();
        userService.saveUser("Петька", "Петров", (byte) 20);
        userService.saveUser("Светка", "Светлова", (byte) 23);
        userService.saveUser("Олька", "Орлова", (byte) 19);
        userService.saveUser("Пашка", "Пахан", (byte) 22);

        userService.getAllUsers();
        userService.cleanUsersTable();
        userService.dropUsersTable();

        Util.closeConnectionHibernate();
    }
}
