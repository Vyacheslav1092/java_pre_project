package jm.task.core.jdbc.util;

import java.sql.*;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/Users";
    private static final String USER_NAME = "root";
    private static final String USER_PASSWORD = "Ww159753@";

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
}

