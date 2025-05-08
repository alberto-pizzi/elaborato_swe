package main.java.DomainModel;

import java.sql.SQLException;

public interface Observer {
    //methods

    void update() throws SQLException;

    void attach();
    void detach();

}
