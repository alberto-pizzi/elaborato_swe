package main.java.DomainModel;

import java.sql.SQLException;

public interface Observer {
    //methods

    void update() throws SQLException, ClassNotFoundException;

    void attach();
    void detach();

}
