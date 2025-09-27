package main.java.ORM;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ConnectionHolder {

    protected Connection connection;

    //constructor

    public ConnectionHolder(){
        try {
            this.connection = ConnectionManager.getInstance().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    //getters
    public Connection getConnection() {
        return connection;
    }
}
