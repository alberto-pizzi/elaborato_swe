package main.java.DomainModel;

import java.sql.SQLException;

public interface Observer {
    //methods

    void update(Observable observable) throws SQLException;


}
