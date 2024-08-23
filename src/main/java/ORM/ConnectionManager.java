package main.java.ORM;

import java.sql.*;

public class ConnectionManager {
    //TODO insert DB data (url, username and password)
    private static final String url = "";
    private static final String username = "";
    private static final String password = "";
    private static Connection connection = null;

    // singleton instance
    private static ConnectionManager instance = null;

    private ConnectionManager(){}

    public static ConnectionManager getInstance() {

        if (instance == null) { instance = new ConnectionManager(); }

        return instance;
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {

        Class.forName("org.postgresql.Driver");

        if (connection == null)
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                System.err.println("Error: " + e.getMessage());
            }

        return connection;

    }
}
